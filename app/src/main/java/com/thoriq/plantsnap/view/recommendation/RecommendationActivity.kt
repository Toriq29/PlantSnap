package com.thoriq.plantsnap.view.recommendation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thoriq.plantsnap.R
import com.thoriq.plantsnap.data.PlantRecData

class RecommendationActivity : AppCompatActivity() {
    private val list = ArrayList<PlantRecData>()
    private lateinit var plantrv: RecyclerView

    companion object {
        val INTENT_PARCELABLE = "OBJECT_INTENT"
    }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)

        plantrv = findViewById(R.id.rec_rv)
        plantrv.setHasFixedSize(true)

        list.addAll(getListFlower())
        showRecyclerList()
    }

    private fun getListFlower(): ArrayList<PlantRecData> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataSuhu = resources.getStringArray(R.array.data_suhu)
        val dataKetinggian = resources.getStringArray(R.array.data_ketinggian)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listFlower = ArrayList<PlantRecData>()
        for (i in dataName.indices) {
            val flower = PlantRecData(dataName[i], dataDescription[i], dataSuhu[i], dataKetinggian[i], dataPhoto.getResourceId(i, -1))
            listFlower.add(flower)
        }
        return listFlower
    }

    private fun showRecyclerList() {
        plantrv.layoutManager = LinearLayoutManager(this)
        val ListPlantAdapter = RecAdapter(list)
        plantrv.adapter = ListPlantAdapter

        ListPlantAdapter.setOnItemClickCallback(object : RecAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlantRecData) {
                val moveIntent = Intent(this@RecommendationActivity, DetailRecommendation::class.java)
                moveIntent.putExtra(INTENT_PARCELABLE, data)
                startActivity(moveIntent)
            }
        })
    }
}