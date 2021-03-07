package com.example.vesputiapp.repositories.remote

import com.example.vesputiapp.models.Items
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("features.json?app_mode=swh-mein-halle-mobi")
    fun getItemsAsync(@Query("type") param:String): Deferred<Response<List<Items>>>
}