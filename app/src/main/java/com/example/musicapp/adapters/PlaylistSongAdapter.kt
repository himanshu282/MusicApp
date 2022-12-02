package com.example.musicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Song

class PlaylistSongAdapter(var click : (Int) -> Unit) : ListAdapter<Song, PlaylistSongAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<Song>(){
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean = false

}) {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val songTitle: TextView = view.findViewById(R.id.song_name)
        private val singerName: TextView = view.findViewById(R.id.singer_name)
        fun bind(song: Song){
            songTitle.text= song.songName
            singerName.text = song.singerName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            click.invoke(position)
        }
    }

}