package edu.bluejack20_2.Konnect.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Timestamp
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.fragment_post.*

class PostFragment : Fragment() {

    private val TAG = "POST_FRAGMENT"

    val sendButton = add_post_send_button
    val inputField = add_post_field

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

//        initializeComponents()
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
}