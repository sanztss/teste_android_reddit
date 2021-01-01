package com.fastnews.repository

import com.fastnews.data.CommentDao
import com.fastnews.service.api.RedditAPI
import com.fastnews.service.model.CommentData

class CommentRepositoryImpl(
    private val dao: CommentDao
) :
    BaseRepositoryImpl(),
    CommentRepository {

    override suspend fun getComments(postId: String): List<CommentData> {

        val commentsResponse = safeApiCall(
            call = { RedditAPI.redditService.getCommentList(postId).await() },
            errorMessage = "Error to fetch comments from postId -> $postId"
        )

        val result = mutableListOf<CommentData>()
        commentsResponse?.map { response ->
            response.data.children.map { data ->
                if (data.kind == "t1") {
                    result.add(data.data)
                }
            }
        }

        return result

    }

}