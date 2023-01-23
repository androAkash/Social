package com.akash.social.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.akash.social.R
import com.akash.social.adapter.PostAdapter
import com.akash.social.databinding.FragmentHomeBinding
import com.akash.social.model.Post
import com.akash.social.model.PostModel
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postList : ArrayList<Post>
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        postList = arrayListOf()

        getPosts()

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.setHasFixedSize(true)

        return binding.root
    }

    private fun getPosts() {
        dbRef = FirebaseDatabase.getInstance().getReference("Post")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                if (snapshot.exists()){
                    for (postSnap in snapshot.children){
                        val postData = postSnap.getValue(Post::class.java)
                        postList.add(postData!!)
                    }
                    val postAdapter = PostAdapter(postList)
                    binding.homeRecyclerView.adapter = postAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}