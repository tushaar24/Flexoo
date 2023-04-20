package com.example.flexxo.ui.fragments.homeFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.flexxo.R
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.ItemMovieV2Binding
import com.example.flexxo.utils.Constants

class HomeMoviesAdapter(
    private val onClick: (MovieDetails) -> Unit,
    private val listOfMovieDetails: List<MovieDetails>,
    private val mContext: Context,
    private val type: String = "",
    private val onViewMoreClicked: (String) -> Unit = {}

) : RecyclerView.Adapter<HomeMoviesAdapter.HomeMoviesViewHolder>() {

    private var lastPosition = -1
    private var count = 0

    class HomeMoviesViewHolder(private val binding: ItemMovieV2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movieDetails: MovieDetails,
            onClick: (MovieDetails) -> Unit,
            position: Int,
            mContext: Context,
            type: String,
            onViewMoreClicked: (String) -> Unit
        ) {
            if (position != 15) {

                binding.grpShowMovie.visibility = View.VISIBLE
                binding.tvShowViewMore.visibility = View.GONE

                binding.clRootView.setBackgroundColor(mContext.resources.getColor(R.color.color_primary))
                binding.tvMovieName.text = movieDetails.original_title

                val imageUrl = Constants.IMAGE_POST_BASE_URL + movieDetails.poster_path

                Glide.with(mContext)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .thumbnail(0.5f)
                    .into(binding.ivMovie)

                binding.root.setOnClickListener {
                    onClick(movieDetails)
                }

            } else {
                if(type.isNotEmpty()) {
                    binding.grpShowMovie.visibility = View.INVISIBLE
                    binding.tvShowViewMore.visibility = View.VISIBLE
                    binding.clRootView.background = ResourcesCompat.getDrawable(
                        mContext.resources,
                        R.drawable.bg_semi_rounded_corners,
                        mContext.theme
                    )
                    binding.root.setOnClickListener {
                        onViewMoreClicked(type)
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): HomeMoviesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieV2Binding.inflate(layoutInflater, parent, false)
                return HomeMoviesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMoviesViewHolder {
        count++
        return HomeMoviesViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return listOfMovieDetails.size
    }

    override fun onBindViewHolder(holder: HomeMoviesViewHolder, position: Int) {
        val currentMovie = listOfMovieDetails[position]
        holder.bind(currentMovie, onClick, position, mContext, type, onViewMoreClicked)
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


}