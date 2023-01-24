package com.akash.social.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akash.social.R
import com.akash.social.databinding.UserLayoutBinding
import com.akash.social.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso

class UserAdapter(var list: ArrayList<UserModel>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser = list[position]
        Picasso.get().load(currentUser.getImageUrl()).into(holder.binding.userImage)
        holder.binding.userName.text = currentUser.getUsername()
        holder.binding.tvBio.text = currentUser.getBio()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = UserLayoutBinding.bind(itemView)
    }
}