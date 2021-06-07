package edu.bluejack20_2.Konnect.view

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.MediaController
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
    val storyDuration = 5000L

    private lateinit var storiesProgressView: StoriesProgressView
    private lateinit var userId: String
    private var user: User? = null

    private val onTouchListener = object: View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if(event?.action == MotionEvent.ACTION_DOWN) {
                Log.wtf(TAG, "Hold down")
//                pressTime = System.currentTimeMillis()
//                storiesProgressView.pause()
                return false
            }
            else if(event?.action == MotionEvent.ACTION_UP) {
                Log.wtf(TAG, "Release")
//                val now = System.currentTimeMillis()
//                storiesProgressView.resume()
//                return limit < now - pressTime
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        Log.wtf(TAG, "Start Story")

        storiesProgressView = view_stories

        userId = intent.getStringExtra("userId")!!

        getStories(userId)

        story_prev.setOnClickListener {
            storiesProgressView.reverse()
        }

        story_next.setOnClickListener {
            storiesProgressView.skip()
        }
    }

    override fun onNext() {
        changeStory(stories.get(++counter))
    }

    override fun onPrev() {
        if((counter-1) < 0) return
        changeStory(stories.get(--counter))
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
            Log.wtf(TAG, "Fetching new story")
            holdStory()
            user = viewModel.getUserById(userId)
            stories = viewModel.getStoryByUserId(userId)

            storiesProgressView.setStoriesCount(stories.size)
            storiesProgressView.setStoryDuration(storyDuration)
            storiesProgressView.setStoriesListener(this@StoryActivity)
            storiesProgressView.startStories(counter)
            Log.wtf(TAG, "Story ready $counter")
            Log.wtf(TAG, stories.get(counter).toString())

            changeStory(stories.get(counter))

            // Load the username above and picture above
            story_username.setText(user?.username)
            GlideApp.with(applicationContext)
                .load(user?.photoUrl)
                .into(story_photo)
        }
    }

    private fun changeStory(story: Story) {
        // Load the story content
        // Validate if image or video
        resetMediaPlayers()
        Log.wtf(TAG, story.toString())
        if(story.mediaType.startsWith("image/")) {
            setStoryImage(story.media)
        } else if(story.mediaType.startsWith("video/")) {
            setStoryVideo(story.media)
        }
    }

    private fun setStoryImage(url: String) {
        storiesProgressView.setStoryDuration(storyDuration)
        story_image.visibility = View.VISIBLE
        GlideApp.with(applicationContext)
            .load(url)
            .into(story_image)
        resumeStory()
    }

    private fun setStoryVideo(url: String) {
        holdStory()
        val mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare()
        }
        story_video.visibility = View.VISIBLE
//        val mediaController = MediaController(applicationContext)
//        story_video.setMediaController(mediaController)
//        mediaController.setAnchorView(story_video)

        Log.wtf(TAG, "Duration: " + mediaPlayer.duration.toString())

        storiesProgressView.setStoryDuration(mediaPlayer.duration.toLong())
        val uri = Uri.parse(url)
        story_video.setVideoURI(uri)
        story_video.start()
        resumeStory()
    }

    private fun resetMediaPlayers() {
        story_image.visibility = View.GONE
        story_video.visibility = View.GONE
    }

    private fun holdStory() {
        story_loading_bar.visibility = View.VISIBLE
        storiesProgressView.pause()
    }

    private fun resumeStory() {
        story_loading_bar.visibility = View.GONE
        storiesProgressView.resume()
    }
}