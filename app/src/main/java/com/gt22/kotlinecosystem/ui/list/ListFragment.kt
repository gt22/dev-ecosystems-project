package com.gt22.kotlinecosystem.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gt22.kotlinecosystem.databinding.ListFragmentBinding
import com.gt22.kotlinecosystem.view.TaskAdapter
import com.gt22.kotlinecosystem.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    val tasks : TaskViewModel by viewModels()

    private var _binding : ListFragmentBinding? = null

    private val binding : ListFragmentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        val adapter = TaskAdapter()
        binding.swipeRefreshLayout.setOnRefreshListener {
            tasks.fetchTasks()
            binding.swipeRefreshLayout.isRefreshing = true
        }
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeMovementFlags(0, ItemTouchHelper.START)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                println("Swiped ${viewHolder.bindingAdapterPosition}, direction $direction")

                lifecycleScope.launch {
                    tasks.deleteTask(viewHolder.bindingAdapterPosition)
                }
            }
        })
        touchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        tasks.tasks.observe(viewLifecycleOwner) {
            adapter.setTasks(it)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToTaskEditFragment(null)
            view.findNavController().navigate(action)
        }

        tasks.fetchTasks()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}