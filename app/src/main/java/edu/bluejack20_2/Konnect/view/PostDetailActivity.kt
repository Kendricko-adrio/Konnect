package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.CommentAdapter
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.viewmodels.PostDetailViewModel
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.coroutines.launch

class PostDetailActivity : AppCompatActivity() {

    private val TAG = "POST_DETAIL_ACTIVITY"

    private val postDetailViewModel = PostDetailViewModel()

    private lateinit var post: ActivityPost
    private lateinit var postId: String
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        loadIntentExtras()
        loadData()
    }

    private fun loadIntentExtras() {
        postId = intent.getStringExtra("postId")!!
    }

    private fun loadData() {
        // wait for this process to finish
        lifecycleScope.launch {
            post = postDetailViewModel.getPostByDocument(postId)
            user = postDetailViewModel.getCurrentUser()
            initializeComponents()
        }
    }

    private fun initializeComponents() {
        likeButtonUpdate()
        post_detail_identity_name.text = post.user.name
        loadImage()
        post_detail_identity_date.text = "Posted at " + DateUtil.timestampToStandardTime(post.createdAt)
        post_detail_content.text = post.content
        post_detail_like_count.text = post.likes.size.toString() + " likes"
        post_detail_comment_count.text = post.comments.size.toString() + " comments"

        val commentAdapter = CommentAdapter(this, R.layout.listview_row_comment, post.comments)
        post.comments.forEachIndexed { index, postComment ->
            post_detail_comments_list.addView(commentAdapter.getView(index, null, post_detail_comments_list))
        }
    }

    private fun loadImage() {
        GlideApp.with(applicationContext)
            .load(post.user.photoUrl)
            .into(post_detail_identity_image)

        GlideApp.with(applicationContext)
            .load(post.media)
            .into(post_detail_media)
    }

    private fun likeButtonUpdate() {
        for (u in post.likes) {
            Log.wtf(TAG, "LIKE BUTTON UPDATE " + u.id.toString() + " COMPARE " + user.id.toString())
            if (u.id == user.id) {
                //Change like button color
                post_detail_like_button.setColorFilter(ContextCompat.getColor(applicationContext, R.color.purple_500), android.graphics.PorterDuff.Mode.SRC_IN)
                return
            }
        }
        post_detail_like_button.setColorFilter(ContextCompat.getColor(applicationContext, R.color.gray_400), android.graphics.PorterDuff.Mode.SRC_IN)
    }

}