package edu.bluejack20_2.Konnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.services.DateUtil
import kotlinx.android.synthetic.main.layout_activity_post.view.*

class PostRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<ActivityPost> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_activity_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PostViewHolder -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(postList : List<ActivityPost>) {
        items = postList
    }

    class PostViewHolder constructor(itemView : View): RecyclerView.ViewHolder(itemView) {
        val identityName = itemView.identity_name
        val identityTitle = itemView.identity_title
        val identityDate = itemView.identity_date

        val postContent = itemView.post_content
        val postMedia = itemView.post_media

        fun bind(post: ActivityPost) {
            identityName.setText(post.user.name)
            identityTitle.setText("Student at Binus University")
            identityDate.setText(DateUtil.timestampToStandardTime(post.createdAt))

            postContent.setText(post.content)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(post.media)
                .into(postMedia)

        }

    }

}