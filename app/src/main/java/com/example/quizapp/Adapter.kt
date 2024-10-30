package com.example.quizapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.SingleItemLeaderboardBinding
import com.google.firebase.auth.FirebaseAuth

class Adapter(var context: Context, var datalist: ArrayList<LeaderboardModel>) :
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
        holder.binding.name.text = "${position + 1}. ${datalist[position].name}"
        holder.binding.score.text = datalist[position].score.toString()

        if(datalist[position].isMe) {
            holder.binding.root.background = ContextCompat.getDrawable(context, R.drawable.background_white)
            holder.binding.name.setTextColor(ContextCompat.getColor(context, R.color.blue))
            holder.binding.score.setTextColor(ContextCompat.getColor(context, R.color.blue))
        }
    }

}