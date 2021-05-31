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
import edu.bluejack20_2.Konnect.models.*
import edu.bluejack20_2.Konnect.viewmodels.EducationInputViewModel
import kotlinx.android.synthetic.main.activity_education_input.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EducationInputActivity : AppCompatActivity() {
    private val TAG = "EDUCATION_INPUT_ACT"

    private val viewModel = EducationInputViewModel()
    private var institutions = mutableListOf<Institution>()
    private var degrees = mutableListOf<EducationDegree>()
    private var fields = mutableListOf<StudyField>()

    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()
    private lateinit var userId: String
    private var educationId: String = ""

    private var education: Education = Education()

    private lateinit var selectedDegree: EducationDegree
    private lateinit var selectedInstitution: Institution
    private lateinit var selectedField: StudyField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_input)

        loadIntentExtras()
        loadData()
    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
        if(intent.hasExtra("educationId")) {
            educationId = intent.getStringExtra("educationId")!!
            Log.wtf(TAG, "EducationL " + educationId)
        }
        Log.wtf(TAG, "User: " + userId)
    }

    private fun loadData() {
        lifecycleScope.launch {
            degrees = viewModel.getAllDegrees()
            fields = viewModel.getAllFields()
            institutions = viewModel.getAllInstitutions()

            if(intent.hasExtra("educationId")) {
                Log.wtf(TAG, "loading education by id")
                education = viewModel.getEducationById(educationId)
                Log.wtf(TAG, education.toString())
                initializeComponents()
            }
            else {
                initializeComponentsAdd()
            }
        }
    }

    private fun initializeComponents() {

        startCalendar.timeInMillis = education.startDate.seconds * 1000
        endCalendar.timeInMillis = education.endDate.seconds * 1000
        updateLabel()

        education_input_description_input.setText(education.description)

        initDegreeSpinner()
        initFieldSpinner()
        initInstitutionSpinner()

        initDate()

        education_input_save_button.setOnClickListener {
            save()
        }
    }

    private fun initializeComponentsAdd() {
        initDegreeSpinner()
        initFieldSpinner()
        initInstitutionSpinner()

        initDate()

        education_input_save_button.setOnClickListener {
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

        education_input_start_date.setOnClickListener {
            DatePickerDialog(
                this@EducationInputActivity,
                dateSetListenerStart,
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        education_input_end_date.setOnClickListener {
            DatePickerDialog(
                this@EducationInputActivity,
                dateSetListenerEnd,
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateLabel() {
        val format = "dd MMMM yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        education_input_start_date.setText(sdf.format(startCalendar.time))
        education_input_end_date.setText(sdf.format(endCalendar.time))
    }

    private fun initDegreeSpinner() {
        val adapter = ArrayAdapter<EducationDegree>(this, android.R.layout.simple_spinner_item, degrees)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        edit_education_degree_spinner.adapter = adapter
        edit_education_degree_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDegree = parent?.selectedItem as EducationDegree
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initInstitutionSpinner() {
        val adapter = ArrayAdapter<Institution>(this, android.R.layout.simple_spinner_item, institutions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        edit_education_school_spinner.adapter = adapter
        edit_education_school_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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

    private fun initFieldSpinner() {
        val adapter = ArrayAdapter<StudyField>(this, android.R.layout.simple_spinner_item, fields)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        edit_education_field_spinner.adapter = adapter
        edit_education_field_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedField = parent?.selectedItem as StudyField
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun save() {
        education.institution = selectedInstitution
        education.educationDegree = selectedDegree
        education.studyField = selectedField
        education.startDate = Timestamp(startCalendar.time)
        education.endDate = Timestamp(endCalendar.time)
        education.description = education_input_description_input.text.toString()

        uploadFirestore()
    }

    private fun uploadFirestore() {
        lifecycleScope.launch {
            Log.wtf(TAG, "Education: " + education.toString())
            if(intent.hasExtra("educationId")) {
                viewModel.updateEducation(education)
                Toast.makeText(applicationContext, "Education updated", Toast.LENGTH_LONG).show()
            }
            else {
                viewModel.addEducation(education, userId)
                Toast.makeText(applicationContext, "Education added", Toast.LENGTH_LONG).show()
            }
        }
    }

}