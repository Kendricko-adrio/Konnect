package edu.bluejack20_2.Konnect.view

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class AddStoryActivity : AppCompatActivity() {

    private val TAG = "ADD_STORY_FRAGMENT"

    private var filepath: Uri = Uri.EMPTY

    private lateinit var userId: String
    private var isImage: Boolean = false
    private val viewModel = AddStoryViewModel()

    private val IMAGE_UPLOAD = 101
    private val VIDEO_UPLOAD = 102

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)

        loadIntentExtras()

        if (isImage) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, IMAGE_UPLOAD)
        } else {
            val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(videoIntent, VIDEO_UPLOAD)
        }

    }

    private fun loadIntentExtras() {
        userId = intent.getStringExtra("userId")!!
        val med = intent.getStringExtra("media_type")!!
        if (med == "image") isImage = true
        else if (med == "video") isImage = false
    }

    private fun getFileExtensions(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_UPLOAD && resultCode == RESULT_OK) {
            // Image upload
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()
            uploadImageBytes(imageData)
        }
        else if (requestCode == VIDEO_UPLOAD && resultCode == RESULT_OK) {
            // Video upload
            val uri = data?.data
            if(uri != null) {
                uploadVideoURI(uri)
            }
        }
        else {
            // Neither
            Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this@AddStoryActivity, HomeActivity::class.java))
            finish()
        }
    }

    private fun uploadImageBytes(data: ByteArray) {
        val date: Date = Date()
        val dir = "stories/" + date.time + ".jpg"
        var ref = FirebaseStorage.getInstance().reference.child(dir)

        var uploadTask = ref.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(applicationContext, "Upload image failed!", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(applicationContext, "Upload image success!", Toast.LENGTH_SHORT).show()
            ref.downloadUrl
                .addOnSuccessListener {
                    val path = it.toString()
                    saveStoryFirestore(path)
                }
        }.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
            Toast.makeText(applicationContext, "Uploading image $progress%", Toast.LENGTH_SHORT).show()
            Log.wtf(TAG, progress.toString())
        }
    }

    private fun uploadVideoURI(uri: Uri) {
        val date = Date()
        val dir = "stories/" + date.time + ".mp4"
        var ref = FirebaseStorage.getInstance().reference.child(dir)

        val uploadTask = ref.putFile(uri)
        uploadTask.addOnFailureListener {
            Toast.makeText(applicationContext, "Upload video failed!", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(applicationContext, "Upload video success!", Toast.LENGTH_SHORT).show()
            ref.downloadUrl
                .addOnSuccessListener {
                    val path = it.toString()
                    saveStoryFirestore(path)
                }
        }.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
            Log.wtf(TAG, progress.toString())
            Toast.makeText(applicationContext, "Uploading video $progress%", Toast.LENGTH_SHORT).show()
        }
    }
}