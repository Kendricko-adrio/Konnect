package edu.bluejack20_2.Konnect.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.viewmodels.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private val TAG = "HOME_ACTIVITY"

    private lateinit var btnLogOut: Button
    private val homeViewModel = HomeViewModel()

    private var cities: List<City> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        firebaseAuthentication()
        initializeComponents()
        initializeBottomNavbar()
    }

    private fun initializeComponents() {
//        btnLogOut = findViewById(R.id.btnSignOut)
        homeViewModel.getAllCities()
//        for (city in cities) {
//            Log.wtf(TAG, city.name)
//        }
    }

    private fun initializeBottomNavbar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.mainBottomNavigation)
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.networkFragment, R.id.postFragment, R.id.notificationFragment, R.id.settingFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun firebaseAuthentication() {
        btnLogOut.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        })
        var btnChat = findViewById<Button>(R.id.btn_chat)
        btnChat.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        })

    }
}