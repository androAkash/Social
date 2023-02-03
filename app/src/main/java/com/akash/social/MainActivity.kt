package com.akash.social

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.akash.social.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val rotateOpen:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    private val rotateClose:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim) }
    private val fromBottom:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }

    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Social)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.navigationView.background = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        setupWithNavController(binding.navigationView, navController)

        binding.btnFloating.setOnClickListener {
            onAddButtonClicked()
        }

        binding.ftBtnGallery.setOnClickListener {
            val intent = Intent(this@MainActivity, PostActivity::class.java)
            startActivity(intent)
        }
        binding.ftBtnVideo.setOnClickListener {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked:Boolean) {
        if (!clicked){
            binding.ftBtnGallery.startAnimation(fromBottom)
            binding.ftBtnVideo.startAnimation(fromBottom)
            binding.btnFloating.startAnimation(rotateOpen)
        } else{
            binding.ftBtnGallery.startAnimation(toBottom)
            binding.ftBtnVideo.startAnimation(toBottom)
            binding.btnFloating.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked:Boolean) {
        if (!clicked){
            binding.ftBtnGallery.visibility = View.VISIBLE
            binding.ftBtnVideo.visibility = View.VISIBLE
        } else{
            binding.ftBtnGallery.visibility = View.INVISIBLE
            binding.ftBtnVideo.visibility = View.INVISIBLE
        }
    }

    private fun setClickable(clicked: Boolean){
        if (!clicked){
            binding.ftBtnGallery.isClickable = true
            binding.ftBtnVideo.isClickable = true
        } else{
            binding.ftBtnGallery.isClickable = false
            binding.ftBtnVideo.isClickable = false
        }
    }
    //Navigate to ProfileActivity

    private fun navigateToProfile() {
        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.profile_activity -> navigateToProfile()
        }

        return true
    }
}