package com.thoriq.plantsnap.view.recommendation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thoriq.plantsnap.R
import com.thoriq.plantsnap.data.PlantRecData

class DetailRecommendation : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_recommendation)
        val Flower = intent.getParcelableExtra<PlantRecData>(RecommendationActivity.INTENT_PARCELABLE)
        val name: TextView = findViewById(R.id.detail_name)
        val description: TextView = findViewById(R.id.description_detail)
        val suhu: TextView = findViewById(R.id.suhu)
        val ketinggian: TextView = findViewById(R.id.ketinggian)
        val photo: ImageView = findViewById(R.id.img_item_photo)

        photo.setImageResource(Flower?.photo!!)
        name.text = Flower.name
        description.text = Flower.description
        suhu.text = Flower.suhu
        ketinggian.text = Flower.ketinggian
    }
}