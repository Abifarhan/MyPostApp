package com.abifarhan.mypostapp

import com.google.firebase.Timestamp
import java.util.*

data class Thought constructor(
    val username: String,
    val timestamp: Date,
    val thoughtTxt: String,
    val numLikes: Int,
    val numComments: Int,
    val documentId: String
)