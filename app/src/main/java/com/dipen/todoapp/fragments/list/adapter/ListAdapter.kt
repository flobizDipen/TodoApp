package com.dipen.todoapp.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dipen.todoapp.data.models.ToDoData
import com.dipen.todoapp.databinding.ItemTodoBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList: List<ToDoData> = listOf()

    class MyViewHolder(private val itemViewBinding: ItemTodoBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(toDoData: ToDoData) {

            itemViewBinding.todoData = toDoData
            itemViewBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun updateData(toDoData: List<ToDoData>) {
        val todoDiffUtil = TodoDiffUtils(dataList, toDoData)
        val todoDiffResult = DiffUtil.calculateDiff(todoDiffUtil)
        this.dataList = toDoData
        todoDiffResult.dispatchUpdatesTo(this)

    }
}