package com.gt22.kotlinecosystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.gt22.kotlinecosystem.model.Model

import com.gt22.kotlinecosystem.model.TaskRef
import kotlinx.coroutines.Dispatchers


class TaskViewModel : ViewModel() {



    private val _taskQuery: MutableLiveData<String> = MutableLiveData()

    private val _tasks: LiveData<List<TaskRef>> = _taskQuery.switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Model.fetchTasks())
        }
    }

    val tasks: LiveData<List<TaskRef>>
        get() = _tasks

    fun fetchTasks() {
        _taskQuery.postValue("fetchTasks")
    }

    suspend fun deleteTask(id: Int) {
        Model.deleteTask(tasks.value!![id].id)
        fetchTasks()
    }

}