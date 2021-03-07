package com.example.vesputiapp.repositories

import com.example.vesputiapp.models.Items
import com.example.vesputiapp.repositories.remote.GetItemService
import com.example.vesputiapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetItemRepository(private val remoteDataSource:GetItemService) {
    suspend fun getItems(param:String): List<Items> = withContext(Dispatchers.IO)
    {
        when (val result = remoteDataSource.getItems(param)){
            is Result.Success -> result.data
            is Result.Error -> throw result.exception
        }
    }
}