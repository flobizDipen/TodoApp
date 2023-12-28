package com.dipen.todoapp.data.models

import com.dipen.todoapp.R

enum class Priority(val color: Int) {
    HIGH(R.color.red),
    MEDIUM(R.color.yellow),
    LOW(R.color.green)
}