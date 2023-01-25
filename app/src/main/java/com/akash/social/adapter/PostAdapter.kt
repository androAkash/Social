package com.akash.social.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
//    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

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

        checkingLikedPosts(currentPost.getPostId(),holder.binding.tvLike)

        holder.binding.tvLike.setOnClickListener {
            if (holder.binding.tvLike.text.toString() == "LikePost"){
                firebaseUser?.uid.let { it1->
                    FirebaseDatabase.getInstance().reference
                        .child("LikePost").child(it1.toString())
                        .child("PostLiked").child(currentPost.getPostId())
                        .setValue(true).addOnCompleteListener { task->
                            if (task.isSuccessful){
                                firebaseUser?.uid.let{it2->
                                    FirebaseDatabase.getInstance().reference
                                        .child("LikePost").child(currentPost.getPostId())
                                        .child("AlreadyLikedPosts").child(it2.toString())
                                        .setValue(true).addOnCompleteListener { task->
                                            if (task.isSuccessful){

                                            }
                                        }
                                }
                            }
                        }
                }
            }
            else{
                firebaseUser?.uid.let { it1->
                    FirebaseDatabase.getInstance().reference
                        .child("LikePost").child(it1.toString())
                        .child("PostLiked").child(currentPost.getPostId())
                        .removeValue().addOnCompleteListener { task->
                            if (task.isSuccessful){
                                firebaseUser?.uid.let{it2->
                                    FirebaseDatabase.getInstance().reference
                                        .child("LikePost").child(currentPost.getPostId())
                                        .child("AlreadyLikedPosts").child(it2.toString())
                                        .removeValue().addOnCompleteListener { task->
                                            if (task.isSuccessful){

                                            }
                                        }
                                }
                            }
                        }
                }
            }
        }

        publisherInfo(holder.binding.ivUser,holder.binding.tvUsername,currentPost.getPublisher())

    }

    private fun checkingLikedPosts(postId: String, tvLike: Button) {
        val likedPostRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("LikePost").child(it1.toString())
                .child("PostLiked")
        }
        likedPostRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(postId).exists()){
                    tvLike.text = "PostLiked"
                }
                else{
                    tvLike.text = "LikePost"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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