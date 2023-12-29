package com.dipen.todoapp.data

import androidx.room.TypeConverter
import com.dipen.todoapp.data.models.Priority

class Converter {
    @TypeConverter
    fun fromPriority(priority: Priority) = priority.name

    @TypeConverter
    fun toPriority(priorityStr: String) = Priority.valueOf(priorityStr)
}