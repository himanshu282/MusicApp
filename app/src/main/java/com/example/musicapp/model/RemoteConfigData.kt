package com.example.musicapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RemoteConfigData(
    val data: List<Songs>
)


@Parcelize
data class Songs(
    val songUrl: String,
    val songName: String,
    val singerName: String
) : Parcelable
