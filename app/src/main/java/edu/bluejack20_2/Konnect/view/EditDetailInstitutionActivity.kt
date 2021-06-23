package edu.bluejack20_2.Konnect.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.base.BaseActivity
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.viewmodels.EditDetailInstitutionViewModel
import kotlinx.android.synthetic.main.activity_edit_detail_institution.*
import kotlinx.android.synthetic.main.activity_edit_detail_institution.edit_profile_picture
import kotlinx.coroutines.launch
import java.util.*

class EditDetailInstitutionActivity : BaseActivity() {
    private val TAG = "EDIT_DETAIL_INST_ACT"
    private lateinit var institution: Institution
    private lateinit var institutionId: String

    private var cities = mutableListOf<City>()
    private lateinit var selectedCity: City

    private val viewModel = EditDetailInstitutionViewModel()

    private var filepath: Uri = Uri.EMPTY


    override fun onCreate(savedInstanceState: Bundle?) {
        changeFont()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_detail_institution)

        loadIntentExtras()
        loadData()

    }

    private fun loadIntentExtras() {
        institutionId = intent.getStringExtra("institutionId")!!
    }

    private fun loadData() {
        lifecycleScope.launch {
            cities = viewModel.getAllCities()
            institution = viewModel.getInstitutionById(institutionId)!!

            initializeComponents()
        }
    }

    private fun initializeComponents() {
        edit_institution_name_input.setText(institution.name)
        edit_institution_summary_input.setText(institution.summary)

        GlideApp.with(applicationContext)
            .load(institution.photoUrl)
            .into(edit_profile_picture)

        initCitySpinner()

        edit_profile_add_picture.setOnClickListener {
            openFileChooser()
        }

        edit_institution_save_button.setOnClickListener {
            save()
        }
    }

    private fun openFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            edit_profile_picture.setImageBitmap(bitmap)
        }
    }

    private fun initCitySpinner() {
        val adapter = ArrayAdapter<City>(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        edit_institution_city_spinner.adapter = adapter
        edit_institution_city_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCity = parent?.selectedItem as City
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun uploadImage() {
        val date: Date = Date()
        val dir = "posts/" + date.time + ".jpg"
        var imageRef = FirebaseStorage.getInstance().reference.child(dir)

        imageRef.putFile(filepath)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
                // If success get the downloadUrl
                imageRef.downloadUrl
                    .addOnSuccessListener {
                        institution.photoUrl = it.toString()
                        uploadFirestore()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            }
            .addOnProgressListener {
                var progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                Log.wtf(TAG, progress.toString())
            }
    }

    private fun save() {
        institution.name = edit_institution_name_input.text.toString()
        institution.summary = edit_institution_summary_input.text.toString()
        institution.city = selectedCity
        if(filepath != Uri.EMPTY) {
            uploadImage()
        } else {
            uploadFirestore()
        }
    }

    private fun uploadFirestore() {
        lifecycleScope.launch {
            viewModel.updateInstitution(institution)
            Toast.makeText(applicationContext, getString(R.string.insti_updated), Toast.LENGTH_LONG).show()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }

}