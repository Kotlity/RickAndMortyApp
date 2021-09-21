package com.example.rickandmortyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.db.Result
import com.example.rickandmortyapp.ui.ListOfPersonsFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_of_characters.view.*

class ListOfCharactersAdapter: RecyclerView.Adapter<ListOfCharactersAdapter.MyViewHolder>() {


    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_of_characters, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentList = differ.currentList[position]
        holder.itemView.apply {
            Picasso.get().load(currentList.image).into(imAvatar)
            tvName.text = currentList.name
            tvStatus.text = "Status: "+ currentList.status
            tvGender.text = "Gender: "+ currentList.gender

            setOnClickListener {
                onItemClickListener?.let { it(currentList) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private val differList = object: DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differList)

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }

}