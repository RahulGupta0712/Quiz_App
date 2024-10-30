package com.example.quizapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.databinding.FragmentLeaderboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentLeaderboard : Fragment() {

    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var datalist: ArrayList<LeaderboardModel>
    private lateinit var adapterObject: Adapter
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // initialise properties
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        // Inflate the layout for this fragment
        binding = FragmentLeaderboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datalist = ArrayList()
        adapterObject = Adapter(requireActivity(), datalist)
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(context)
        binding.rvLeaderboard.adapter = adapterObject

        val currentUser =
            auth.currentUser!!    // it will be non-null as leaderboard can only be viewed when a user is signed in

        databaseReference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                datalist.clear()

                for (snap in snapshot.children) {
                    val data = snap.getValue(DataModel::class.java)
                    data?.let {
                        datalist.add(
                            LeaderboardModel(
                                data.name,
                                data.score,
                                (currentUser.uid == snap.key)   // if the current logged in user is this user with current data
                            )
                        )
                    }
                }

                // sorting data based on score
                val sortedList = datalist.sortedByDescending { it.score }

                datalist.clear()

                for (data in sortedList) {
                    datalist.add(data)
                }

                adapterObject.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}