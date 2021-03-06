package com.example.androidacademyhomework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidacademyhomework.data.model.viewholder.CastItem
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.LANGUAGE
import com.example.androidacademyhomework.network.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorListViewModel:ViewModel() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val _mutableActorList = MutableLiveData<List<CastItem?>?>(emptyList())
    val actorList: LiveData<List<CastItem?>?> get() = _mutableActorList
    fun loadActorList(movieId:Int)
    {
        scope.launch { val updatedActorList: List<CastItem?>? = _mutableActorList.value?.plus(
            RetrofitModule.moviesApi.getCast(
                movieId,
                API_KEY, LANGUAGE
            ).cast as List<CastItem?>
        )
            _mutableActorList.value = updatedActorList }
    }

}