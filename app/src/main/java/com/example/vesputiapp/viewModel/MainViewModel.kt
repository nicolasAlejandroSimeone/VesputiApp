package com.example.vesputiapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vesputiapp.models.Items
import com.example.vesputiapp.repositories.GetItemRepository
import kotlinx.coroutines.launch

class MainViewModel(private val getItemRepository: GetItemRepository) : ViewModel() {

    private val _results = MutableLiveData<List<Items>>()
    val results: LiveData<List<Items>>
        get() = _results


    fun getItems(param:String){
        viewModelScope.launch {
            _results.value = getItemRepository.getItems(param)
        }
    }
}