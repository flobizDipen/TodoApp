package com.dipen.todoapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dipen.todoapp.data.models.ToDoData
import com.dipen.todoapp.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private var toDoRepository: ToDoRepository = ToDoRepository(toDoDao)

    var getAllData: LiveData<List<ToDoData>> = toDoRepository.getAllData
    var searchByHighPriority: LiveData<List<ToDoData>> = toDoRepository.searchByHighPriority
    var searchByLowPriority: LiveData<List<ToDoData>> = toDoRepository.searchByLowPriority

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.updateData(toDoData)
        }
    }

    fun deleteData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteData(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            toDoRepository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return toDoRepository.searchDatabase(searchQuery)
    }

}