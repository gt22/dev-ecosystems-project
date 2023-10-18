package com.gt22.kotlinecosystem.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gt22.kotlinecosystem.databinding.TaskItemViewBinding
import com.gt22.kotlinecosystem.model.TaskRef
import com.gt22.kotlinecosystem.ui.list.ListFragmentDirections

class TaskAdapter : RecyclerView.Adapter<TaskViewHolder>() {

    private val tasks: MutableList<TaskRef> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(tasks: List<TaskRef>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }


}

class TaskViewHolder(private val binding: TaskItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(task: TaskRef) {
        binding.taskDescription.text = task.task.description
        binding.taskLanguage.text = task.task.language.name.lowercase().replaceFirstChar { it.uppercase() }
        binding.root.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToTaskEditFragment(task)
            itemView.findNavController().navigate(action)
        }
    }



}