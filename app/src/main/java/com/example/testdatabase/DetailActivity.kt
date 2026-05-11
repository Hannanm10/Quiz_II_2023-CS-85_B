package com.example.testdatabase

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvName = findViewById<TextView>(R.id.tvDetailStudentName)
        val tvRoll = findViewById<TextView>(R.id.tvDetailRollNumber)
        val tvCategory = findViewById<TextView>(R.id.tvDetailCategory)
        val tvPriority = findViewById<TextView>(R.id.tvDetailPriority)
        val tvStatus = findViewById<TextView>(R.id.tvDetailStatus)
        val tvDate = findViewById<TextView>(R.id.tvDetailDate)
        val tvDescription = findViewById<TextView>(R.id.tvDetailDescription)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarDetail)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Get data from intent
        val title = intent.getStringExtra("title")
        val name = intent.getStringExtra("name")
        val roll = intent.getStringExtra("roll")
        val category = intent.getStringExtra("category")
        val priority = intent.getStringExtra("priority")
        val description = intent.getStringExtra("description")
        val status = intent.getStringExtra("status")
        val timestamp = intent.getLongExtra("timestamp", 0L)

        // Set data to views
        tvTitle.text = title
        tvName.text = "Student: $name"
        tvRoll.text = "Roll Number: $roll"
        tvCategory.text = "Category: $category"
        tvPriority.text = "Priority: $priority"
        tvStatus.text = "Status: $status"
        tvDescription.text = description

        // Format date
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val dateString = sdf.format(Date(timestamp))
        tvDate.text = "Date: $dateString"

        btnBack.setOnClickListener {
            finish()
        }
    }
}
