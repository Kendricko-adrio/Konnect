package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.viewmodels.UserProfileViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {

    private val TAG = "USER_PROFILE_ACTIVITY"

    private val userProfileViewModel = UserProfileViewModel()

    private lateinit var user: User
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        loadIntentExtras()
        loadData()
    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
    }

    private fun loadData() {
        // Wait for this process to finish
        lifecycleScope.launch {
            user = userProfileViewModel.getUserByDocument(userId)
            initializeComponents()
        }
    }

    private fun initializeComponents() {

        user_profile_identity_name.text = user.name
        loadImage()
        user_profile_identity_location.text = "Living in " + user.city.name + ", " + user.city.countryName
        user_profile_about_me_content.text = user.summary
    }

    private fun loadImage() {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(applicationContext)
            .applyDefaultRequestOptions(requestOptions)
            .load(user.photoUrl)
            .into(user_profile_identity_user_image)
    }
}