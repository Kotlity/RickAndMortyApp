package com.example.rickandmortyapp.ui


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.rickandmortyapp.R
import com.squareup.picasso.Picasso


class ArticlePersonWithoutSaveButtonFragment : Fragment(R.layout.fragment_article_person_without_save_button) {

    val args: ArticlePersonWithoutSaveButtonFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imAvatarDescriptionWithoutButton = view.findViewById<ImageView>(R.id.imAvatarDescriptionWithoutButton)
        val tvNameDescriptionWithoutButton = view.findViewById<TextView>(R.id.tvNameDescriptionWithoutButton)
        val tvStatusDescriptionWithoutButton = view.findViewById<TextView>(R.id.tvStatusDescriptionWithoutButton)
        val tvGenderDescriptionWithoutButton = view.findViewById<TextView>(R.id.tvGenderDescriptionWithoutButton)
        val tvSpeciesDescriptionWithoutButton = view.findViewById<TextView>(R.id.tvSpeciesDescriptionWithoutButton)
        val tvLocationDescriptionWithoutButton = view.findViewById<TextView>(R.id.tvLocationDescriptionWithoutButton)
        val tvTypeDescriptionWithoutButton = view.findViewById<TextView>(R.id.tvTypeDescriptionWithoutButton)

        val url = args.resultWithoutSaveButton.image
        Picasso.get().load(url).into(imAvatarDescriptionWithoutButton)
        tvNameDescriptionWithoutButton.setText(args.resultWithoutSaveButton.name)
        tvStatusDescriptionWithoutButton.setText(args.resultWithoutSaveButton.status)
        tvGenderDescriptionWithoutButton.setText(args.resultWithoutSaveButton.gender)
        tvSpeciesDescriptionWithoutButton.setText(args.resultWithoutSaveButton.species)
        tvLocationDescriptionWithoutButton.setText(args.resultWithoutSaveButton.location.name)
        tvTypeDescriptionWithoutButton.setText(args.resultWithoutSaveButton.type)
    }
}