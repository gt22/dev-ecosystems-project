package com.gt22.kotlinecosystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gt22.kotlinecosystem.model.Language
import com.gt22.kotlinecosystem.model.Model
import com.gt22.kotlinecosystem.model.Task
import com.gt22.kotlinecosystem.model.TaskRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel : ViewModel() {

    private val _results: MutableLiveData<Result<String>> = MutableLiveData()

    val results: LiveData<Result<String>>
        get() = _results

    fun createTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val id = Model.createTask(task)
                _results.postValue(Result.success("Task created with id $id"))
            } catch (e: Exception) {
                _results.postValue(Result.failure(e))
            }
        }
    }

    fun updateTask(task: TaskRef) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Model.updateTask(task)
                _results.postValue(Result.success("Task updated"))
            } catch (e: Exception) {
                _results.postValue(Result.failure(e))
            }
        }
    }

}