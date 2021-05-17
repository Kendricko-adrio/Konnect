package edu.bluejack20_2.Konnect.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity() {

    private val TAG = "HOME_ACTIVITY"

    private lateinit var btnLogOut: Button
    private val homeViewModel = HomeViewModel()

    private var cities: List<City> = emptyList()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        hideToolbar()

        firebaseAuthentication()
        loadData()
        initializeBottomNavbar()
    }

    private fun loadData() {
        // Wait for this process to finish
        lifecycleScope.launch {
            if(cities.size == 0) {
                cities = homeViewModel.getAllCities()
            }
            user = homeViewModel.getCurrentUser()

            Log.wtf(TAG, "Finish Load Data")
            initializeComponents()
        }
    }

    private fun initializeComponents() {
        Log.wtf(TAG, "Initialize Components")
        btnLogOut = findViewById(R.id.btnSignOut)

        // Load the image to mini user icon on the left top corner
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(applicationContext)
            .applyDefaultRequestOptions(requestOptions)
            .load(user.photoUrl)
            .into(toolbar_user_image)

        toolbar_user_image.setOnClickListener {
            // Redirect to user profile page and tagging the user's document id along
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("userId", user.id)
            startActivity(intent)
        }
    }

    private suspend fun printlnDelayed(message: String) {
        delay(3000)
        Log.wtf(TAG, message)
    }

    private fun initializeBottomNavbar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.mainBottomNavigation)
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.networkFragment, R.id.postFragment, R.id.notificationFragment, R.id.settingFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun firebaseAuthentication() {
        btnLogOut = findViewById(R.id.btnSignOut)
        btnLogOut.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        })
        var btnChat = findViewById<Button>(R.id.btn_chat)
        btnChat.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        })

    }

    public fun hideToolbar() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }
    }
}