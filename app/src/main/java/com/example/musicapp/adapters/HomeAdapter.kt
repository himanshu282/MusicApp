package com.example.musicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Songs

class HomeAdapter(var onClick : (Int) -> Unit): ListAdapter<Songs,HomeAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<Songs>(){
    override fun areItemsTheSame(oldItem: Songs, newItem: Songs): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Songs, newItem: Songs): Boolean = false

}){
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val songTitle: TextView = view.findViewById(R.id.song_name)
        private val singerName: TextView = view.findViewById(R.id.singer_name)
        fun bind(song: Songs){
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
            onClick.invoke(position)
        }
    }
}
