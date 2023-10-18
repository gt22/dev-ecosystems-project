package com.gt22.kotlinecosystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gt22.kotlinecosystem.databinding.ActivityMainBinding
import com.gt22.kotlinecosystem.view.TaskAdapter
import com.gt22.kotlinecosystem.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private var _binding : ActivityMainBinding? = null

    private val binding : ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}