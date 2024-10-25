package com.example.quizapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.SingleItemLeaderboardBinding
import com.google.firebase.auth.FirebaseAuth

class Adapter(var context: Context, var datalist: ArrayList<DataModel>) :
    RecyclerView.Adapter<Adapter.myViewHolder>() {
    inner class myViewHolder(var binding: SingleItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding =
            SingleItemLeaderboardBinding.inflate(LayoutInflater.from(context), parent, false)
        return myViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.binding.name.text = datalist[position].name
        holder.binding.score.text = datalist[position].score.toString()
    }

}