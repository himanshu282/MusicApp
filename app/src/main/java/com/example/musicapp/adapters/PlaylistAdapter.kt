package com.example.musicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R

class PlaylistAdapter(var click : (String) -> Unit) : ListAdapter<String,PlaylistAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = false

}) {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val playlistTitle: TextView = view.findViewById(R.id.playlist_title)
        fun bind(text : String){
            playlistTitle.setOnClickListener {
                click.invoke(text)
            }
            playlistTitle.text = text
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


