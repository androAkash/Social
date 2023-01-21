package com.akash.social.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akash.social.R
import com.akash.social.SignInActivity
import com.akash.social.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var user : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        user = FirebaseAuth.getInstance()

        binding.btnLogOut.setOnClickListener {
            user.signOut()
            val intent = Intent(requireContext(),SignInActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}