package com.example.rickandmortyapp.ui


import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.adapter.ListOfCharactersAdapter
import com.example.rickandmortyapp.db.ResultDatabase
import com.example.rickandmortyapp.repository.RickAndMortyRepository
import com.example.rickandmortyapp.retrofitservice.RetrofitService
import com.example.rickandmortyapp.viewmodel.RickAndMortyViewModel
import com.example.rickandmortyapp.viewmodelFactory.RickAndMortyViewmodelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_persons.*


class SavedPersonsFragment : Fragment(R.layout.fragment_saved_persons) {

    lateinit var rickAndMortyViewModel: RickAndMortyViewModel
    lateinit var listOfCharactersAdapter: ListOfCharactersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        listOfCharactersAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putSerializable("resultWithoutSaveButton", it)
            findNavController().navigate(R.id.action_savedPersonsFragment_to_articlePersonWithoutSaveButtonFragment, bundle)
        }

        val rickAndMortyRepository = RickAndMortyRepository(RetrofitService.getResponse(), ResultDatabase.getInstance(requireContext()))
        val rickAndMortyViewmodelFactory = RickAndMortyViewmodelFactory(rickAndMortyRepository)
        rickAndMortyViewModel = ViewModelProvider(this, rickAndMortyViewmodelFactory).get(RickAndMortyViewModel::class.java)

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,     // We indicate how we will scroll our list
            ItemTouchHelper.LEFT  // We indicate how we will swipe our list
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true  // performs actions when the user scrolls the list(we do not need to use this in this project)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val result = listOfCharactersAdapter.differ.currentList[position]
                rickAndMortyViewModel.deleteCharacter(result)
                Snackbar.make(view, "You have successfully deleted ${result.name} from the Saved Persons tab", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        rickAndMortyViewModel.addCharacter(result)
                    }
                    show()
                }
            }
        }

        val deleteAllCharactersButton = view.findViewById<Button>(R.id.deleteAllCharactersButton)
        deleteAllCharactersButton.setOnClickListener {
            deleteAllCharactersFromRecyclerView()
        }

        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(rcViewSavedCharacters)

        rickAndMortyViewModel.getAllCharactersRoom().observe(viewLifecycleOwner, Observer { result ->
            listOfCharactersAdapter.differ.submitList(result)
        })

    }

    private fun setupRecyclerView() {
        val rcViewSavedCharacters = view?.findViewById<RecyclerView>(R.id.rcViewSavedCharacters)
        listOfCharactersAdapter = ListOfCharactersAdapter()
        rcViewSavedCharacters?.adapter = listOfCharactersAdapter
        rcViewSavedCharacters?.layoutManager = LinearLayoutManager(activity)
    }

    private fun deleteAllCharactersFromRecyclerView() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setPositiveButton("Yes") {_, _ ->
            rickAndMortyViewModel.deleteAllCharacters()
            Snackbar.make(requireView(), "You have successfully deleted all persons from the Saved Persons tab", Snackbar.LENGTH_LONG).show()
        }
        alertDialog.setNegativeButton("No") {_, _ ->}
        alertDialog.setTitle("Do you want to remove all persons from the Saved Persons tab ?")
        alertDialog.create().show()
    }

    private fun emptyRecyclerViewCheck() {
        if (listOfCharactersAdapter.differ.currentList.isNullOrEmpty()) {
            deleteAllCharactersButton.visibility = View.INVISIBLE
        }
    }
}