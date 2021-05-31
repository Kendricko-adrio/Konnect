package edu.bluejack20_2.Konnect.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.EmploymentType
import edu.bluejack20_2.Konnect.models.Experience
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.viewmodels.ExperienceInputViewModel
import kotlinx.android.synthetic.main.activity_experience_input.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ExperienceInputActivity : AppCompatActivity() {

    private val TAG = "EXPERIENCE_INPUT_ACT"

    private val viewModel = ExperienceInputViewModel()
    private var types = mutableListOf<EmploymentType>()
    private var institutions = mutableListOf<Institution>()

    private var experience: Experience = Experience()
    private lateinit var selectedEmploymentType: EmploymentType
    private lateinit var selectedInstitution: Institution

    private lateinit var userId: String
    private var experienceId: String = ""

    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    private var isWorking: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience_input)

        loadIntentExtras()
        loadData()
    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
        if(intent.hasExtra("experienceId")) {
            experienceId = intent.getStringExtra("experienceId")!!
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            types = viewModel.getAllEmploymentType()
            institutions = viewModel.getAllInstitutions()
            if(experienceId.isNotBlank()) {
                experience = viewModel.getExperienceById(experienceId)
                initializeComponents()
            }
            else {
                initializeComponentsAdd()
            }
        }
    }

    private fun initializeComponents() {
        experience_input_title.setText(experience.title)
        startCalendar.timeInMillis = experience.startDate.seconds * 1000
        endCalendar.timeInMillis = experience.endDate.seconds * 1000
        experience_input_desription.setText(experience.description)
        updateLabel()

        initTypeSpinner()
        initInstitutionSpinner()

        initCheckBox()
        initDate()

        experience_input_save_button.setOnClickListener {
            save()
        }
    }

    private fun initializeComponentsAdd() {
        initTypeSpinner()
        initInstitutionSpinner()

        initCheckBox()
        initDate()

        experience_input_save_button.setOnClickListener {
            save()
        }
    }

    private fun initDate() {
        val dateSetListenerStart =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                startCalendar.set(Calendar.YEAR, year)
                startCalendar.set(Calendar.MONTH, month)
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

        val dateSetListenerEnd =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                endCalendar.set(Calendar.YEAR, year)
                endCalendar.set(Calendar.MONTH, month)
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

        experience_input_start_date.setOnClickListener {
            DatePickerDialog(
                this@ExperienceInputActivity,
                dateSetListenerStart,
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        experience_input_end_date.setOnClickListener {
            DatePickerDialog(
                this@ExperienceInputActivity,
                dateSetListenerEnd,
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun initCheckBox() {
        if(intent.hasExtra("experienceId")) {
            isWorking = experience.isWorking
            edit_experience_checkbox.isChecked = isWorking
        }
        if(isWorking) experience_input_end_date.visibility = View.GONE
        else experience_input_end_date.visibility = View.VISIBLE
        edit_experience_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                // Disable end date edittext
                experience_input_end_date.visibility = View.GONE
                isWorking = true
            }
            else {
                // Enable it
                experience_input_end_date.visibility = View.VISIBLE
                isWorking = false
            }
        }
    }

    private fun updateLabel() {
        val format = "dd MMMM yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        experience_input_start_date.setText(sdf.format(startCalendar.time))
        experience_input_end_date.setText(sdf.format(endCalendar.time))
    }

    private fun initTypeSpinner() {
        val adapter = ArrayAdapter<EmploymentType>(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        edit_experience_employment_spinner.adapter = adapter
        edit_experience_employment_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedEmploymentType = parent?.selectedItem as EmploymentType
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initInstitutionSpinner() {
        val adapter = ArrayAdapter<Institution>(this, android.R.layout.simple_spinner_item, institutions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        edit_experience_institution_spinner.adapter = adapter
        edit_experience_institution_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedInstitution = parent?.selectedItem as Institution
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun save() {
        experience.institution = selectedInstitution
        experience.title = experience_input_title.text.toString()
        experience.employmentType = selectedEmploymentType
        experience.startDate = Timestamp(startCalendar.time)
        experience.isWorking = isWorking
        if(!isWorking) experience.endDate = Timestamp(endCalendar.time)
        experience.description = experience_input_desription.text.toString()

        uploadFirestore()
    }

    private fun uploadFirestore() {
        lifecycleScope.launch {
            if(intent.hasExtra("experienceId")) {
                Log.wtf(TAG, experience.toString())
                viewModel.updateExperience(experience)
                Toast.makeText(applicationContext, "Experience updated", Toast.LENGTH_LONG).show()
            } else {
                Log.wtf(TAG, experience.toString())
                viewModel.addExperience(experience, userId)
                Toast.makeText(applicationContext, "Experience added", Toast.LENGTH_LONG).show()
            }
        }
    }
}