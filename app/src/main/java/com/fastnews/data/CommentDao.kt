package com.fastnews.data

import androidx.room.*
import com.fastnews.service.model.CommentData

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment_table ORDER BY created_utc DESC")
    fun getCommentsSortedByDateCreated(): List<CommentData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(commentData: CommentData)

    @Update
    suspend fun update(commentData: CommentData)

    @Query("DELETE FROM comment_table")
    suspend fun deleteAll()
}