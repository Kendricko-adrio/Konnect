package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.viewmodels.InstitutionProfileViewModel
import kotlinx.coroutines.launch

class InstitutionProfileActivity : AppCompatActivity() {

    private val TAG = "INST_PROFILE_ACT"
    private val viewModel = InstitutionProfileViewModel()
    private lateinit var institutionId: String
    private lateinit var institution: Institution

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_institution_profile)
    }

    private fun loadIntentExtras() {
        institutionId = intent.getStringExtra("institutionId")!!
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.loadRelations(institutionId)
        }
    }

}