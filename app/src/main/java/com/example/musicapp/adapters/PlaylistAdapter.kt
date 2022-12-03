package com.example.musicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Playlist

class PlaylistAdapter(var click : (String) -> Unit) : ListAdapter<Playlist,PlaylistAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<Playlist>(){
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean = false

}) {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val playlistTitle: TextView = view.findViewById(R.id.playlist_title)
        fun bind(text : Playlist){
            playlistTitle.setOnClickListener {
                click.invoke(text.playlistName)
            }
            playlistTitle.text = text.playlistName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}


