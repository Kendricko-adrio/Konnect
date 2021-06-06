package edu.bluejack20_2.Konnect.view

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.viewmodels.AddStoryViewModel
import edu.bluejack20_2.Konnect.viewmodels.StoryViewModel
import kotlinx.coroutines.launch
import java.util.*

class AddStoryActivity : AppCompatActivity() {

    private val TAG = "ADD_STORY_FRAGMENT"

    private var filepath: Uri = Uri.EMPTY

    private lateinit var userId: String
    private val viewModel = AddStoryViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)

        loadIntentExtras()

        CropImage.activity()
            .setAspectRatio(9, 16).
                start(this)

    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
    }

    private fun getFileExtensions(uri: Uri): String? {
        val contentResolver : ContentResolver = contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun publishStory() {
        val pd = ProgressDialog(this)
        pd.setMessage("Posting")
        pd.show()

        if(filepath != Uri.EMPTY) {
            val date: Date = Date()
            val dir = "stories/" + date.time + getFileExtensions(filepath)
            var ref = FirebaseStorage.getInstance().reference.child(dir)

            ref.putFile(filepath)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
                    ref.downloadUrl
                        .addOnSuccessListener {
                            val uri = it.toString()
                            saveStoryFirestore(uri)
                            pd.dismiss()
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
        else {
            Toast.makeText(applicationContext, "No image selected!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.wtf(TAG, "Request Code: $requestCode what it should have been ${CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE}")
        Log.wtf(TAG, "resultCode: $resultCode what is should have been $RESULT_OK")

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            filepath = result.uri

            publishStory()
        }
        else {
            Toast.makeText(applicationContext, "Something went wrong! But I don't know what it is :(", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun saveStoryFirestore(uri: String) {
        var storyObj = Story()
        var user = User()
        user.id = userId
        storyObj.user = user
        storyObj.media = uri
        storyObj.createdAt = Timestamp.now()
        storyObj.deletedAt = Timestamp(Date(System.currentTimeMillis() + 86400000)) // 24 hours

        lifecycleScope.launch {
            viewModel.addStory(storyObj)
            Toast.makeText(applicationContext, "Story Added!", Toast.LENGTH_LONG).show()
        }
    }

}