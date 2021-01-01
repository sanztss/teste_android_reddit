package com.fastnews.repository

import com.fastnews.data.CommentDao
import com.fastnews.service.api.RedditAPI
import com.fastnews.service.model.CommentData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun setCommentsOnCache(comments: List<CommentData>, postId: String) {
        return withContext(Dispatchers.IO) {
            comments.forEach {
                val commentData = CommentData(
                    id = it.id,
                    author = it.author,
                    body = it.body,
                    name = it.name,
                    downs = it.downs,
                    ups = it.ups,
                    created_utc = it.created_utc,
                    post_id = postId
                )
                dao.insert(commentData)
            }
        }
    }

    override suspend fun getCommentsFromCache(postId: String): List<CommentData> {
        return withContext(Dispatchers.IO) {
            dao.getCommentsSortedByDateCreated(postId)
        }
    }

}