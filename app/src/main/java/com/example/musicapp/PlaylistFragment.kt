package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.adapters.PlaylistAdapter
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.viewmodels.PlaylistFragmentViewModel

class PlaylistFragment : Fragment() {
    private lateinit var binding : FragmentPlaylistBinding
    private val model : PlaylistFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.getPlaylist()
        val playlistAdapter = PlaylistAdapter {
            val intent = Intent(context,PlaylistSongsActivity::class.java)
            intent.putExtra("playListName",it)
            Log.d("TAG", "playlistnamefromadapter: $it")
            startActivity(intent)
        }
        model.playlist.observe(viewLifecycleOwner){
            playlistAdapter.submitList(it)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter=playlistAdapter
        super.onViewCreated(view, savedInstanceState)
    }


}