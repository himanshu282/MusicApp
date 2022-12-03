package com.example.musicapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.musicapp.adapters.PlaylistAdapter
import com.example.musicapp.databinding.DialogAddToPlaylistBinding
import com.example.musicapp.model.Song
import com.example.musicapp.model.Songs
import com.example.musicapp.viewmodels.PlaylistSharedViewModel

class PlaylistDialogFragment : DialogFragment() {

    private lateinit var binding: ViewBinding
    private val model : PlaylistSharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.dialog_add_to_playlist,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getPlaylist()
        val songs = arguments?.getParcelable<Songs>("song")
        model.song.value = songs


        val playlistAdapter = PlaylistAdapter { playlistName ->
            model.insertSong(Song(playlistName = playlistName, songUrl = songs?.songUrl ?: "", singerName = songs?.singerName ?: "", songName = songs?.songName ?: ""))
        }
        (binding as DialogAddToPlaylistBinding).createPlaylist.setOnClickListener {
            NewPlayListDialogFragment {
                model.getPlaylist()
                model.playlist.observe(viewLifecycleOwner){
                    playlistAdapter.submitList(it)
                }
            }.show(this.parentFragmentManager,this.activity?.localClassName)
        }

        model.playlist.observe(viewLifecycleOwner){
            playlistAdapter.submitList(it)
        }

        (binding as DialogAddToPlaylistBinding).playlistRecyclerView.layoutManager = LinearLayoutManager(context)
        (binding as DialogAddToPlaylistBinding).playlistRecyclerView.adapter=playlistAdapter

    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

}