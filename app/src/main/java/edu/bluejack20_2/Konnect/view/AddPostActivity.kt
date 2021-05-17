package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Timestamp
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ActivityPost
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : AppCompatActivity() {

    private val TAG = "ADD_POST_ACTIVITY"

    val sendButton = add_post_send_button
    val inputField = add_post_field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        hideToolbar()

        initializeComponents()
    }

    private fun initializeComponents() {
        sendButton.setOnClickListener {
            addPost()
        }
    }

    private fun addPost() {
        // bunch of validations
        val input = inputField.text

        if(input.length == 0) {
            return
        }

        // create an object of that post
        var post: ActivityPost = ActivityPost()
        post.content = input.toString()
        post.createdAt = Timestamp.now()

        // send it back to the viewmodel
    }

    private fun hideToolbar() {
        supportActionBar?.hide()
    }
}