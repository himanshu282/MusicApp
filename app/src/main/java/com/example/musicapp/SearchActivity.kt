package com.example.musicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.databinding.MusicPlayerBinding
import com.example.musicapp.model.Songs
import com.example.musicapp.model.SongsWithPosition
import com.example.musicapp.services.AudioPlayerService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.*


class SearchActivity : AppCompatActivity() {
    var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private lateinit var binding: MusicPlayerBinding
    private var mAudioService: AudioPlayerService? = null


    var isBound = false
    var job: Job? = null
    var song: SongsWithPosition? = null

    private fun doBindService(songs: SongsWithPosition?) {
        val playServiceIntent = Intent(this, AudioPlayerService::class.java)
        playServiceIntent.putExtra("song", songs)
        bindService(playServiceIntent, playServiceConnection, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    private val playServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AudioPlayerService.ServiceBinder
            mAudioService = binder.getPlayerService()
            player = binder.getPlayerService().player
            mAudioService?.songName?.observe(this@SearchActivity){
                binding.songName.text = it
            }
            mAudioService?.singerName?.observe(this@SearchActivity){
                binding.singerName.text=it
            }
            song?.index?.let {
                mAudioService?.setSongIndex(it)
            }
            binding.exoProgress.progress = player?.currentPosition?.div(1000L)?.toInt() ?: 0
            binding.initialTime.text = player?.currentPosition?.div(1000L)?.toInt().toString()
            binding.totalTime.text = player?.duration?.div(1000).toString()
            setPlayPauseVisibility()
            player?.addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == ExoPlayer.STATE_READY) {
                        setSeekbarPosition()
                    }

                    if (player?.isPlaying == true) {
                        binding.playSong.visibility = View.INVISIBLE
                        binding.pauseSong.visibility = View.VISIBLE
                    } else {
                        binding.playSong.visibility = View.VISIBLE
                        binding.pauseSong.visibility = View.INVISIBLE
                    }
                }
            })
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            //Something to do
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        song = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("songs", SongsWithPosition::class.java)
        } else {
            intent.getParcelableExtra("songs")
        }
        doBindService(song)

        binding.initialTime.text = "0"
        binding.playSong.setOnClickListener {
            player?.playWhenReady = playWhenReady
            binding.playSong.visibility = View.INVISIBLE
            binding.pauseSong.visibility = View.VISIBLE
        }

        binding.pauseSong.setOnClickListener {
            player?.playWhenReady = false
            job?.cancel()
            binding.playSong.visibility = View.VISIBLE
            binding.pauseSong.visibility = View.INVISIBLE
        }

        binding.forwardTime.setOnClickListener {
            player?.seekTo(player?.currentPosition?.plus(10000L) ?: 0)
        }

        binding.backwardTime.setOnClickListener {
            player?.seekTo(player?.currentPosition?.minus(10000L) ?: 0)
        }

        binding.nextSong.setOnClickListener {
            player?.seekToNext()
        }

        binding.previousSong.setOnClickListener {
            player?.seekToPrevious()
        }

        binding.addToPlaylist.setOnClickListener {
            val playlistDialogFragment = PlaylistDialogFragment()
            val bundle = Bundle()
            val song : Songs? = mAudioService?.song?.list?.get(player?.currentMediaItemIndex!!.toInt())
            bundle.putParcelable("song",song)
            playlistDialogFragment.arguments=bundle
            playlistDialogFragment.show(
                supportFragmentManager,
                this@SearchActivity.localClassName
            )
        }

        binding.exoProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

                /* if (progress > 0 && player != null && player?.isPlaying == false) {
                     player?.release()
                     binding.exoProgress.progress = 0
                 }*/
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.d("TAG", "onStartTrackingTouch: ")

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (player != null && player?.isPlaying == true) {
                    player?.seekTo((p0?.progress?.toLong()?.times(1000L) ?: 0))
                    binding.exoProgress.progress = p0?.progress ?: 0
                }
            }
        })
    }


    private fun initializePlayer() {
        val songs =
            arrayListOf(getString(R.string.media_url_mp3), getString(R.string.media_url_mp3))
        val newSongs = arrayListOf<MediaItem>()
        lateinit var mediaUri: Uri
        player = ExoPlayer.Builder(this).build()
        songs.forEach {
            mediaUri = Uri.parse(it)
            val mediaItem = MediaItem.fromUri(mediaUri)
            newSongs.add(mediaItem)
        }
        player?.setMediaItems(newSongs)
        player?.prepare()
        player?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    val realDurationMillis: Long? = player?.duration
                    binding.exoProgress.max = (realDurationMillis?.div(1000))?.toInt() ?: 0
                    binding.totalTime.text = (realDurationMillis?.div(1000))?.toString()
                    job = getSeekProgress()
                }
                if (playbackState == ExoPlayer.COMMAND_SEEK_FORWARD)
                    Log.d("TAG", "helloo: ")
            }
        })


    }


    private fun releasePlayer() {
        player?.let { exoPlayer ->
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

    private fun getSeekProgress(): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            var currentPosition: Int = player?.currentPosition?.toInt() ?: 0
            val total: Int = player?.duration?.toInt() ?: 0
            while (player != null && player?.isPlaying == true && currentPosition < total) {
                currentPosition = try {
                    player?.currentPosition?.toInt() ?: 0
                } catch (e: Exception) {
                    return@launch
                }
                binding.exoProgress.progress = currentPosition / 1000
                binding.initialTime.text = currentPosition.div(1000).toString()
                delay(1000)
            }


        }
    }

    private fun setPlayPauseVisibility() {
        if (player?.isPlaying == true) {
            setSeekbarPosition()
            binding.playSong.visibility = View.INVISIBLE
            binding.pauseSong.visibility = View.VISIBLE
        } else {
            binding.playSong.visibility = View.VISIBLE
            binding.pauseSong.visibility = View.INVISIBLE
        }
    }

    private fun setSeekbarPosition() {
        val realDurationMillis: Long? = player?.duration
        binding.exoProgress.max = (realDurationMillis?.div(1000))?.toInt() ?: 0
        binding.totalTime.text = (realDurationMillis?.div(1000))?.toString()
        job = getSeekProgress()
    }
}