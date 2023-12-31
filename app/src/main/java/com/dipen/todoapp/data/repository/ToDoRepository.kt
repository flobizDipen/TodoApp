package com.dipen.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.dipen.todoapp.data.ToDoDao
import com.dipen.todoapp.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()
    val searchByHighPriority = toDoDao.searchByHighPriority()
    val searchByLowPriority = toDoDao.searchByLowPriority()

    suspend fun insertData(toDoData: ToDoData) = toDoDao.insertData(toDoData)

    suspend fun updateData(toDoData: ToDoData) = toDoDao.updateData(toDoData)

    suspend fun deleteData(toDoData: ToDoData) = toDoDao.deleteData(toDoData)

    suspend fun deleteAll() = toDoDao.deleteAll()

    fun searchDatabase(searchQuery: String) = toDoDao.searchDatabase(searchQuery)

}