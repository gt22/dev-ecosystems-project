package com.gt22.kotlinecosystem.ui.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.gt22.kotlinecosystem.R
import com.gt22.kotlinecosystem.databinding.TaskEditFragmentBinding
import com.gt22.kotlinecosystem.model.Language
import com.gt22.kotlinecosystem.model.Task
import com.gt22.kotlinecosystem.model.TaskRef
import com.gt22.kotlinecosystem.viewmodel.EditViewModel

class TaskEditFragment : Fragment() {

    val tasks: EditViewModel by viewModels()

    private var _binding: TaskEditFragmentBinding? = null

    private val binding: TaskEditFragmentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TaskEditFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_dropdown_item,
            Language.values()
        )


        val args: TaskEditFragmentArgs by navArgs()
        val task = args.task
        binding.taskDescription.setText(task?.task?.description ?: "")
        binding.taskLanguageSpinner.setText(task?.task?.language?.name ?: "")
        binding.taskUpdateButton.text = if (task == null) "Create" else "Update"
        binding.taskUpdateButton.backgroundTintList =
            if (task == null) ContextCompat.getColorStateList(
                view.context,
                R.color.green
            ) else ContextCompat.getColorStateList(view.context, R.color.blue)
        binding.taskUpdateButton.setOnClickListener {
            val description = binding.taskDescription.text.toString()
            val language = runCatching {
                Language.valueOf(binding.taskLanguageSpinner.text.toString().uppercase())
            }.getOrNull()
            if (language == null) {
                Toast.makeText(view.context, "Select language", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val newTask = Task(description, language)
            if (task == null) {
                tasks.createTask(newTask)
            } else {
                tasks.updateTask(task.copy(task = newTask))
            }
        }
        tasks.results.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                Toast.makeText(requireActivity(), it.getOrNull(), Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack()
            } else {
                Toast.makeText(
                    view.context,
                    "Error: ${it.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.taskLanguageSpinner.setAdapter(adapter)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}