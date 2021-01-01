package com.fastnews.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CommentResponse(val data: CommentDataChild)

data class CommentDataChild(val children: List<CommentChildren>)

data class CommentChildren(val kind: String, val data: CommentData)

@Entity(tableName = "comment_table")
data class CommentData(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val author: String,
    val body: String,
    val name: String,
    val downs: Int,
    val ups: Int,
    val created_utc: Long,
    val post_id: String?)