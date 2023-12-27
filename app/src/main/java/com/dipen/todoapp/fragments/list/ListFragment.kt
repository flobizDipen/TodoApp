package com.dipen.todoapp.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dipen.todoapp.R
import com.dipen.todoapp.databinding.FragmentListBinding


class ListFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        clickListener()
        return _binding?.root
    }

    private fun clickListener() {
        binding.floatingActionButton.setOnClickListener(this)
        binding.listLayout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.floatingActionButton -> findNavController().navigate(R.id.action_listFragment_to_addFragment)
            binding.listLayout -> findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

}