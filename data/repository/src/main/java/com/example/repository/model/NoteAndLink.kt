package com.example.repository.model

import androidx.annotation.Keep

@Keep
data class NoteAndLink(
    val noteUid: String,
    val linkId: String
)
