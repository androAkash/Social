package com.akash.social

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.akash.social.databinding.ActivityPostBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private var myUrl = ""
    private var myImageUri: Uri? = null
    private var storagePostPicRef: StorageReference? = null
    private lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Social)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storagePostPicRef = FirebaseStorage.getInstance().reference.child("PostPictures")

        binding.imvPost.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.btnUpload.setOnClickListener {
            uploadPosts()
        }
    }

    private fun uploadPosts() {
        when {
            myImageUri == null -> Toast.makeText(this, "Please select an image for post by clicking the drawable", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(binding.descriptionEt.text.toString()) -> Toast.makeText(
                this, "Please give a description for the post", Toast.LENGTH_SHORT).show()

            else -> {

                val builder = AlertDialog.Builder(this)

                builder.setMessage("Please wait we are uploading your post..")
                builder.setTitle("Loading")
                builder.setCancelable(false)

                dialog = builder.create()
                dialog.show()

                val fileRef =
                    storagePostPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")

                val muploadPost: StorageTask<*>
                muploadPost = fileRef.putFile(myImageUri!!)

                muploadPost.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            dialog.dismiss()
                            throw it
                        }
                    }
                    return@Continuation fileRef.downloadUrl

                }).addOnCompleteListener(OnCompleteListener<Uri>{ post ->
                    if (post.isSuccessful){
                        val downloadUrl = post.result
                        myUrl = downloadUrl.toString()

                        val currentTime:String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
                        val currentDate:String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                        val ref = FirebaseDatabase.getInstance().reference.child("Post")
                        val postId = ref.push().key

                        val postMap = HashMap<String,Any>()
                        postMap["postId"] = postId!!
                        postMap["description"] = binding.descriptionEt.text.toString()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["postImage"] = myUrl
                        postMap["currentTime"] = currentTime
                        postMap["currentDate"] = currentDate
                        ref.child(postId!!).updateChildren(postMap)

                        Toast.makeText(this, "Post Uploaded successFully", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@PostActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        dialog.dismiss()
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == 1) {
            myImageUri = data.data
            binding.imvPost.setImageURI(myImageUri)
        }
    }
}