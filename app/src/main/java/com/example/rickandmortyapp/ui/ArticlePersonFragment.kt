package com.example.rickandmortyapp.ui


import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.db.ResultDatabase
import com.example.rickandmortyapp.repository.RickAndMortyRepository
import com.example.rickandmortyapp.retrofitservice.RetrofitService
import com.example.rickandmortyapp.viewmodel.RickAndMortyViewModel
import com.example.rickandmortyapp.viewmodelFactory.RickAndMortyViewmodelFactory
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article_person.*


class ArticlePersonFragment : Fragment(R.layout.fragment_article_person) {

    val args: ArticlePersonFragmentArgs by navArgs()
    lateinit var rickAndMortyViewModel: RickAndMortyViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rickAndMortyRepository = RickAndMortyRepository(RetrofitService.getResponse(), ResultDatabase.getInstance(requireContext()))
        val rickAndMortyViewmodelFactory = RickAndMortyViewmodelFactory(rickAndMortyRepository)
        rickAndMortyViewModel = ViewModelProvider(this, rickAndMortyViewmodelFactory).get(RickAndMortyViewModel::class.java)

        val imAvatarDescription = view.findViewById<ImageView>(R.id.imAvatarDescription)
        val tvNameDescription = view.findViewById<TextView>(R.id.tvNameDescription)
        val tvStatusDescription = view.findViewById<TextView>(R.id.tvStatusDescription)
        val tvGenderDescription = view.findViewById<TextView>(R.id.tvGenderDescription)
        val tvSpeciesDescription = view.findViewById<TextView>(R.id.tvSpeciesDescription)
        val tvLocationDescription = view.findViewById<TextView>(R.id.tvLocationDescription)


        Picasso.get().load(args.result.image).into(imAvatarDescription)
        tvNameDescription.setText(args.result.name)
        tvStatusDescription.setText("Status: "+ args.result.status)
        tvGenderDescription.setText("Gender: "+ args.result.gender)
        tvSpeciesDescription.setText("Species: "+ args.result.species)
        tvLocationDescription.setText("Location: "+ args.result.location.name)
        typeCheck(args.result.type)

        val saveCharacterButton = view.findViewById<Button>(R.id.saveCharacterButton)
        saveCharacterButton.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setPositiveButton("Yes") {_, _ ->
                rickAndMortyViewModel.addCharacter(args.result)
                Snackbar.make(view, "You have saved ${args.result.name} to the Saved Persons tab", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        rickAndMortyViewModel.deleteCharacter(args.result)
                        Snackbar.make(view, "You have successfully deleted ${args.result.name} from the Saved Persons tab", Snackbar.LENGTH_SHORT).show()
                    }
                    show()
                }
            }
            alertDialog.setNegativeButton("No") {_, _ ->}
            alertDialog.setTitle("Do you want to save ${args.result.name} to the Saved Persons tab ?")
            alertDialog.create().show()
        }
    }


    private fun typeCheck(type: String): Boolean {
        val tvTypeDescription = view?.findViewById<TextView>(R.id.tvTypeDescription)
        if (type.isNullOrEmpty()) {
            tvTypeDescription?.visibility = View.INVISIBLE
        }
        else {
            tvTypeDescription?.setText("Type: "+ args.result.type)
        }
        return true
    }

}