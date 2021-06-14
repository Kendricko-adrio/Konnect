package edu.bluejack20_2.Konnect.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Notification
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.view.PostDetailActivity
import edu.bluejack20_2.Konnect.view.UserProfileActivity
import kotlinx.android.synthetic.main.layout_activity_notification.view.*

class NotificationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<Notification> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationviewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_activity_notification, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is NotificationviewHolder -> {
                holder.bind(items[position])
                holder.itemView.setOnClickListener {
                    if(items[position].type == "viewer" || items[position].type == "invitation") {
                        val intent = Intent(holder.itemView.context, UserProfileActivity::class.java).apply {
                            putExtra("userId", items[position].relatedUser.id)
                        }
                        holder.itemView.context.startActivity(intent)
                    }
                    else if(items[position].type == "tag") {
                        val intent = Intent(holder.itemView.context, PostDetailActivity::class.java).apply {
                            putExtra("postId", items[position].postId)
                        }
                        holder.itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(notificationList: List<Notification>) {
        items = notificationList
    }

    class NotificationviewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(notification: Notification) {

            GlideApp.with(itemView.context)
                .load(notification.relatedUser.photoUrl)
                .into(itemView.notification_avatar)

            itemView.notification_content_title.text = notification.relatedUser.username
            itemView.notification_content_date.text = DateUtil.timestampToStandardTime(notification.createdAt)
            when (notification.type) {
                "viewer" -> {
                    itemView.notification_content_content.text = "${notification.relatedUser.username} viewed your profile"
                }
                "invitation" -> {
                    itemView.notification_content_content.text = "You have an upcoming invitation from ${notification.relatedUser.username}"
                }
                "tag" -> {
                    itemView.notification_content_content.text = "${notification.relatedUser.username} tagged you in a post"
                }
                "chat" -> {
                    itemView.notification_content_content.text = "You have a new message from ${notification.relatedUser.username}"
                }
            }
        }
    }
}