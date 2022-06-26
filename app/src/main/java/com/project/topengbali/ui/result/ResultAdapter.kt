package com.project.topengbali.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.topengbali.R
import com.project.topengbali.databinding.ItemImagesBinding

//adapter untuk menampilkan foto-foto dibawah detail topeng
class ResultAdapter(private val listImage: ArrayList<String>) :
    RecyclerView.Adapter<ResultAdapter.ListViewHolder>() {

    private lateinit var binding: ItemImagesBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        binding =
            ItemImagesBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listImage[position])
    }

    override fun getItemCount(): Int = listImage.size

    class ListViewHolder(itemView: ItemImagesBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var ivMask: ImageView = itemView.image
        fun bind(data: String) {
            Glide.with(itemView.context)
                .load(data)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_load)
                        .error(R.drawable.app_logo)
                )
                .into(ivMask)
        }
    }

}