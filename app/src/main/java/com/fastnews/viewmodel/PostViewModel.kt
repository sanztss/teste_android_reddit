package com.fastnews.viewmodel

import androidx.lifecycle.*
import com.fastnews.repository.PostRepository
import com.fastnews.service.model.PostData
import kotlinx.coroutines.launch

class PostViewModel(private val repository : PostRepository) : ViewModel() {

    lateinit var posts: MutableLiveData<MutableList<PostData>>
    var items: MutableList<PostData> = mutableListOf()

    fun getPosts(after: String): LiveData<MutableList<PostData>> {
        posts = MutableLiveData()

        viewModelScope.launch {
            val result =  repository.getPosts(after, 10)

            posts.postValue(result as MutableList<PostData>?)
        }
        return posts
    }

    fun setRvItems(rvItems: MutableList<PostData>) {
        items = rvItems
    }

    fun savePostsOnCache(posts: MutableList<PostData>) {
        viewModelScope.launch {
            repository.setPostsOnCache(posts)
        }
    }

    fun getPostsFromCache(): LiveData<MutableList<PostData>> {
        posts = MutableLiveData()
        viewModelScope.launch {
            posts.postValue(repository.getPostsFromCache() as MutableList<PostData>?)
        }
        return posts
    }

}