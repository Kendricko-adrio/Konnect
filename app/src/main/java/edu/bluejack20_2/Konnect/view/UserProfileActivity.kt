package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.EducationAdapter
import edu.bluejack20_2.Konnect.adapters.ExperienceAdapter
import edu.bluejack20_2.Konnect.adapters.SkillAdapter
import edu.bluejack20_2.Konnect.models.Experience
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
            Log.wtf(TAG, user.toString())
            initializeComponents()
        }
    }

    private fun initializeComponents() {

        user_profile_identity_name.text = user.name
        loadImage()
        if(user.experiences.size > 0) {
            val firstExperience = user.experiences.first()
            user_profile_identity_title.text = firstExperience.title + " at " + firstExperience.institution.name
        }
        user_profile_identity_location.text = "Living in " + user.city.name + ", " + user.city.countryName
        user_profile_about_me_content.text = user.summary

        val experienceAdapter = ExperienceAdapter(this, R.layout.listview_row_experience, user.experiences)
        user.experiences.forEachIndexed { index, experience ->
            user_profile_experience_list.addView(experienceAdapter.getView(index, null, user_profile_experience_list))
        }

//        var experienceListView = user_profile_experience_listview
//        experienceListView.adapter = ExperienceAdapter(this, R.layout.listview_row_experience, user.experiences)

//        experienceListView.setOnItemClickListener { parent:AdapterView<*>, view:View, position:Int, id:Long ->
//            Toast.makeText(this, "you click on " + user.experiences.get(position).title, Toast.LENGTH_LONG).show()
//        }

        val educationAdapter = EducationAdapter(this, R.layout.listview_row_education, user.educations)
        user.educations.forEachIndexed { index, education ->
            user_profile_education_list.addView(educationAdapter.getView(index, null, user_profile_education_list))
        }

//        var educationListView = user_profile_education_listview
//        educationListView.adapter = EducationAdapter(this, R.layout.listview_row_education, user.educations)
//
//        educationListView.setOnItemClickListener { parent:AdapterView<*>, view:View, position:Int, id:Long ->
//            Toast.makeText(this, "you click on " + user.educations.get(position).studyField, Toast.LENGTH_LONG).show()
//        }

        val skillAdapter = SkillAdapter(this, R.layout.listview_row_skill, user.skills)
        user.skills.forEachIndexed { index, skill ->
            user_profile_skill_list.addView(skillAdapter.getView(index, null, user_profile_skill_list))
        }

//        var skillListView = user_profile_skill_listview
//        skillListView.adapter = SkillAdapter(this, R.layout.listview_row_skill, user.skills)
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