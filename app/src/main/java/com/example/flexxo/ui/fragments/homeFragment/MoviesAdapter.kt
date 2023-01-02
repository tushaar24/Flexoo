package com.example.flexxo.ui.fragments.homeFragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flexxo.R
import com.example.flexxo.data.models.Movie
import com.example.flexxo.databinding.ItemMovieBinding
import com.example.flexxo.databinding.ItemMovieV2Binding
import com.example.flexxo.utils.Constants.IMAGE_POST_BASE_URL
import com.squareup.picasso.Picasso

class MoviesAdapter(
    private val onClick: (Movie) -> Unit,
    private val mContext: Context,
    private val type: Boolean
) : PagingDataAdapter<Movie, RecyclerView.ViewHolder>(DiffUtil) {

    private var lastPosition = -1
    val isDataSet: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> return MoviesViewHolder.from(parent)
            1 -> return MovieViewHolder2.from(parent)
        }
        return MoviesViewHolder.from(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return if (type) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentData = getItem(position)
        currentData?.let {
            Log.d("oxoxtushar", "data is here")
            when(holder.itemViewType){
                1 -> (holder as MovieViewHolder2).bind(it, onClick)
                0 -> (holder as MoviesViewHolder).bind(it, onClick)
            }
        }

        setAnimations(holder.itemView, position)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        isDataSet.value = true
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

    class MoviesViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movie: Movie,
            onClick: (Movie) -> Unit
        ) {
            binding.tvMovieName.text = movie.title
            binding.tvReleaseDate.text = movie.release_date
            binding.tvAverageRating.text = movie.vote_average.toString()

            val imageUrl = IMAGE_POST_BASE_URL + movie.poster_path

            Picasso.get()
                .load(imageUrl)
                .fit()
                .into(binding.ivMovieImage)

            binding.root.setOnClickListener {
                onClick(movie)
            }
        }


        companion object {
            fun from(parent: ViewGroup): MoviesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
                return MoviesViewHolder(binding)
            }
        }
    }

    class MovieViewHolder2(private val binding: ItemMovieV2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movie: Movie,
            onClick: (Movie) -> Unit
        ){

            binding.tvMovieName.text = movie.original_title

            val imageUrl = IMAGE_POST_BASE_URL + movie.poster_path

            Picasso.get()
                .load(imageUrl)
                .fit()
                .into(binding.ivMovie)

            binding.root.setOnClickListener {
                onClick(movie)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder2 {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieV2Binding.inflate(layoutInflater, parent, false)
                return MovieViewHolder2(binding)
            }
        }
    }

    object DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return false
        }

    }
}