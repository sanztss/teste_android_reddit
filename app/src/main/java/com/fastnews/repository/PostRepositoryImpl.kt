package com.fastnews.repository

import com.fastnews.data.PostDao
import com.fastnews.service.api.RedditAPI
import com.fastnews.service.model.PostData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepositoryImpl(
    private val dao: PostDao
) :
    BaseRepository(),
    PostRepository {

    override suspend fun getPosts(after: String, limit: Int): List<PostData> {
        val postResponse = safeApiCall(
            call = { RedditAPI.redditService.getPostList(after, limit).await() },
            errorMessage = "Error to fetching posts"
        )
        val result: MutableList<PostData> = mutableListOf()
        postResponse?.data?.children?.map { postChildren -> result.add(postChildren.data) }
        return result
    }

    override suspend fun setPostsOnCache(posts: MutableList<PostData>) {
        return withContext(Dispatchers.IO) {
            posts.forEach {
                val postData = PostData(
                    id = it.id,
                    author = it.author,
                    thumbnail = it.thumbnail,
                    name = it.name,
                    num_comments = it.num_comments,
                    score = it.score,
                    title = it.title,
                    created_utc = it.created_utc,
                    url = it.url,
                    preview = it.preview
                )
                dao.insert(postData)
            }
        }
    }

    override suspend fun getPostsFromCache(): List<PostData> {
        return withContext(Dispatchers.IO) {
            dao.getPostsSortedByDateCreated()
        }
    }
}