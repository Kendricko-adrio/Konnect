package edu.bluejack20_2.Konnect.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import edu.bluejack20_2.Konnect.view.fragment.SearchFragment
import edu.bluejack20_2.Konnect.viewmodels.HomeViewModel
import edu.bluejack20_2.Konnect.viewmodels.SearchViewModel
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

        loadData()
        initializeBottomNavbar()
        chatButton()
        searchFragment()

    }

    private fun searchFragment(){

        val viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        cl_search_component.visibility = View.GONE
        ib_search.setOnClickListener {
            cl_search_component.visibility = View.VISIBLE
            ib_search.visibility = View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.fragment, SearchFragment(), "search").commit()
        }
        ib_exit.setOnClickListener {
            cl_search_component.visibility = View.GONE
            ib_search.visibility = View.VISIBLE
            supportFragmentManager.findFragmentByTag("search")?.let { it1 ->
                supportFragmentManager.beginTransaction().remove(
                    it1
                ).commit()
            }
        }

        et_search_bar.addTextChangedListener {
            viewModel.setString(it.toString())
            Log.wtf("test", it.toString())
        }
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

    private fun chatButton(){
        val ib_chat = findViewById<ImageButton>(R.id.ib_to_chat)
        ib_chat.setOnClickListener{
            startActivity(Intent(this, ChatActivity::class.java))
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

//    private fun firebaseAuthentication() {
//        btnLogOut = findViewById(R.id.btnSignOut)
//        btnLogOut.setOnClickListener(View.OnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//        })
//
//    }

    public fun hideToolbar() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }
    }
}