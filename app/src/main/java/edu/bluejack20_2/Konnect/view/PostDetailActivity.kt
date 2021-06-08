package edu.bluejack20_2.Konnect.view

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.CommentAdapter
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.services.PostSpannableConverter
import edu.bluejack20_2.Konnect.viewmodels.PostDetailViewModel
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.coroutines.launch

class PostDetailActivity : AppCompatActivity() {

    private val TAG = "POST_DETAIL_ACTIVITY"

    private val postDetailViewModel = PostDetailViewModel()

    private lateinit var post: ActivityPost
    private lateinit var postId: String
    private lateinit var user: User
    private lateinit var users: List<User>

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
            users = postDetailViewModel.getAllUsers()
            initializeComponents()
        }
    }

    private fun initializeComponents() {
        likeButtonUpdate()
        resetMedia()
        post_detail_identity_name.text = post.user.name
        post_detail_identity_date.text = "Posted at " + DateUtil.timestampToStandardTime(post.createdAt)
        post_detail_content.text = preprocessPost(post.content)
        post_detail_content.movementMethod = LinkMovementMethod.getInstance()
        post_detail_like_count.text = post.likes.size.toString() + " likes"
        post_detail_comment_count.text = post.comments.size.toString() + " comments"

        GlideApp.with(applicationContext)
            .load(post.user.photoUrl)
            .into(post_detail_identity_image)

        Log.wtf(TAG, post.mediaType)
        if(post.mediaType.startsWith("image/")) {
            setPostImage(post.media)
        }
        else if(post.mediaType.startsWith("video/")) {
            setPostVideo(post.media)
        }

        post_detail_comments_list.removeAllViews()

        val commentAdapter = CommentAdapter(this, R.layout.listview_row_comment, post.comments)
        post.comments.forEachIndexed { index, postComment ->
            post_detail_comments_list.addView(commentAdapter.getView(index, null, post_detail_comments_list))
        }

        post_detail_like_button.setOnClickListener {
            likeDislikeButton()
        }

        post_detail_comment_send.setOnClickListener {
            addComment()
        }
    }

    private fun preprocessPost(content: String): SpannableString {
        var converter: PostSpannableConverter = PostSpannableConverter()
        var hashUsernamePosition: HashMap<String, IntRange> = HashMap<String, IntRange>()
        var hashIDPosition: HashMap<String, IntRange> = HashMap<String, IntRange>()
        hashUsernamePosition = converter.getPostMatchResults(post.content)

        for((username, pos) in hashUsernamePosition) {
            val id = getUsernameId(username, users)
            if(id != "") {
                hashIDPosition.put(id, pos)
            }
        }

        return converter.convertPostSpannableTag(applicationContext, content, hashIDPosition)
    }

    private fun getUsernameId(username: String, users: List<User>): String {
        for(user in users) {
            if(username == user.username) {
                return user.id;
            }
        }
        return ""
    }

    private fun setPostImage(url: String) {
        post_detail_media.visibility = View.VISIBLE
        GlideApp.with(applicationContext)
            .load(url)
            .into(post_detail_media)
    }

    private fun setPostVideo(url: String) {
        post_detail_video.visibility = View.VISIBLE

        val uri = Uri.parse(url)
        post_detail_video.setVideoURI(uri)
        post_detail_video.setOnPreparedListener(object: MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {
                mp?.isLooping = true
            }
        })
        post_detail_video.start()
    }

    private fun likeDislikeButton() {
        if (isLiked())
            lifecycleScope.launch { postDetailViewModel.dislikePost(postId, user.id) }
        else
            lifecycleScope.launch { postDetailViewModel.likePost(postId, user.id) }
        Toast.makeText(applicationContext, "Like Updated!", Toast.LENGTH_SHORT).show()
        loadData()
    }

    private fun likeButtonUpdate() {
        if (isLiked()) post_detail_like_button.setColorFilter(ContextCompat.getColor(applicationContext, R.color.purple_500), android.graphics.PorterDuff.Mode.SRC_IN)
        else post_detail_like_button.setColorFilter(ContextCompat.getColor(applicationContext, R.color.gray_400), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    private fun addComment() {
        val comment = post_detail_comment_input.text
        if(comment.toString() == "") {
            Toast.makeText(applicationContext, "Please input your comment!", Toast.LENGTH_LONG).show()
        }
        else {
            lifecycleScope.launch {
                postDetailViewModel.addPostComment(postId, user.id, comment.toString())
                loadData()
                Toast.makeText(applicationContext, "Comment added!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLiked(): Boolean {
        for (u in post.likes) {
            if (u.id == user.id) return true
        }
        return false
    }

    private fun resetMedia() {
        post_detail_media.visibility = View.GONE
        post_detail_video.visibility = View.GONE
    }
}