package com.fastnews.data

import androidx.room.*
import com.fastnews.service.model.PostData

@Dao
interface PostDao {

    @Query("SELECT * FROM post_table ORDER BY created_utc DESC")
    fun getPostsSortedByDateCreated(): List<PostData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(postData: PostData)

    @Update
    suspend fun update(postData: PostData)

    @Query("DELETE FROM post_table")
    suspend fun deleteAll()
}