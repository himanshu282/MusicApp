package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.adapters.PlaylistSongAdapter
import com.example.musicapp.databinding.ActivityPlaylistSongsBinding
import com.example.musicapp.model.Songs
import com.example.musicapp.model.SongsWithPosition
import com.example.musicapp.services.AudioPlayerService
import com.example.musicapp.viewmodels.PlaylistSongViewModel

class PlaylistSongsActivity : AppCompatActivity() {

    private val model : PlaylistSongViewModel by viewModels()
    private val listOfSongs : ArrayList<Songs> = arrayListOf()
    private lateinit var binding: ActivityPlaylistSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = intent.getStringExtra("playListName")
        model.getPlaylistWithSongs(intent.getStringExtra("playListName").toString())

        model.data.observe(this) {
            val playlistSongAdapter = PlaylistSongAdapter { position ->
                val songsWithPosition = SongsWithPosition(listOfSongs, position)
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("songs", songsWithPosition)
                startActivity(intent)
                startService(Intent(this, AudioPlayerService::class.java))
            }
            listOfSongs.clear()

            it?.songs?.forEach {
                listOfSongs.add(Songs(it.songUrl, it.songName, it.singerName))
            }

            playlistSongAdapter.submitList(it?.songs)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = playlistSongAdapter
            }

    }
}