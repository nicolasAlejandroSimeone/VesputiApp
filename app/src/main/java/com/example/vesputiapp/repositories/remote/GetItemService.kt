package com.example.vesputiapp.repositories.remote

import com.example.vesputiapp.models.Items
import com.example.vesputiapp.utils.Result

class GetItemService(private val api: ApiClient) {
    suspend fun getItems(param:String): Result<List<Items>> {
        val response = api.getItemsAsync(param).await()
        val body = response.body()
        body?.let {
            return Result.Success(body)
        } ?: run {
            val messageError = when(response.code()){
                401 -> "Unauthorize"
                404 -> "Not Found"
                502 -> "bad Gateway"
                else -> "Unexpected_error"
            }
            return Result.Error(
                Exception(messageError)
            )
        }
    }
}