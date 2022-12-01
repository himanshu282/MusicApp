package com.example.musicapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.RemoteConfigData
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val responseData = MutableLiveData<RemoteConfigData>()
    init {
        getRemoteConfigData()
    }


    private fun getRemoteConfigData()  {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        viewModelScope.launch(Dispatchers.IO) {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val response = remoteConfig.getString("responseJson")
                        Log.d("TAG", "setupFirebase: $response")
                        responseData.value = Gson().fromJson(response, RemoteConfigData::class.java)

                        Log.d("TAG", "list of data: $responseData")
                    } else {
                        Log.d("remote config error", "Fetch Failed")
                    }
                }
        }
    }
}