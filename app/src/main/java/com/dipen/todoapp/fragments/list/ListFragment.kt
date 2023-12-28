package com.dipen.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dipen.todoapp.R
import com.dipen.todoapp.data.ToDoViewModel
import com.dipen.todoapp.data.models.ToDoData
import com.dipen.todoapp.databinding.FragmentListBinding
import com.dipen.todoapp.fragments.SharedViewModel
import com.dipen.todoapp.fragments.list.adapter.ListAdapter
import com.dipen.todoapp.utils.hideKeyboard
import com.dipen.todoapp.utils.observeOnce
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment(), View.OnClickListener, SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this

        hideKeyboard(requireActivity())

        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        swipeToDelete(binding.recycleView)

        clickListener()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search?.actionView as? SearchView
                searchView?.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete_all -> deleteAllData()
                    R.id.menu_priority_high -> toDoViewModel.searchByHighPriority.observeOnce(
                        viewLifecycleOwner
                    ) {
                        adapter.updateData(it)
                    }

                    R.id.menu_priority_low -> toDoViewModel.searchByLowPriority.observeOnce(
                        viewLifecycleOwner
                    ) {
                        adapter.updateData(it)
                    }

                    android.R.id.home -> requireActivity().onBackPressed()

                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun clickListener() {
        binding.listLayout.setOnClickListener(this)

        toDoViewModel.getAllData.observe(viewLifecycleOwner) {
            adapter.updateData(it)
            sharedViewModel.checkIfDBisEmpty(it)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.listLayout -> findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }
    }


    private fun deleteAllData() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete All data")
        builder.setMessage("Are you sure you want to remove all the data ?")
        builder.setPositiveButton("No") { _, _ ->
        }
        builder.setNegativeButton("Yes") { _, _ ->
            toDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                toDoViewModel.deleteData(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedItem(viewHolder.itemView, itemToDelete)
            }
        }
        val itemToucheHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemToucheHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedItem(view: View, deleteItem: ToDoData) {
        val snackBar = Snackbar.make(view, "Deleted ${deleteItem.title}", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            toDoViewModel.insertData(toDoData = deleteItem)
        }
        snackBar.show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) searchThroughDb(query)
        return true
    }

    private fun searchThroughDb(query: String) {
        toDoViewModel.searchDatabase("%$query%").observeOnce(viewLifecycleOwner) { list ->
            list.let {
                adapter.updateData(it)
            }
        }
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) searchThroughDb(query)
        return true
    }

}