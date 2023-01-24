package com.akash.social.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akash.social.R
import com.akash.social.databinding.PostListBinding
import com.akash.social.model.Post
import com.akash.social.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(private val postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.post_list, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val currentPost = postList[position]
        Picasso.get().load(currentPost.getPostImage()).into(holder.binding.ivPost)
        holder.binding.tvPostDescription.text = currentPost.getDescription()

        publisherInfo(holder.binding.ivUser,holder.binding.tvUsername,currentPost.getPublisher())

    }

    override fun getItemCount(): Int {
        return postList.size
    }

     class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PostListBinding.bind(itemView)
    }

    private fun publisherInfo(
        profilePicture: CircleImageView,
        username: TextView,
        publisherId: String
    ) {
        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(publisherId)

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue<UserModel>(UserModel::class.java)
                    Picasso.get().load(user!!.getImageUrl()).placeholder(R.drawable.person).into(profilePicture)
                    username.text = user.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}