package com.example.testdatabase

data class Complaint(
    var id: String = "",
    var studentName: String = "",
    var rollNumber: String = "",
    var complaintTitle: String = "",
    var category: String = "",
    var priority: String = "",
    var description: String = "",
    var status: String = "Pending",
    var createdAt: Long = System.currentTimeMillis()
)
