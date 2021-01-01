package com.fastnews.repository

import com.fastnews.service.model.PostData

interface PostRepository {
    suspend fun getPosts(after: String, limit: Int): List<PostData>
    suspend fun setPostsOnCache(posts: MutableList<PostData>)
    suspend fun getPostsFromCache(): List<PostData>
}