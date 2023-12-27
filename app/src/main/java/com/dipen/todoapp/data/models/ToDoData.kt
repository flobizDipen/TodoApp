package com.dipen.todoapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dipen.todoapp.data.models.Priority

@Entity(tableName = "todo_table")
data class ToDoData(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    val priority: Priority,
    val description: String
)
