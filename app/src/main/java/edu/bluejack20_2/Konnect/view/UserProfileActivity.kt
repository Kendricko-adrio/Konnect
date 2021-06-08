package edu.bluejack20_2.Konnect.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.UserRecoverableException
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.EducationAdapter
import edu.bluejack20_2.Konnect.adapters.ExperienceAdapter
import edu.bluejack20_2.Konnect.adapters.SkillAdapter
import edu.bluejack20_2.Konnect.base.BaseActivity
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.viewmodels.UserProfileViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.launch

class UserProfileActivity : BaseActivity() {

    private val TAG = "USER_PROFILE_ACTIVITY"

    private val userProfileViewModel = UserProfileViewModel()

    private lateinit var currentUser: User
    private lateinit var user: User
    private lateinit var userId: String

    private var isOwn: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        loadIntentExtras()
        loadData()
    }

    private fun checkBlock(){

    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
    }

    private fun loadData() {
        // Wait for this process to finish
        lifecycleScope.launch {
            user = userProfileViewModel.getUserByDocument(userId)
            currentUser = userProfileViewModel.getCurrentUser()

            Log.wtf("block", currentUser.block_user_ref.toString())

            val userRef = UserRepository.getUserByDoc(user.id)
            for(data in currentUser.block_user_ref){
                if(data == userRef){
                    startActivity(Intent(this@UserProfileActivity, HomeActivity::class.java))
                    Toast.makeText(this@UserProfileActivity, "You have block this user", Toast.LENGTH_SHORT).show()
                    finish()
                    break
                }
            }
            val currUserRef = UserRepository.getUserByDoc(currentUser.id)
            for(data in user.block_user_ref){
                if(data == currUserRef){
                    startActivity(Intent(this@UserProfileActivity, HomeActivity::class.java))
                    Toast.makeText(this@UserProfileActivity, "You have been block by this user", Toast.LENGTH_SHORT).show()
                    finish()
                    break
                }
            }

            if(user.id == currentUser.id) isOwn = true

            initializeComponents()
        }
    }

    private fun initializeComponents() {

        user_profile_identity_name.text = user.name
        loadImage()
        if(user.experiences.size > 0) {
            val firstExperience = user.experiences.first()
            user_profile_identity_title.text = firstExperience.title + " at " + firstExperience.institution.name
        }else {
            user_profile_identity_title.text = "No experience yet"
        }

        user_profile_identity_location.text = "Living in " + user.city.name + ", " + user.city.countryName

        if(user.summary != "") {
            user_profile_about_me_content.text = user.summary
        } else {
            user_profile_about_me_content.text = "No summary"
        }

        user_profile_identity_dob.text = "Birth date, " + DateUtil.timestampToStandardDate(user.dob)

        textView_connection_number.text = user.connections.size.toString() + " connections"

        val experienceAdapter = ExperienceAdapter(this, R.layout.listview_row_experience, user.experiences, false, userId)
        user.experiences.forEachIndexed { index, experience ->
            user_profile_experience_list.addView(experienceAdapter.getView(index, null, user_profile_experience_list))
        }

        val educationAdapter = EducationAdapter(this, R.layout.listview_row_education, user.educations, false, userId)
        user.educations.forEachIndexed { index, education ->
            user_profile_education_list.addView(educationAdapter.getView(index, null, user_profile_education_list))
        }

        val skillAdapter = SkillAdapter(this, R.layout.listview_row_skill, user.skills, false, userId)
        user.skills.forEachIndexed { index, skill ->
            user_profile_skill_list.addView(skillAdapter.getView(index, null, user_profile_skill_list))
        }

        textView_connection_number.setOnClickListener {
            // Connection text clicked --> Redirect to user profile connection page
            val intent = Intent(this, UserProfileConnectionActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        if(isOwn) {
            updateButtons()
        }
        initializeButtons()
        loadButtons()
    }

    private fun initializeButtons() {

        user_profile_edit_identity_btn.setOnClickListener {
            val intent = Intent(applicationContext, EditProfileActivity::class.java)
            intent.putExtra("userId", currentUser.id)
            startActivity(intent)
        }

        user_profile_add_btn.setOnClickListener {
            lifecycleScope.launch {
                // Current user send friend request to user
                userProfileViewModel.addFriend(currentUser.id, user.id)
                Toast.makeText(applicationContext, "Add Friend", Toast.LENGTH_SHORT)
                loadButtons()
            }
        }

        user_profile_accept_btn.setOnClickListener {
            lifecycleScope.launch {
                // User send friend request to current user and current user accept
                userProfileViewModel.acceptFriend(user.id, currentUser.id)
                Toast.makeText(applicationContext, "Accept Friend", Toast.LENGTH_SHORT)
                loadButtons()
            }
        }

        user_profile_decline_btn.setOnClickListener {
            lifecycleScope.launch {
                // User send friend request to current user and current user decline
                userProfileViewModel.declineFriend(user.id, currentUser.id)
                Toast.makeText(applicationContext, "Decline Friend", Toast.LENGTH_SHORT)
                loadButtons()
            }
        }

        user_profile_cancel_btn.setOnClickListener {
            lifecycleScope.launch {
                // Current user send friend request to user and current user cancel the request
                userProfileViewModel.declineFriend(currentUser.id, user.id)
                Toast.makeText(applicationContext, "Cancel Invite", Toast.LENGTH_SHORT)
                loadButtons()
            }
        }
    }

    private fun loadButtons() {
        resetButtons()
        if(isOwn) {
            button_add_post.visibility = View.VISIBLE
            textView_empty_post.visibility = View.VISIBLE
            user_profile_edit_identity_btn.visibility = View.VISIBLE
        } else {
            // Check friend status
            val isFriend = friendStatus()
            if(!isFriend) {
                // If not friend
                var notStranger = false
                for (outbound in user.outbound) {
                    if(outbound.id == currentUser.id) {
                        // Check if there is any outbound request from the current user? -->
                        // Show accept / decline button
                        user_profile_accept_btn.visibility = View.VISIBLE
                        user_profile_decline_btn.visibility = View.VISIBLE
                        notStranger = true
                    }
                }

                for (inbound in user.inbound) {
                    if(inbound.id == currentUser.id) {
                        // Check if there is any inbound request from the current user? -->
                        // Show cancel button
                        user_profile_cancel_btn.visibility = View.VISIBLE
                        notStranger = true
                    }
                }

                // Else show send button to send friend request
                if(!notStranger) {
                    user_profile_add_btn.visibility = View.VISIBLE
                }

            } else {
                // If friend
                // Show message button
                user_profile_message_btn.visibility = View.VISIBLE
            }
        }
    }

    private fun friendStatus(): Boolean {
        for(connection in user.connections) {
            if(connection.id == currentUser.id)  {
                // Already a friend/connection
                return true
            }
        }
        return false
    }

    private fun loadImage() {

        GlideApp.with(applicationContext)
            .load(user.photoUrl)
            .into(user_profile_identity_user_image)
    }

    private fun updateButtons() {
        user_profile_edit_experience_button.visibility = View.VISIBLE
        user_profile_edit_skill_button.visibility = View.VISIBLE
        user_profile_edit_education_button.visibility = View.VISIBLE

        val intent = Intent(applicationContext, EditDetailProfileActivity::class.java)
        intent.putExtra("userId", currentUser.id)

        val redirectToEditDetail = View.OnClickListener() {
            startActivity(intent)
        }
        user_profile_edit_experience_button.setOnClickListener(redirectToEditDetail)
//        user_profile_edit_skill_button.setOnClickListener(redirectToEditDetail)
//        user_profile_edit_education_button.setOnClickListener(redirectToEditDetail)
    }

    private fun resetButtons() {
        button_add_post.visibility = View.GONE
        textView_empty_post.visibility = View.GONE
        user_profile_edit_identity_btn.visibility = View.GONE
        user_profile_message_btn.visibility = View.GONE
        user_profile_add_btn.visibility = View.GONE
        user_profile_cancel_btn.visibility = View.GONE
        user_profile_accept_btn.visibility = View.GONE
        user_profile_decline_btn.visibility = View.GONE
    }
}