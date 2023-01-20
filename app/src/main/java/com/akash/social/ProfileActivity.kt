package com.akash.social

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.akash.social.databinding.ActivityProfileBinding
import com.akash.social.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var selectedImage: Uri? = null
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Social)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile")
            .setCancelable(false)

        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.profileIv.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        binding.updateBtn.setOnClickListener {
            if (binding.username.editText?.text!!.isEmpty()){
                Toast.makeText(this,"Please write your username",Toast.LENGTH_SHORT).show()
            }
            if (selectedImage == null){
                Toast.makeText(
                    this,
                    "Please Select your image by clicking the vector",
                    Toast.LENGTH_SHORT
                ).show()
            } else{
                dialog.show()
                uploadProfileData()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                selectedImage = data.data!!
                binding.profileIv.setImageURI(selectedImage)
            }
        }
    }

    private fun uploadProfileData(){
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImage!!).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String){
        val user = UserModel(auth.uid.toString(),
            binding.username.editText?.text.toString(), binding.bio.editText?.text.toString(),imgUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully inserted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }
}