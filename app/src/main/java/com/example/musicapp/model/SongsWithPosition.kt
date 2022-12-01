package com.example.musicapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongsWithPosition(val list: ArrayList<Songs>, val index: Int) : Parcelable
