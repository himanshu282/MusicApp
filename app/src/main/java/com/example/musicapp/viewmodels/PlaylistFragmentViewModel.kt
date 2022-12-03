package com.example.musicapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.MyApplication
import com.example.musicapp.Repository
import com.example.musicapp.model.Playlist
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel (application: Application) : AndroidViewModel(application) {
    private var repository : Repository = Repository((application as MyApplication).database)
    var playlist = MutableLiveData<List<Playlist>?>()





     fun getPlaylist(){
        viewModelScope.launch {
            playlist.postValue(repository.getPlaylist())
        }
    }

}