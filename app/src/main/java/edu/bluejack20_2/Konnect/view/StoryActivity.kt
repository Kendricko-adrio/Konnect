package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.viewmodels.StoryViewModel
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_story.*
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity(), StoriesProgressView.StoriesListener {

    private val TAG = "STORY_ACTIVITY"
    private val viewModel = StoryViewModel()
    private lateinit var stories: MutableList<Story>

    var counter = 0
    var pressTime = 0L
    val limit = 500L

    private lateinit var storiesProgressView: StoriesProgressView
    private lateinit var userId: String
    private lateinit var user: User

    private val onTouchListener = object: View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if(event?.action == MotionEvent.ACTION_DOWN) {
                pressTime = System.currentTimeMillis()
                storiesProgressView.pause()
                return false
            }
            else if(event?.action == MotionEvent.ACTION_UP) {
                val now = System.currentTimeMillis()
                storiesProgressView.resume()
                return limit < now - pressTime
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        storiesProgressView = view_stories

        userId = intent.getStringExtra("userId")!!

        getStories(userId)

        story_prev.setOnClickListener {
            storiesProgressView.skip()
        }

        story_next.setOnTouchListener(onTouchListener)

    }

    override fun onNext() {
        GlideApp.with(applicationContext).load(stories.get(++counter).media).into(story_image)
    }

    override fun onPrev() {
        if((counter-1) < 0) return
        GlideApp.with(applicationContext).load(stories.get(--counter).media).into(story_image)
    }

    override fun onComplete() {
        finish()
    }

    override fun onDestroy() {
        storiesProgressView.destroy()
        super.onDestroy()
    }

    override fun onPause() {
        storiesProgressView.pause()
        super.onPause()
    }

    override fun onResume() {
        storiesProgressView.resume()
        super.onResume()
    }

    private fun getStories(userId: String) {
        // Fetch data from firebase
        lifecycleScope.launch {
            user = viewModel.getUserById(userId)!!
            stories = viewModel.getStoryByUserId(userId)

            storiesProgressView.setStoriesCount(stories.size)
            storiesProgressView.setStoryDuration(5000L)
            storiesProgressView.setStoriesListener(this@StoryActivity)
            storiesProgressView.startStories(counter)

            GlideApp.with(applicationContext)
                .load(stories.get(counter).media)
                .into(story_image)

            story_username.setText(user.username)

            GlideApp.with(applicationContext)
                .load(user.photoUrl)
                .into(story_photo)

        }
    }
}