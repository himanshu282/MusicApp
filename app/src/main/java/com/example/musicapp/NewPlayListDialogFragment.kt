package com.example.musicapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.musicapp.databinding.AddToPlaylistBinding
import com.example.musicapp.model.Song
import com.example.musicapp.viewmodels.PlaylistSharedViewModel

class NewPlayListDialogFragment(private var click : () -> Unit) : DialogFragment() {

    private val model : PlaylistSharedViewModel by activityViewModels()
    private lateinit var binding: ViewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.add_to_playlist,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (binding as AddToPlaylistBinding).btnOk.setOnClickListener {
            val text = (binding as AddToPlaylistBinding).playlistName.text.toString()
            if (text.isNotEmpty()){
                model.insertPlaylist(com.example.musicapp.model.Playlist(playlistName = text))
                model.song.observe(viewLifecycleOwner){
                    model.insertSong(Song(playlistName = text, songName = it.songName, singerName = it.singerName, songUrl = it.songUrl))
                }

                dialog?.dismiss()
                click.invoke()
            }
            else
                Toast.makeText(view.context,"Playlist Name should not be empty.",Toast.LENGTH_SHORT).show()


        }

        (binding as AddToPlaylistBinding).btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
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