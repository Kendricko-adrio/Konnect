package edu.bluejack20_2.Konnect.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.add_post_field
import kotlinx.android.synthetic.main.fragment_post.add_post_send_button
import kotlinx.coroutines.launch
import java.util.*

class PostFragment : Fragment() {

    private val TAG = "POST_FRAGMENT"

    private lateinit var sendButton: ImageView
    private lateinit var inputField: EditText
    private lateinit var imageButton: ImageView
    private lateinit var imagePreview: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var filepath: Uri
    private lateinit var currentUser: User

    companion object {
        fun newInstance() = PostFragment()
    }

    private lateinit var viewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        loadData()
        initializeComponents()

    }
    private fun initializeComponents() {
        sendButton = add_post_send_button
        inputField = add_post_field
        imageButton = add_post_image_button
        imagePreview = add_post_upload_image

        imageButton.setOnClickListener {
            startOpenFileChooser()
        }

        sendButton.setOnClickListener {
            addPostData()
        }
    }

    private fun startOpenFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filepath)
            imagePreview.setImageBitmap(bitmap)
        }
    }

    private fun addPostData() {
        // bunch of validations
        if (inputField.text.length == 0) {
            Toast.makeText(activity?.applicationContext, "Please input a text!", Toast.LENGTH_LONG).show()
            return
        }

        // Upload media to storage (if any)
        if(filepath != null) {
            progressBar = add_post_progressbar
            progressBar.visibility = View.VISIBLE
            val date: Date = Date()
            val dir = "posts/" + date.time + ".jpg"
            var imageRef = FirebaseStorage.getInstance().reference.child(dir)

            imageRef.putFile(filepath)
                .addOnSuccessListener {
                    Toast.makeText(activity?.applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
                    // If success get the downloadUrl
                    imageRef.downloadUrl
                        .addOnSuccessListener {
                            val url = it.toString()
                            savePostFirestore(url)
                        }
                }
                .addOnFailureListener {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {
                    var progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    Log.wtf(TAG, progress.toString())
                }
        }

        // create an object of that post


        // send it back to the viewmodel
    }

    private fun savePostFirestore(uri: String) {
        var postObj: ActivityPost = ActivityPost()
        postObj.content = inputField.text.toString()
        postObj.createdAt = Timestamp.now()
        postObj.media = uri

        // Send it to firestore here
        lifecycleScope.launch {
            viewModel.addPost(postObj, currentUser)
            Log.wtf(TAG, "Success")
            progressBar.visibility = View.INVISIBLE // Success
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            currentUser = viewModel.getCurrentUser()
        }
    }
}
