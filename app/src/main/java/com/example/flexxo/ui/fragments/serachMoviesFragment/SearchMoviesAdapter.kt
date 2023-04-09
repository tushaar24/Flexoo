package com.example.flexxo.ui.fragments.serachMoviesFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.flexxo.R
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.ItemMovieBinding
import com.example.flexxo.utils.Constants
import com.squareup.picasso.Picasso

class SearchMoviesAdapter(
    private val mContext: Context,
    private val onClick: (MovieDetails) -> Unit,
) : RecyclerView.Adapter<SearchMoviesAdapter.SearchMoviesViewHolder>() {

    private var movieDetailsList: MutableList<MovieDetails> = mutableListOf()
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoviesViewHolder {
        return SearchMoviesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchMoviesViewHolder, position: Int) {
        val currentData = movieDetailsList[position]
        currentData.let {
            holder.bind(it, onClick)
        }
        setAnimations(holder.itemView, position)
    }

    private fun setAnimations(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class SearchMoviesViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movieDetails: MovieDetails,
            onClick: (MovieDetails) -> Unit
        ) {
            binding.tvMovieName.text = movieDetails.title
            binding.tvReleaseDate.text = movieDetails.release_date
            binding.tvAverageRating.text = movieDetails.vote_average.toString()

            val imageUrl = Constants.IMAGE_POST_BASE_URL + movieDetails.poster_path

            Picasso.get()
                .load(imageUrl)
                .fit()
                .into(binding.ivMovieImage)

            binding.root.setOnClickListener {
                onClick(movieDetails)
            }
        }

        companion object {
            fun from(parent: ViewGroup): SearchMoviesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
                return SearchMoviesViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return movieDetailsList.size
    }

    fun setData(newMovieListDetails: List<MovieDetails>) {
        movieDetailsList.clear()
        movieDetailsList.addAll(newMovieListDetails)
        notifyDataSetChanged()
    }
}