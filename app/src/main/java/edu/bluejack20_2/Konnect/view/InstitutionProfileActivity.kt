package edu.bluejack20_2.Konnect.view

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.InstitutionPostAdapter
import edu.bluejack20_2.Konnect.adapters.UserRelationAdapter
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.models.InstitutionPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.viewmodels.InstitutionProfileViewModel
import kotlinx.android.synthetic.main.activity_institution_profile.*
import kotlinx.coroutines.launch

class InstitutionProfileActivity : AppCompatActivity() {

    private val TAG = "INST_PROFILE_ACT"
    private val viewModel = InstitutionProfileViewModel()
    private lateinit var institutionId: String
    private lateinit var institution: Institution
    private lateinit var currentUser: User
    private lateinit var posts: MutableList<InstitutionPost>

    private var isAdmin = false

    private var relations = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_institution_profile)

        loadIntentExtras()
        loadData()
    }

    private fun loadIntentExtras() {
        institutionId = intent.getStringExtra("institutionId")!!
    }

    private fun loadData() {
        lifecycleScope.launch {
            institution = viewModel.getInstitutionById(institutionId)!!
            relations = viewModel.loadRelations(institutionId)
            posts = viewModel.getInstitutionPostByInstitution(institutionId)
            currentUser = viewModel.getCurrentUser()!!

            initializeComponents()
        }
    }


    private fun initializeComponents() {
        GlideApp.with(applicationContext)
            .load(institution.photoUrl)
            .into(inst_profile_identity_user_image)

        inst_profile_identity_name.text = institution.name
        inst_profile_identity_website.text = institution.website
        inst_profile_identity_location.text = getString(R.string.located_in) + " " + institution.city.name + ", " + institution.city.countryName
        inst_profile_summary.text = institution.summary

        val relationAdapter = UserRelationAdapter(this, R.layout.listview_row_user_relation, relations)
        relations.forEachIndexed { index, relation ->
            inst_profile_relation_list.addView(relationAdapter.getView(index, null, inst_profile_relation_list))
        }

        val postAdapter = InstitutionPostAdapter(this, R.layout.layout_activity_institution_post, posts, institution)
        posts.forEachIndexed { index, institutionPost ->
            inst_profile_post_list.addView(postAdapter.getView(index, null, inst_profile_post_list))
        }

        Log.wtf(TAG, institution.admins.toString())
        Log.wtf(TAG, currentUser.id.toString())

        for(admin in institution.admins) {
            if(admin.id == currentUser.id) {
                isAdmin = true
                inst_edit_button.visibility = View.VISIBLE
            }
        }

        inst_edit_button.setOnClickListener {
            val intent = Intent(applicationContext, EditDetailInstitutionActivity::class.java).apply {
                putExtra("institutionId", institutionId)
            }
            startActivity(intent)
        }

    }
}