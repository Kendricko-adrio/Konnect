package edu.bluejack20_2.Konnect.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.EducationAdapter
import edu.bluejack20_2.Konnect.adapters.ExperienceAdapter
import edu.bluejack20_2.Konnect.adapters.SkillAdapter
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.viewmodels.EditDetailProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_detail_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.launch

class EditDetailProfileActivity : AppCompatActivity() {

    private val TAG = "EDIT_DETAIL_ACTIVITY"
    private val viewModel = EditDetailProfileViewModel()

    private lateinit var user: User
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_detail_profile)

        loadIntentExtras()
        loadData()
    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
    }

    private fun loadData() {
        // Wait for this process to finish
        lifecycleScope.launch {
            user = viewModel.getUserByDocument(userId)
            Log.wtf(TAG, user.toString())
            initializeComponents()
        }
    }

    private fun initializeComponents() {

        edit_detail_profile_add_experience_button.setOnClickListener {
            val intent = Intent(this, ExperienceInputActivity::class.java).apply {
                putExtra("userId", userId)
            }
            startActivity(intent)
        }

        edit_detail_profile_education_button.setOnClickListener {
            val intent = Intent(this, EducationInputActivity::class.java).apply {
                putExtra("userId", userId)
            }
            startActivity(intent)
        }

        edit_detail_profile_skill_button.setOnClickListener {
            val intent = Intent(this, SkillInputActivity::class.java).apply {
                putExtra("userId", userId)
            }
            startActivity(intent)
        }

        val experienceAdapter = ExperienceAdapter(this, R.layout.listview_row_experience, user.experiences, true, userId)
        user.experiences.forEachIndexed { index, experience ->
            edit_detail_profile_experience_list.addView(experienceAdapter.getView(index, null, edit_detail_profile_experience_list))
        }

        val educationAdapter = EducationAdapter(this, R.layout.listview_row_education, user.educations, true, userId)
        user.educations.forEachIndexed { index, education ->
            edit_detail_profile_education_list.addView(educationAdapter.getView(index, null, edit_detail_profile_education_list))
        }

        val skillAdapter = SkillAdapter(this, R.layout.listview_row_skill, user.skills, true, userId)
        user.skills.forEachIndexed { index, skill ->
            edit_detail_profile_skill_list.addView(skillAdapter.getView(index, null, edit_detail_profile_skill_list))
        }
    }
}