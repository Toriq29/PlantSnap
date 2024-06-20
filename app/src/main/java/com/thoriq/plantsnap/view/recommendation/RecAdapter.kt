package com.thoriq.plantsnap.view.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thoriq.plantsnap.R
import com.thoriq.plantsnap.data.PlantRecData

class RecAdapter (private val listFlower: ArrayList<PlantRecData>) : RecyclerView.Adapter<RecAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_plant, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listFlower.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, suhu, ketinggian, photo) = listFlower[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.tvKetinggian.text = ketinggian

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFlower[holder.adapterPosition]) }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_plant)
        val tvName: TextView = itemView.findViewById(R.id.plant_name)
        val tvKetinggian: TextView = itemView.findViewById(R.id.plant_ketinggian)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PlantRecData)
    }
}