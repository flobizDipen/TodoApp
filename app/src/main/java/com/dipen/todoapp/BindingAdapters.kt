package com.dipen.todoapp

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.dipen.todoapp.data.models.Priority
import com.dipen.todoapp.data.models.ToDoData
import com.dipen.todoapp.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapters {

    companion object {

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate)
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            view.isVisible = emptyDatabase.value ?: false
        }

        @BindingAdapter("android:parsePriority")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) =
            view.setSelection(Priority.entries.indexOf(priority))

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(view: CardView, color: Int) {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, color))
        }

        @BindingAdapter("sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentItem: ToDoData) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }


    }
}