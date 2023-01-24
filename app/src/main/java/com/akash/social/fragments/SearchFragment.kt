package com.akash.social.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.akash.social.adapter.UserAdapter
import com.akash.social.databinding.FragmentSearchBinding
import com.akash.social.model.UserModel
import com.google.firebase.database.*

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var userList : ArrayList<UserModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)

        userList = arrayListOf()

        binding.searchFragmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchFragmentRecyclerView.setHasFixedSize(true)

        binding.SearchET.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.SearchET.toString() == ""){

                }
                else{
                    binding.searchFragmentRecyclerView.visibility = View.VISIBLE

                    retrieveUser()
                    searchUser(p0.toString())

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        return binding.root
    }

    private fun searchUser(input:String){
        val query = FirebaseDatabase.getInstance().reference.child("users")
            .orderByChild("username")
            .startAt(input)
            .endAt(input + "\uf8ff")

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                for (existingSnapshot in snapshot.children){
                    val user = existingSnapshot.getValue(UserModel::class.java)

                    if (user != null){
                        userList.add(user)
                    }
                    val userAdapter = UserAdapter(userList)
                    binding.searchFragmentRecyclerView.adapter = userAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun retrieveUser() {
        val userRef = FirebaseDatabase.getInstance().reference.child("users")
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (binding.SearchET.text.toString() == ""){
                    userList.clear()

                    for (existingSnapshot in snapshot.children){
                        val user = existingSnapshot.getValue(UserModel::class.java)
                        if (user != null){
                            userList.add(user)
                        }
                    }
                    val userAdapter = UserAdapter(userList)
                    binding.searchFragmentRecyclerView.adapter = userAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}