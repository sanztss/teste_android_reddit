package com.fastnews.repository

import com.fastnews.service.model.CommentData

interface CommentRepository {
    suspend fun getComments(postId: String): List<CommentData>
}