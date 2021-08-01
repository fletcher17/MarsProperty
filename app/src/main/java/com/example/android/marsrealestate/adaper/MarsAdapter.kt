package com.example.android.marsrealestate.adaper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.clickListener.DetailsClickListener
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

class MarsAdapter(val clickItem: DetailsClickListener) : RecyclerView.Adapter<MarsAdapter.MarsViewHolder>() {
    var listOfMars: List<MarsProperty> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsViewHolder {
        val binding = GridViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MarsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarsViewHolder, position: Int) {
        val marsProps = listOfMars[position]

        holder.bind(marsProps, clickItem)
    }

    override fun getItemCount(): Int {
        return listOfMars.size
    }


    inner class MarsViewHolder(val binding: GridViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(marsProperties: MarsProperty, clickListener: DetailsClickListener) {
            Glide.with(binding.root).load(marsProperties.imgSrcUrl).apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_broken_image))
                    .into(binding.marsImage)

            binding.marsImage.setOnClickListener {
                clickListener.sendDetails(marsProperties)
            }
        }

    }

    fun newListOfMars(marsPictures : List<MarsProperty>) {
        this.listOfMars = marsPictures
        notifyDataSetChanged()
    }
}