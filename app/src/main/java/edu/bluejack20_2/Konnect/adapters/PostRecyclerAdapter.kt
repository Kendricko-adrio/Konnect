package edu.bluejack20_2.Konnect.adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.services.KonnectGlideModule
import edu.bluejack20_2.Konnect.services.PostSpannableConverter
import edu.bluejack20_2.Konnect.view.HomeActivity
import edu.bluejack20_2.Konnect.view.PostDetailActivity
import edu.bluejack20_2.Konnect.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.layout_activity_post.view.*

class PostRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<ActivityPost> = ArrayList()
    private var users: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_activity_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {
            is PostViewHolder -> {
                holder.bind(items[position], users)
                holder.itemView.setOnClickListener {
                    Log.wtf("PostRecyclerAdapter", "Clicked!!")
                    val intent = Intent(holder.itemView.context, PostDetailActivity::class.java).apply {
                        putExtra("postId", items.get(position).id)
                    }
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(postList : List<ActivityPost>, users: List<User>) {
        items = postList
        this.users = users
    }

    class PostViewHolder constructor(itemView : View): RecyclerView.ViewHolder(itemView) {

        val identityName = itemView.identity_name
        val identityTitle = itemView.identity_title
        val identityDate = itemView.identity_date
        val identityDp = itemView.identity_profile_picture
        val postContent = itemView.post_content
        val postMedia = itemView.post_media

        fun bind(post: ActivityPost, users: List<User>) {

            identityName.setText(post.user.name)
            identityTitle.setText("Student at Binus University")
            identityDate.setText(DateUtil.timestampToStandardTime(post.createdAt))
            Glide.with(itemView).load(post.user.photoUrl).into(identityDp)

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

            var spannableContent = converter.convertPostSpannableTag(itemView.context, post.content, hashIDPosition)

            postContent.text = spannableContent

            if(post.mediaType.startsWith("image/")) {
                Log.wtf("Post Adapter", "Image " + post.media)
                setPostImage(post.media)
            }
            else if(post.mediaType.startsWith("video/")) {
                Log.wtf("Post Adapter", "Video " + post.media)
                setPostVideo(post.media)
            }
        }

        private fun setPostImage(url: String) {
            itemView.post_media.visibility = View.VISIBLE
            GlideApp.with(itemView.context)
                .load(url)
                .into(postMedia)
        }

        private fun setPostVideo(url: String) {
            itemView.post_video.visibility = View.VISIBLE
            val mediaController = MediaController(itemView.context)

            val videoView: VideoView = itemView.post_video

//            videoView.setMediaController(mediaController)
//            mediaController.setAnchorView(videoView)

            val uri = Uri.parse(url)
            videoView.setVideoURI(uri)
            videoView.start()
        }

        fun getUsernameId(username: String, users: List<User>): String {
            for(user in users) {
                if(username == user.username) {
                    return user.id;
                }
            }
            return ""
        }
    }
}