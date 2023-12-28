package com.dipen.todoapp.fragments.update

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.dipen.todoapp.R
import com.dipen.todoapp.data.ToDoViewModel
import com.dipen.todoapp.data.models.ToDoData
import com.dipen.todoapp.databinding.FragmentUpdateBinding
import com.dipen.todoapp.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args
        binding.spinner.onItemSelectedListener = sharedViewModel.listener

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_save -> insertDataToDB()
                    R.id.menu_delete -> deleteData()
                    android.R.id.home -> requireActivity().onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun deleteData() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete ${args.currentItem.title}")
        builder.setMessage("Are you sure you want to remove ${args.currentItem.title} ?")
        builder.setPositiveButton("No") { _, _ ->
        }
        builder.setNegativeButton("Yes") { _, _ ->
            toDoViewModel.deleteData(args.currentItem)
            Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.create().show()
    }

    private fun insertDataToDB() {
        val title = binding.edtTitle.text.trim().toString()
        val priority =
            sharedViewModel.parsePriority(binding.spinner.selectedItem.toString().uppercase())
        val description = binding.edtDescription.text.trim().toString()

        if (sharedViewModel.verifyData(title, description)) {
            val todoData = ToDoData(args.currentItem.id, title, priority, description)
            toDoViewModel.updateData(todoData)
            Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else
            Toast.makeText(
                requireContext(),
                "Title or description can't be empty",
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}