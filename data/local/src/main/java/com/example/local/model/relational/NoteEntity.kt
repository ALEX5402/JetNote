package com.example.local.model.relational

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.local.model.*
import com.example.local.utils.Constants.ID
import com.example.local.utils.Constants.UUID

data class NoteEntity(
    @Embedded val dataEntity: DataEntity,
    @Relation(
        parentColumn = UUID,
        entityColumn = ID,
        entity = TagEntity::class,
        associateBy = Junction(
            NoteAndTagEntity::class,
            parentColumn = "noteUid",
            entityColumn = "labelId"
        )
    )
    val tagEntities :List<TagEntity>,
    @Relation(
        parentColumn = UUID,
        entityColumn = ID,
        entity = TaskEntity::class,
        associateBy = Junction(
            NoteAndTaskEntity::class,
            parentColumn = "noteUid",
            entityColumn = "todoId"
        )
    )
    val taskEntities:List<TaskEntity>,
    @Relation(
        parentColumn = UUID,
        entityColumn = ID,
        entity = LinkEntity::class,
        associateBy = Junction(
            NoteAndLinkEntity::class,
            parentColumn = "noteUid",
            entityColumn = "linkId"
        )
    )
    val linkEntities: List<LinkEntity>
)
