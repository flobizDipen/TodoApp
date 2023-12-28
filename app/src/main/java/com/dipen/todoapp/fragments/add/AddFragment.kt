package com.dipen.todoapp.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.dipen.todoapp.R
import com.dipen.todoapp.data.ToDoViewModel
import com.dipen.todoapp.data.models.ToDoData
import com.dipen.todoapp.databinding.FragmentAddBinding
import com.dipen.todoapp.fragments.SharedViewModel

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.spinner.onItemSelectedListener = sharedViewModel.listener
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_add -> insertDataToDB()
                    android.R.id.home -> requireActivity().onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun insertDataToDB() {
        val title = binding.edtTitle.text.trim().toString()
        val priority =
            sharedViewModel.parsePriority(binding.spinner.selectedItem.toString().uppercase())
        val description = binding.edtDescription.text.trim().toString()

        if (sharedViewModel.verifyData(title, description)) {
            val todoData = ToDoData(0, title, priority, description)
            toDoViewModel.insertData(todoData)
            Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else
            Toast.makeText(
                requireContext(),
                "Title or description can't be empty",
                Toast.LENGTH_SHORT
            ).show()
    }


}