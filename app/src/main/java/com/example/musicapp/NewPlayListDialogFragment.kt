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
import androidx.viewbinding.ViewBinding
import com.example.musicapp.databinding.AddToPlaylistBinding
import com.example.musicapp.viewmodels.PlaylistSharedViewModel
import com.google.gson.Gson

class NewPlayListDialogFragment(var click : (Playlist?) -> Unit) : DialogFragment() {

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
//            AppPreference.listOfPlaylist = text
            var playlist : Playlist? = Gson().fromJson(AppPreference.listOfPlaylist,Playlist::class.java)
            playlist?.let {
                it.name.add(text)
            } ?: kotlin.run {
                val listOfNames = arrayListOf<String>()
               listOfNames.add(text)
                playlist = Playlist(listOfNames)
            }
            AppPreference.listOfPlaylist = Gson().toJson(playlist)
            dialog?.dismiss()
            click.invoke(playlist)
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