package edu.bluejack20_2.Konnect.view

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.base.BaseActivity
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.viewmodels.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity() {
    private val TAG = "EDIT_PROFILE_ACTIVITY"
    val calendar = Calendar.getInstance()

    private lateinit var user: User
    private lateinit var userId: String
    private val viewModel = EditProfileViewModel()

    private var filepath: Uri = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
//        changeFont()
        super.changeFont()
        loadIntentExtras()
        loadData()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
    }

    private fun loadData() {
        lifecycleScope.launch {
            user = viewModel.getUserByDocument(userId)

            initializeComponents()
        }
    }

    private fun initializeComponents() {

        edit_profile_name_input.setText(user.name)
        edit_profile_summary_input.setText(user.summary)
        loadImage()
        calendar.timeInMillis = user.dob.seconds * 1000
        updateLabel()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

        edit_profile_dob_input.setOnClickListener {
            DatePickerDialog(
                this@EditProfileActivity,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        edit_profile_add_picture.setOnClickListener {
            openFileChooser()
        }

        edit_profile_reset_button.setOnClickListener {
            reset()
        }

        edit_profile_save_button.setOnClickListener {
            save()
        }

    }

    private fun updateLabel() {
        val format = "dd MMMM yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        edit_profile_dob_input.setText(sdf.format(calendar.time))
    }

    private fun loadImage() {
        GlideApp.with(applicationContext)
            .load(user.photoUrl)
            .into(edit_profile_picture)
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

    private fun reset() {
        edit_profile_name_input.setText(user.name)
        calendar.timeInMillis = user.dob.seconds * 1000
        edit_profile_summary_input.setText(user.summary)
        updateLabel()
        loadImage()
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
                        user.photoUrl = it.toString()
                        uploadFirestore()
                    }
            }
            .addOnFailureListener {
                edit_profile_progress_bar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            }
            .addOnProgressListener {
                var progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                Log.wtf(TAG, progress.toString())
            }
    }

    private fun save() {
        if(clientValidation()) {
            user.name = edit_profile_name_input.text.toString()
            user.summary = edit_profile_summary_input.text.toString()
            user.dob = Timestamp(calendar.time)
            edit_profile_progress_bar.visibility = View.VISIBLE
            if(filepath != Uri.EMPTY) {
                uploadImage()
            } else {
                uploadFirestore()
            }
        }
    }

    private fun uploadFirestore() {
        lifecycleScope.launch {
            viewModel.updateUserProfile(user)
            edit_profile_progress_bar.visibility = View.GONE
            Toast.makeText(applicationContext, "Profile Updated!", Toast.LENGTH_LONG).show()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clientValidation(): Boolean {
        if(edit_profile_name_input.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "User name cannot be empty!", Toast.LENGTH_SHORT).show()
            return false;
        }
        else if(Timestamp(calendar.time).compareTo(Timestamp.now()) > 0) {
            Toast.makeText(applicationContext, "Date of Birth cannot exceed today's date", Toast.LENGTH_SHORT).show()
            return false;
        }
        else if(edit_profile_summary_input.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Summary cannot be empty!", Toast.LENGTH_SHORT).show()
            return false;
        }
        return true;
    }
}