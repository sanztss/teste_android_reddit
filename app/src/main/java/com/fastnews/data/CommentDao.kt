package com.fastnews.data

import androidx.room.*
import com.fastnews.service.model.CommentData

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment_table WHERE post_id = :postId ORDER BY created_utc DESC")
    fun getCommentsSortedByDateCreated(postId: String): List<CommentData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(commentData: CommentData)

    @Update
    suspend fun update(commentData: CommentData)

    @Query("DELETE FROM comment_table")
    suspend fun deleteAll()
}