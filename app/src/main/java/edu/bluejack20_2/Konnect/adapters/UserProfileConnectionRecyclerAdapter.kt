package edu.bluejack20_2.Konnect.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.GlideApp
import edu.bluejack20_2.Konnect.view.UserProfileActivity
import kotlinx.android.synthetic.main.layout_user_profile_connection.view.*

class UserProfileConnectionRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "USER_PROF_CON_ADAPTER"
    private var items: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserProfileConnectionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user_profile_connection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is UserProfileConnectionViewHolder -> {
                holder.bind(items.get(position))
                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, UserProfileActivity::class.java).apply {
                        putExtra("userId", items.get(position).id)
                    }
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitData(userList: List<User>) {
        items = userList
    }

    class UserProfileConnectionViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName = itemView.user_profile_connection_name
        val userTitle = itemView.user_profile_connection_title
        val userCity = itemView.user_profile_connection_city

        val profilePicture = itemView.user_profile_connection_profile_picture

        fun bind(user: User) {
            userName.text = user.name
            if(user.experiences.size == 0) {
                userTitle.text = "No Experience!"
            }
            userCity.text = user.city.name

            GlideApp.with(itemView.context)
                .load(user.photoUrl)
                .into(profilePicture)
        }
    }

}