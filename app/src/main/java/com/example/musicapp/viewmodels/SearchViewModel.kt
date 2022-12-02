package com.example.musicapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.MyApplication
import com.example.musicapp.Repository
import com.example.musicapp.model.Playlist
import com.example.musicapp.model.Song
import kotlinx.coroutines.launch

class SearchViewModel() :ViewModel() {
//   private var repository :Repository = Repository(( as MyApplication).database)
//
//
//    fun insertSong(song: Song){
//        viewModelScope.launch {
//            repository.insertSong(song)
//        }
//    }
//
//    fun insertPlaylist(playlist: Playlist){
//        viewModelScope.launch {
//            repository.insertPlaylist(playlist)
//        }
//    }
//
//    fun getPlaylistWithSongs(playlistName : String){
//        viewModelScope.launch {
//            repository.getPlaylistWithSongs(playlistName)
//        }
//    }
}