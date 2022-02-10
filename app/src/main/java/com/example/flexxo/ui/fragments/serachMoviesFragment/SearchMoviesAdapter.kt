package com.example.flexxo.ui.fragments.serachMoviesFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.flexxo.R
import com.example.flexxo.data.models.Movie
import com.example.flexxo.databinding.ItemMovieBinding
import com.example.flexxo.utils.Constants
import com.squareup.picasso.Picasso

class SearchMoviesAdapter(
    private val mContext : Context,
    private val onClick: (Movie) -> Unit,
    ) : RecyclerView.Adapter<SearchMoviesAdapter.SearchMoviesViewHolder>() {

    private var movieList : MutableList<Movie> = mutableListOf()
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoviesViewHolder {
        return SearchMoviesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchMoviesViewHolder, position: Int) {
        val currentData = movieList[position]
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

    fun setLastPositionToMinusOne() {
        lastPosition = -1
    }

    class SearchMoviesViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movie: Movie,
            onClick: (Movie) -> Unit
        ) {
            binding.tvMovieName.text = movie.title
            binding.tvReleaseDate.text = movie.release_date
            binding.tvAverageRating.text = movie.vote_average.toString()

            val imageUrl = Constants.IMAGE_POST_BASE_URL + movie.poster_path

            Picasso.get()
                .load(imageUrl)
                .fit()
                .into(binding.ivMovieImage)

            binding.root.setOnClickListener {
                onClick(movie)
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
        return movieList.size
    }

    fun setData(newMovieList : List<Movie>){
        movieList.clear()
        movieList.addAll(newMovieList)
        notifyDataSetChanged()
    }
}