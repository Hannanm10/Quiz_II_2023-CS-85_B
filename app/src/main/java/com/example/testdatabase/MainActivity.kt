package com.example.testdatabase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var etStudentName: TextInputEditText
    private lateinit var etRollNumber: TextInputEditText
    private lateinit var etTitle: TextInputEditText
    private lateinit var spinnerCategory: MaterialAutoCompleteTextView
    private lateinit var spinnerPriority: MaterialAutoCompleteTextView
    private lateinit var etDescription: TextInputEditText
    private lateinit var btnSubmit: MaterialButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

    private lateinit var database: DatabaseReference
    private lateinit var adapter: ComplaintAdapter
    private val complaintList = mutableListOf<Complaint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Toolbar
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize Views
        etStudentName = findViewById(R.id.etStudentName)
        etRollNumber = findViewById(R.id.etRollNumber)
        etTitle = findViewById(R.id.etTitle)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        spinnerPriority = findViewById(R.id.spinnerPriority)
        etDescription = findViewById(R.id.etDescription)
        btnSubmit = findViewById(R.id.btnSubmit)
        recyclerView = findViewById(R.id.recyclerView)
        tvEmpty = findViewById(R.id.tvEmpty)

        // Use explicit URL if google-services.json is missing it
        val databaseUrl = "https://testdatabase-e88b2-default-rtdb.firebaseio.com/"
        database = FirebaseDatabase.getInstance(databaseUrl).getReference("Complaints")

        setupDropdowns()
        setupRecyclerView()
        fetchComplaints()

        btnSubmit.setOnClickListener {
            submitComplaint()
        }
    }

    private fun setupDropdowns() {
        val categories = arrayOf("IT", "Library", "Transport", "Hostel", "Accounts", "Examination", "Cafeteria", "Administration")
        val priorities = arrayOf("Low", "Medium", "High", "Urgent")

        val catAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        spinnerCategory.setAdapter(catAdapter)
        spinnerCategory.setText(categories[0], false) // Set default

        val prioAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priorities)
        spinnerPriority.setAdapter(prioAdapter)
        spinnerPriority.setText(priorities[0], false) // Set default
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ComplaintAdapter(complaintList) { complaint ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("title", complaint.complaintTitle)
                putExtra("name", complaint.studentName)
                putExtra("roll", complaint.rollNumber)
                putExtra("category", complaint.category)
                putExtra("priority", complaint.priority)
                putExtra("description", complaint.description)
                putExtra("status", complaint.status)
                putExtra("timestamp", complaint.createdAt)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun submitComplaint() {
        Log.d("MainActivity", "submitComplaint called")
        val name = etStudentName.text.toString().trim()
        val roll = etRollNumber.text.toString().trim()
        val title = etTitle.text.toString().trim()
        val category = spinnerCategory.text.toString()
        val priority = spinnerPriority.text.toString()
        val description = etDescription.text.toString().trim()

        Log.d("MainActivity", "Fields: name=$name, roll=$roll, title=$title, cat=$category, prio=$priority, desc=$description")

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(roll) || TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Log.w("MainActivity", "Validation failed: Some fields are empty")
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val id = database.push().key ?: return
        val complaint = Complaint(id, name, roll, title, category, priority, description)

        database.child(id).setValue(complaint).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MainActivity", "Complaint submitted successfully")
                Toast.makeText(this, "Complaint Submitted Successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Log.e("MainActivity", "Failed to submit complaint", task.exception)
                Toast.makeText(this, "Failed to submit: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        etStudentName.text?.clear()
        etRollNumber.text?.clear()
        etTitle.text?.clear()
        etDescription.text?.clear()
        spinnerCategory.setText("", false)
        spinnerPriority.setText("", false)
    }

    private fun fetchComplaints() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("MainActivity", "onDataChange: ${snapshot.childrenCount} items")
                complaintList.clear()
                for (data in snapshot.children) {
                    val complaint = data.getValue(Complaint::class.java)
                    Log.d("MainActivity", "Fetched: $complaint")
                    complaint?.let { complaintList.add(it) }
                }

                complaintList.sortByDescending { it.createdAt }

                if (complaintList.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
                adapter.updateList(complaintList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
