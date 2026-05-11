package com.example.testdatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComplaintAdapter(
    private var complaintList: List<Complaint>,
    private val onItemClick: (Complaint) -> Unit
) : RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvCardTitle)
        val tvName: TextView = itemView.findViewById(R.id.tvCardStudentName)
        val tvRoll: TextView = itemView.findViewById(R.id.tvCardRollNumber)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCardCategory)
        val tvPriority: TextView = itemView.findViewById(R.id.tvCardPriority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_complaint, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = complaintList[position]
        holder.tvTitle.text = complaint.complaintTitle
        holder.tvName.text = "Student: ${complaint.studentName}"
        holder.tvRoll.text = "Roll No: ${complaint.rollNumber}"
        holder.tvCategory.text = "Category: ${complaint.category}"
        holder.tvPriority.text = complaint.priority

        // Set priority color
        val context = holder.itemView.context
        val color = when (complaint.priority) {
            "Low" -> context.getColor(R.color.priority_low)
            "Medium" -> context.getColor(R.color.priority_medium)
            "High" -> context.getColor(R.color.priority_high)
            "Urgent" -> context.getColor(R.color.priority_urgent)
            else -> context.getColor(R.color.black)
        }
        holder.tvPriority.setTextColor(color)

        holder.itemView.setOnClickListener {
            onItemClick(complaint)
        }
    }

    override fun getItemCount(): Int = complaintList.size

    fun updateList(newList: List<Complaint>) {
        complaintList = newList
        notifyDataSetChanged()
    }
}
