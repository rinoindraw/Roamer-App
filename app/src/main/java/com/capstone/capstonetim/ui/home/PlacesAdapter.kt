package com.capstone.capstonetim.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.capstonetim.database.model.PlaceResponse
import com.capstone.capstonetim.databinding.ItemRvDestinationBinding
import com.capstone.capstonetim.ui.detail.DetailActivity
import com.capstone.capstonetim.utils.setImageFromUrl
import com.google.gson.Gson

class PlacesAdapter : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private val places: MutableList<PlaceResponse> = mutableListOf()

    class ViewHolder(private val binding: ItemRvDestinationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: PlaceResponse) {
            binding.apply {
                tvLocName.text = place.name
                place.img_link?.let { url ->
                    imgLocation.setImageFromUrl(itemView.context, url)
                }

                itemView.setOnClickListener {
                    val historyListJson = Gson().toJson(listOf(place))
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DETAIL_DATA, historyListJson)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPlaces(newPlaces: List<PlaceResponse>) {
        places.clear()
        places.addAll(newPlaces)
        // places.addAll(newPlaces.sortedBy { it.name })
        notifyDataSetChanged()
    }
}
