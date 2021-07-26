package com.abifarhan.mypostapp.model

import java.util.*

data class Comment constructor(
    val username: String,
    val timestamp: Date,
    val commentTxt: String,
    val documentId: String,
    val userId: String
)
