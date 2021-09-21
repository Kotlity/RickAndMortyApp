package com.example.rickandmortyapp.ui


import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.adapter.ListOfCharactersAdapter
import com.example.rickandmortyapp.db.ResultDatabase
import com.example.rickandmortyapp.repository.RickAndMortyRepository
import com.example.rickandmortyapp.response.Resource
import com.example.rickandmortyapp.retrofitservice.RetrofitService
import com.example.rickandmortyapp.viewmodel.RickAndMortyViewModel
import com.example.rickandmortyapp.viewmodelFactory.RickAndMortyViewmodelFactory
import kotlinx.coroutines.*


class SearchPersonsFragment : Fragment(R.layout.fragment_search_persons) {

    lateinit var rickAndMortyViewModel: RickAndMortyViewModel
    lateinit var listOfCharactersAdapter: ListOfCharactersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val rickAndMortyRepository = RickAndMortyRepository(RetrofitService.getResponse(), ResultDatabase.getInstance(requireContext()))
        val rickAndMortyViewmodelFactory = RickAndMortyViewmodelFactory(rickAndMortyRepository)
        setupRecyclerView()

        listOfCharactersAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putSerializable("result", it)
            findNavController().navigate(R.id.action_searchPersonsFragment_to_articlePersonFragment, bundle)
        }

        rickAndMortyViewModel = ViewModelProvider(this, rickAndMortyViewmodelFactory).get(RickAndMortyViewModel::class.java)


        var job: Job? = null
        val etSearchCharacters = view.findViewById<EditText>(R.id.etSearchCharacters)
        etSearchCharacters.addTextChangedListener { editable ->
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        rickAndMortyViewModel.getSearchCharacter(editable.toString())
                    }
                }
            }
        }

        rickAndMortyViewModel.listOfSearchCharacters.observe(viewLifecycleOwner, Observer { characters ->
            when(characters) {
                is Resource.Success -> {
                    hideProgressBar()
                    characters.data?.let { response ->
                        listOfCharactersAdapter.differ.submitList(response.results)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    characters.message?.let { message ->
                        Toast.makeText(requireContext(), "An error occured: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val rcViewSearchCharacters = view?.findViewById<RecyclerView>(R.id.rcViewSearchCharacters)
        listOfCharactersAdapter = ListOfCharactersAdapter()
        rcViewSearchCharacters?.adapter = listOfCharactersAdapter
        rcViewSearchCharacters?.layoutManager = LinearLayoutManager(activity)
    }

    private fun showProgressBar() {
        val prBarListOfSearchCharacter = view?.findViewById<ProgressBar>(R.id.prBarListOfSearchCharacters)
        prBarListOfSearchCharacter?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val prBarListOfSearchCharacter = view?.findViewById<ProgressBar>(R.id.prBarListOfSearchCharacters)
        prBarListOfSearchCharacter?.visibility = View.INVISIBLE
    }

}