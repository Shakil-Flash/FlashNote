package com.flash.flashnote

import android.graphics.Color

data class Note(
    val title: String,
    val content: String,
    val date: String,
    val color: Int = Color.WHITE
)
