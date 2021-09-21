package com.example.rickandmortyapp.ui


import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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
import kotlinx.android.synthetic.main.fragment_list_of_persons.*


class ListOfPersonsFragment : Fragment(R.layout.fragment_list_of_persons) {

    lateinit var listOfCharactersAdapter: ListOfCharactersAdapter
    lateinit var rickAndMortyViewModel: RickAndMortyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val rickAndMortyRepository = RickAndMortyRepository(RetrofitService.getResponse(), ResultDatabase.getInstance(requireContext()))
        val rickAndMortyViewModelFactory = RickAndMortyViewmodelFactory(rickAndMortyRepository)

        rickAndMortyViewModel = ViewModelProvider(this, rickAndMortyViewModelFactory).get(RickAndMortyViewModel::class.java)

        listOfCharactersAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putSerializable("result", it)
            findNavController().navigate(R.id.action_listOfPersonsFragment_to_articlePersonFragment, bundle)
        }


        rickAndMortyViewModel.listOfCharacters.observe(viewLifecycleOwner, Observer { characters ->
            when(characters) {
                is Resource.Success -> {
                    hideProgressBar()
                    characters.data?.let { response ->
                        listOfCharactersAdapter.differ.submitList(response.results.toList())
                        val totalPages = response.info.pages / 20 + 2
                        isLastPage = rickAndMortyViewModel.listOfCharactersPageNumber == totalPages
                        if (isLastPage) {
                            rcViewListOfCharacters.setPadding(0,0,0,0)
                        }
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

        rickAndMortyViewModel.getAllCharacters()
    }

    private fun showProgressBar() {
        val prBarListOfCharacters = view?.findViewById<ProgressBar>(R.id.prBarListOfCharacters)
        prBarListOfCharacters?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val prBarListOfCharacters = view?.findViewById<ProgressBar>(R.id.prBarListOfCharacters)
        prBarListOfCharacters?.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView() {
        val rcViewListOfCharacters = view?.findViewById<RecyclerView>(R.id.rcViewListOfCharacters)
        listOfCharactersAdapter = ListOfCharactersAdapter()
        rcViewListOfCharacters?.adapter = listOfCharactersAdapter
        rcViewListOfCharacters?.layoutManager = LinearLayoutManager(activity)
        rcViewListOfCharacters?.addOnScrollListener(this@ListOfPersonsFragment.scrollListener)
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    val scrollListener = object: RecyclerView.OnScrollListener() {


        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {  // that means we are currently scrolling
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition() // first visible item position in the RecyclerView
            val visibleItemCount = layoutManager.childCount // the whole list of visible items in the RecyclerView
            val totalItemCount = layoutManager.itemCount // the WHOLE LIST of items in the RecyclerView

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20 // 20 elements on one page
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                rickAndMortyViewModel.getAllCharacters()
                isScrolling = false
            }
        }
    }

}