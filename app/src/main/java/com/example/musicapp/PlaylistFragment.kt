package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.adapters.PlaylistAdapter
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.google.gson.Gson

class PlaylistFragment : Fragment() {
    private lateinit var binding : FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistAdapter = PlaylistAdapter {
            val intent = Intent(context,PlaylistSongsActivity::class.java)
            intent.putExtra("playListName",it)
            Log.d("TAG", "playlistnamefromadapter: $it")
            startActivity(intent)
        }
        val playlist : Playlist? = Gson().fromJson(AppPreference.listOfPlaylist,Playlist::class.java)
        playlistAdapter.submitList(playlist?.name?.toList() ?: arrayListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter=playlistAdapter
    }

}