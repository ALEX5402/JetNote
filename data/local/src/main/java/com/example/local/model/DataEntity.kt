package com.example.local.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.example.local.utils.Constants.AUDIO_DURATION
import com.example.local.utils.Constants.AUDIO_URL
import com.example.local.utils.Constants.COLOR
import com.example.local.utils.Constants.DATE
import com.example.local.utils.Constants.DESCRIPTION
import com.example.local.utils.Constants.IMAGE_UIL
import com.example.local.utils.Constants.NON
import com.example.local.utils.Constants.NOTES_TABLE
import com.example.local.utils.Constants.PRIORITY
import com.example.local.utils.Constants.REMINDING
import com.example.local.utils.Constants.TEXT_COLOR
import com.example.local.utils.Constants.TITLE
import com.example.local.utils.Constants.TRASHED
import com.example.local.utils.Constants.UUID

@Entity(
    tableName = NOTES_TABLE
)
data class DataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = UUID) var uid: String = "",
    @ColumnInfo(name = TITLE) var title: String? = null,
    @ColumnInfo(name = DESCRIPTION) var description: String? = null,
    @ColumnInfo(name = PRIORITY) var priority: String = NON,
    @ColumnInfo(name = COLOR) var color: Int = 0,
    @ColumnInfo(name = TEXT_COLOR) var textColor: Int = 0x000000,
    @ColumnInfo(name = DATE) var date: String = "",
    @ColumnInfo(name = TRASHED) var trashed: Int = 0,
    @ColumnInfo(name = AUDIO_DURATION) var audioDuration: Int = 0,
    @ColumnInfo(name = REMINDING) var reminding: Long = 0L,
    @ColumnInfo(name = IMAGE_UIL) var imageUrl: String? = null,
    @ColumnInfo(name = AUDIO_URL) var audioUrl: String? = null,
)
