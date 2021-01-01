package com.fastnews.viewmodel

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastnews.repository.CommentRepository
import com.fastnews.service.model.CommentData
import kotlinx.coroutines.launch

class CommentViewModel(private val repository : CommentRepository) : ViewModel() {

    private lateinit var comments: MutableLiveData<List<CommentData>>

    @UiThread
    fun getComments(postId: String): LiveData<List<CommentData>> {
        if(!::comments.isInitialized) {
            comments = MutableLiveData()

            viewModelScope.launch {
                val result =  repository.getComments(postId)

                comments.postValue(result)
            }
        }
        return comments
    }

    fun saveCommentsOnCache(comments: List<CommentData>, postId: String) {
        viewModelScope.launch {
            repository.setCommentsOnCache(comments, postId)
        }
    }

    fun getCommentsFromCache(postId: String): LiveData<List<CommentData>> {
        comments = MutableLiveData()
        viewModelScope.launch {
            comments.postValue(repository.getCommentsFromCache(postId) as List<CommentData>?)
        }
        return comments
    }

}