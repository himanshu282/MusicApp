package com.example.musicapp.services

import android.app.Notification
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.R
import com.example.musicapp.SearchActivity
import com.example.musicapp.model.SongsWithPosition
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager


class AudioPlayerService : Service() {

    private val serviceBinder = ServiceBinder()
    var song: SongsWithPosition? = null
    private val songsUrl :ArrayList<String?> = arrayListOf()
    val songName = MutableLiveData<String>()
    val singerName = MutableLiveData<String>()

    inner class ServiceBinder : Binder() {
        fun getPlayerService(): AudioPlayerService {
            return this@AudioPlayerService
        }
    }

    var player: ExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null
    override fun onBind(p0: Intent?): IBinder? {
        song = p0?.extras?.getParcelable("song") as SongsWithPosition?
        song?.list?.forEach {
            songsUrl.add(it.songUrl)
        }
        val newSongs = arrayListOf<MediaItem>()
        lateinit var mediaUri: Uri
        songsUrl.forEach {
            mediaUri = Uri.parse(it)
            val mediaItem = MediaItem.fromUri(mediaUri)
            newSongs.add(mediaItem)
        }
        player?.setMediaItems(newSongs)
        player?.prepare()
        song?.index?.let { player?.seekToDefaultPosition(it) }
        return serviceBinder
    }


    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(applicationContext).build()
        setupNotification()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        player?.setAudioAttributes(audioAttributes, true)
    }


    override fun onDestroy() {
        if (player?.isPlaying == true)
            player?.stop()
        playerNotificationManager?.setPlayer(null)
        player?.release()
        player = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH)
        }
        stopSelf()
        super.onDestroy()
    }


    fun setSongIndex(index:Int){
            player?.stop()
            player?.prepare()
            player?.playWhenReady=true
            player?.seekToDefaultPosition(index)
    }

    private fun setupNotification(){
        val channelId = resources.getString(R.string.app_name)
        val notificationId = 111

        playerNotificationManager =
            PlayerNotificationManager.Builder(this, notificationId, channelId)
                .setChannelImportance(IMPORTANCE_HIGH)
                .setSmallIconResourceId(R.drawable.music_icon)
                .setChannelDescriptionResourceId(R.string.app_name)
                .setNextActionIconResourceId(R.drawable.next_song)
                .setPreviousActionIconResourceId(R.drawable.previous)
                .setPlayActionIconResourceId(R.drawable.play_button)
                .setPauseActionIconResourceId(R.drawable.pause_button)
                .setChannelNameResourceId(R.string.app_name)
                .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                    override fun onNotificationPosted(
                        notificationId: Int,
                        notification: Notification,
                        ongoing: Boolean
                    ) {
                        super.onNotificationPosted(notificationId, notification, ongoing)
                        startForeground(notificationId, notification)
                    }

                    override fun onNotificationCancelled(
                        notificationId: Int,
                        dismissedByUser: Boolean
                    ) {
                        super.onNotificationCancelled(notificationId, dismissedByUser)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            stopForeground(STOP_FOREGROUND_DETACH)
                        }
                        if (player?.isPlaying == true)
                            player?.pause()
                    }
                })
                .setMediaDescriptionAdapter(object :
                    PlayerNotificationManager.MediaDescriptionAdapter {
                    override fun getCurrentContentTitle(player: Player): CharSequence {
                        singerName.value = player.currentMediaItemIndex.let { song?.list?.get(it)?.singerName }
                        songName.value = player.currentMediaItemIndex.let { it1 -> song?.list?.get(it1)?.songName }.toString()
                        return player.currentMediaItemIndex.let { it1 -> song?.list?.get(it1)?.songName }.toString()
                    }

                    override fun createCurrentContentIntent(player: Player): PendingIntent? {
                        val openAppIntent = Intent(applicationContext, SearchActivity::class.java)
                        return PendingIntent.getActivity(
                            applicationContext,
                            0,
                            openAppIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    }

                    override fun getCurrentContentText(player: Player): CharSequence? {
                        return player.currentMediaItemIndex.let { song?.list?.get(it)?.singerName }
                    }

                    override fun getCurrentLargeIcon(
                        player: Player,
                        callback: PlayerNotificationManager.BitmapCallback
                    ): Bitmap? {
                        return BitmapFactory.decodeResource(resources, R.drawable.music_icon)
                    }

                })
                .build()

        playerNotificationManager?.setPlayer(player)
        playerNotificationManager?.setPriority(NotificationCompat.PRIORITY_MAX)
        playerNotificationManager?.setUseRewindAction(false)
        playerNotificationManager?.setUseFastForwardAction(false)
    }

}