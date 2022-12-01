package com.example.musicapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.musicapp.adapters.PlaylistAdapter
import com.example.musicapp.databinding.DialogAddToPlaylistBinding
import com.example.musicapp.model.Songs
import com.google.gson.Gson

class PlaylistDialogFragment : DialogFragment() {

    private lateinit var binding: ViewBinding

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
        val playlistAdapter = PlaylistAdapter()
        (binding as DialogAddToPlaylistBinding).createPlaylist.setOnClickListener {
            NewPlayListDialogFragment {
                playlistAdapter.submitList(it?.name?.toList() ?: arrayListOf())
            }.show(this.parentFragmentManager,this.activity?.localClassName)
        }

        val songData = arguments?.getParcelable<Songs>("song")

        val playlist : Playlist? = Gson().fromJson(AppPreference.listOfPlaylist,Playlist::class.java)
        playlistAdapter.submitList(playlist?.name?.toList() ?: arrayListOf())
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