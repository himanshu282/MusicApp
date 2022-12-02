package com.example.musicapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.MyApplication
import com.example.musicapp.Repository
import com.example.musicapp.model.relations.PlaylistWithSongs
import kotlinx.coroutines.launch

class PlaylistSongViewModel(application: Application) : AndroidViewModel(application) {
    private var repository : Repository = Repository((application as MyApplication).database)
    var data = MutableLiveData<PlaylistWithSongs?>()



     fun getPlaylistWithSongs(playlistName: String) {
        viewModelScope.launch() {
          data.value = repository.getPlaylistWithSongs(playlistName)
            Log.d("TAG", "getPlaylistWithSongs: ${repository.getPlaylistWithSongs(playlistName)}")
        }

    }
}