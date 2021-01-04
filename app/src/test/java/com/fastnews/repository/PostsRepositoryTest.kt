package com.fastnews.repository

import com.fastnews.service.model.PostChildren
import com.fastnews.service.model.PostData
import com.fastnews.viewmodel.PostViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.net.HttpURLConnection
import java.util.*

class PostsRepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    lateinit var postRepository: PostRepository


    @Mock
    lateinit var deferred: Deferred<List<PostData>>
    val listData = List(1) { data }
    val data = PostData("12345", "author", "thumb", "PostTest", 0, 1, "title", Date().time, "www.google.com", null)
    val post = PostChildren(data)
    val result = post.data


    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `Fetching Posts Success`(): Unit = runBlocking {
        `when`(postRepository.getPosts("", 1)).thenReturn(listData)
        `when`(deferred.await()).thenReturn(listData)
        val postViewModel = PostViewModel(postRepository)
        delay(10000L)
        Assert.assertNotEquals(result, postViewModel.getPosts(""))
    }

    @Test
    fun `WHEN  api delivers a valid content THEN  post respository retrieves expected data`() = runBlocking {
        val postViewModel = PostViewModel(postRepository)
        val dataReceived = postViewModel.getPosts("")
        Assert.assertNotNull(dataReceived)
    }
}