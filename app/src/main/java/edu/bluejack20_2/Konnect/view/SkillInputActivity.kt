package edu.bluejack20_2.Konnect.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Skill
import edu.bluejack20_2.Konnect.viewmodels.SkillInputViewModel
import kotlinx.android.synthetic.main.activity_skill_input.*
import kotlinx.coroutines.launch

class SkillInputActivity : AppCompatActivity() {

    private val TAG = "SKILL_INPUT_ACTIVITY"

    private val viewModel = SkillInputViewModel()
    private lateinit var userId: String
    private lateinit var skill: Skill
    private lateinit var skillId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skill_input)

        loadIntentExtras()
    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
        if(intent.hasExtra("skillId")) {
            skillId = intent.getStringExtra("skillId")!!
            deleteSkill()
        }
        else {
            initializeComponents()
        }
    }

    private fun deleteSkill() {
        lifecycleScope.launch {
            viewModel.deleteSkill(skillId, userId)
            Toast.makeText(applicationContext, "Skill deleted!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun initializeComponents() {
        skill_add_button.setOnClickListener {
            val text = skill_input_name.text.toString()
            if(text.isNotEmpty()) {
                skill = Skill()
                skill.name = text
                lifecycleScope.launch {
                    viewModel.addSkill(skill, userId)
                    Toast.makeText(applicationContext, "Skill added", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}