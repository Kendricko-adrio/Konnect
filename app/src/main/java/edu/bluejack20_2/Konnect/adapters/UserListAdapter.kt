package edu.bluejack20_2.Konnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.R

class UserListAdapter(var connectionList: MutableList<DocumentReference>):
    RecyclerView.Adapter<UserListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_list_template, parent,false)
        return UserListHolder(view)
    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        val connection = connectionList[position]
        connection.get().addOnSuccessListener {
            holder.name.text = it["name"].toString()
            Glide.with(holder.itemView).load(it["photoUrl"].toString()).apply(RequestOptions().override(150, 150)).into(holder.photo)
        }
    }
}

class UserListHolder(view: View): RecyclerView.ViewHolder(view){

    lateinit var photo: ImageView
    lateinit var name: TextView

    init {
        photo = view.findViewById(R.id.iv_Connection_pp)
        name = view.findViewById(R.id.tv_connection_name)
    }
}