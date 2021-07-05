package edu.bluejack20_2.Konnect.view

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.services.PostSpannableConverter
import edu.bluejack20_2.Konnect.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.add_post_field
import kotlinx.android.synthetic.main.fragment_post.add_post_send_button
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class PostFragment : Fragment() {

    private val TAG = "POST_FRAGMENT"

    private lateinit var sendButton: ImageView
    private lateinit var inputField: EditText
    private lateinit var imageButton: ImageView
    private lateinit var videoButton: ImageView
    private lateinit var imagePreview: ImageView
    private lateinit var videoPreview: VideoView
    private lateinit var progressBar: ProgressBar

    private var filepath: Uri = Uri.EMPTY
    private lateinit var currentUser: User

    private var isImage = false

    private var taggedUsers: MutableList<String> = mutableListOf()

    var converter: PostSpannableConverter = PostSpannableConverter()

    private val CHANNEL_ID: String = "Channel1"


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
        createNotificationChannel()
    }

    private fun addNotification(postId: String, content: String) {

        val contentIntent = Intent(requireActivity().applicationContext, PostDetailActivity::class.java)
        contentIntent.putExtra("postId", postId)
        val contentPendingIntent = PendingIntent.getActivity(
            requireContext().applicationContext,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        var notificationBuilder = NotificationCompat.Builder(activity?.applicationContext!!, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_konnect_logo_round)
            .setContentTitle("Congratulations ${currentUser.username}, you have successfully added a new post to your network!")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(requireActivity().applicationContext)
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var channel = NotificationChannel(CHANNEL_ID, "Notif", importance)
            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initializeComponents() {
        resetMediaContainer()
        if(add_post_header_name != null) {
            add_post_header_name.text = currentUser.name
        }
        loadImage()

        sendButton = add_post_send_button
        inputField = add_post_field
        imageButton = add_post_image_button
        videoButton = add_post_video_button
        imagePreview = add_post_upload_image
        videoPreview = add_post_upload_video
        progressBar = add_post_progressbar

        imageButton.setOnClickListener {
            openImageFileChooser()
        }

        videoButton.setOnClickListener {
            openVideoFileChooser()
        }

        sendButton.setOnClickListener {
            addPostData()
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                val pattern = "[@][a-zA-Z0-9-.]+".toRegex()
                var matchResults = pattern.findAll(text)
                var cursorPosition = add_post_field.selectionStart
                var selectedTag: String = ""
                lateinit var matchResult: MatchResult

                for (match in matchResults) {
                    if (cursorPosition - 1 in match.range) {
                        selectedTag = match.value
                        matchResult = match
                        break
                    }
                }

                s?.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    0, text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val hashIDPosition: HashMap<String, IntRange> = HashMap()
                val hashUsernamePosition = converter.getPostMatchResults(text)

                for ((username, pos) in hashUsernamePosition) {
                    val id = getUsernameId(username, currentUser.connections)
                    if (id != "") {
                        hashIDPosition.put(id, pos)
                    }
                }

                Log.wtf(TAG, hashIDPosition.toString())
                taggedUsers = mutableListOf()

                for ((id, pos) in hashIDPosition) {
                    taggedUsers.add(id)
                    s?.setSpan(
                        ForegroundColorSpan(Color.BLUE),
                        pos.start,
                        pos.last + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        add_post_field.addTextChangedListener(textWatcher)
    }

    private fun getUserTag(tag: String, userList: MutableList<User>): User? {
        for (user in userList) {
            if (tag == user.username) {
                return user
            }
        }
        return null
    }

    private fun openImageFileChooser() {
        var i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
    }

    private fun openVideoFileChooser() {
        var intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Video"), 112)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            isImage = true
            filepath = data.data!!
            imagePreview.visibility = View.VISIBLE
            GlideApp.with(activity?.applicationContext!!)
                .load(filepath)
                .into(imagePreview)
        }
        else if(requestCode == 112 && resultCode == Activity.RESULT_OK && data != null) {
            isImage = false
            filepath = data.data!!
            // Load to video view
            videoPreview.setVideoURI(filepath)
            videoPreview.visibility = View.VISIBLE
            videoPreview.setOnPreparedListener(object: MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer?) {
                    mp?.isLooping = true
                }
            })
            videoPreview.start()
        }
    }

    private fun addPostData() {
        // bunch of validations
        if (inputField.text.length == 0) {
            Toast.makeText(activity?.applicationContext, "Please input a text!", Toast.LENGTH_LONG)
                .show()
            return
        }

        // Upload media to storage (if any)
        if (filepath != Uri.EMPTY) {
            progressBar.visibility = View.VISIBLE
            val date: Date = Date()
            lateinit var dir: String
            if(isImage) {
                dir = "posts/" + date.time + ".jpg"
            }
            else {
                dir = "posts/" + date.time + ".mp4"
            }

            val mediaRef = FirebaseStorage.getInstance().reference.child(dir)

            mediaRef.putFile(filepath)
                .addOnSuccessListener {
                    Toast.makeText(activity?.applicationContext, "File Uploaded", Toast.LENGTH_LONG)
                        .show()
                    // If success get the downloadUrl
                    mediaRef.downloadUrl
                        .addOnSuccessListener {
                            val url = it.toString()
                            savePostFirestore(url)
                        }
                }
                .addOnFailureListener {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG)
                        .show()
                }
                .addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    Log.wtf(TAG, progress.toString())
                }
        } else {
            val uri = ""
            savePostFirestore(uri)
        }
    }

    private fun savePostFirestore(uri: String) {
        var postObj: ActivityPost = ActivityPost()
        postObj.content = inputField.text.toString()
        postObj.createdAt = Timestamp.now()
        if (uri != "") postObj.media = uri

        // Send it to firestore here
        lifecycleScope.launch {
            val postId = viewModel.addPost(postObj, currentUser)
            for(taggedUser in taggedUsers) {
                viewModel.addNotification(taggedUser, currentUser.id, "tag", postId)
            }
            addNotification(postId, postObj.content)
            progressBar.visibility = View.INVISIBLE // Success
            startActivity(Intent(activity?.applicationContext, HomeActivity::class.java))
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            currentUser = viewModel.getCurrentUser()
            initializeComponents()
            Log.wtf(TAG, currentUser.toString())
        }
    }

    private fun loadImage() {
        GlideApp.with(requireContext())
            .load(currentUser.photoUrl)
            .into(add_post_header_image)
    }

    fun getUsernameId(username: String, users: List<User>): String {
        for (user in users) {
            if (username == user.username) {
                return user.id
            }
        }
        return ""
    }

    private fun resetMediaContainer() {
        add_post_upload_image?.visibility = View.GONE
        add_post_upload_video?.visibility = View.GONE
    }
}
