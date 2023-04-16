package com.example.flexxo.ui.fragments.movieDetailFragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.flexxo.data.common.models.MovieCastEntity
import com.example.flexxo.databinding.ItemCastBinding
import com.example.flexxo.utils.Constants.IMAGE_POST_BASE_URL

class MovieCastAdapter(
    private val mContext: Context,
    private val movieCastList: List<MovieCastEntity>
) : RecyclerView.Adapter<MovieCastAdapter.MovieCastViewHolder>() {

    class MovieCastViewHolder(
        private val binding: ItemCastBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            movieCastEntity: MovieCastEntity,
            mContext: Context
        ) {
            binding.tvName.text = movieCastEntity.name

            val profileImageUrl = IMAGE_POST_BASE_URL + movieCastEntity.profile_path

            Glide.with(mContext)
                .load(profileImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .thumbnail(0.5f)
                .into(binding.ivProfileImage)
        }

        companion object {
            fun from(parent: ViewGroup): MovieCastViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCastBinding.inflate(layoutInflater, parent, false)
                return MovieCastViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCastViewHolder {
        return MovieCastViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return movieCastList.size
    }

    override fun onBindViewHolder(holder: MovieCastViewHolder, position: Int) {
        val currentCast = movieCastList[position]
        holder.bind(currentCast, mContext)
    }
}