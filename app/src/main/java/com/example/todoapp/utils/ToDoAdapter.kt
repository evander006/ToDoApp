package com.example.todoapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.EachToDoBinding

class ToDoAdapter(val list:MutableList<ToDo>):
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    private var listener:todoAdapter?=null
    fun setListener(listener: todoAdapter){
        this.listener=listener
    }
    inner class ToDoViewHolder(val binding: EachToDoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding=EachToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text=this.task

                binding.delete.setOnClickListener {
                    listener?.onDelete(this)
                }
                binding.edit.setOnClickListener {
                    listener?.onEdit(this)
                }
            }
        }
    }
    interface todoAdapter{
        fun onDelete(todoData: ToDo)
        fun onEdit(todoData: ToDo)
    }
}