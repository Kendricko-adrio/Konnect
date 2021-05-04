package edu.bluejack20_2.Konnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.User

public class UserAdapter(private val users: MutableList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Design not yet implemented")
//        return ViewHolder(
//                LayoutInflater.from(parent.context).inflate(
//                      R.layout.user_card, parent, false
//                )
//        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Design not yet implemented")
//        val currentUser = users[position]
//        holder.itemView.apply {
//
//        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

}