package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.adapters.HomeAdapter
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.model.Songs
import com.example.musicapp.model.SongsWithPosition
import com.example.musicapp.services.AudioPlayerService
import com.example.musicapp.viewmodels.MainViewModel

class HomeFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var songList: ArrayList<Songs> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.responseData.observe(viewLifecycleOwner) {
            val homeAdapter = HomeAdapter { position ->
                val songsWithPosition = SongsWithPosition(songList, position)
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("songs", songsWithPosition)
                startActivity(intent)
                activity?.startService(Intent(context, AudioPlayerService::class.java))
            }
            songList.clear()
            songList.addAll(it.data)
            homeAdapter.submitList(it.data)
            binding.homeRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.homeRecyclerView.adapter = homeAdapter
        }
    }



}