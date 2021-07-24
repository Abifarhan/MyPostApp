package com.abifarhan.mypostapp.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Thought constructor(
    val username: String,
    val timestamp: Date,
    val thoughtTxt: String,
    val numLikes: Int,
    val numComments: Int,
    val documentId: String
) : Parcelable