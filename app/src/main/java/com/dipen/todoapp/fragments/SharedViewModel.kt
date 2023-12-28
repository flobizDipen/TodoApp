package com.dipen.todoapp.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dipen.todoapp.R
import com.dipen.todoapp.data.models.Priority
import com.dipen.todoapp.data.models.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    var emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDBisEmpty(todoDataList: List<ToDoData>) {
        emptyDatabase.value = todoDataList.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> (parent?.getChildAt(0) as TextView).setColor(R.color.red)
                1 -> (parent?.getChildAt(0) as TextView).setColor(R.color.yellow)
                else -> (parent?.getChildAt(0) as TextView).setColor(R.color.green)
            }
        }
    }

    fun verifyData(title: String, description: String) =
        title.isNotEmpty() && description.isNotEmpty()

    fun parsePriority(priorityStr: String) =
        Priority.entries.first { it.name == priorityStr }

    private fun TextView.setColor(colorId: Int) {
        this.setTextColor(ContextCompat.getColor(this.context, colorId))
    }
}