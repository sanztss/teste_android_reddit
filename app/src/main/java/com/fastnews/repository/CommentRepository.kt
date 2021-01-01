package com.fastnews.repository

import com.fastnews.service.model.CommentData

interface CommentRepository {
    suspend fun getComments(postId: String): List<CommentData>
    suspend fun setCommentsOnCache(comments: List<CommentData>, postId: String)
    suspend fun getCommentsFromCache(postId: String): List<CommentData>
}