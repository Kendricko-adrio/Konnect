package edu.bluejack20_2.Konnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.repositories.UserRepository

class ConnectionListAdapter(private val connectionList : MutableList<DocumentReference>): RecyclerView.Adapter<ConnectionListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionListHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.connection_detail, parent,false)
        return ConnectionListHolder(view)
    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    override fun onBindViewHolder(holder: ConnectionListHolder, position: Int) {
        val fbuser = FirebaseAuth.getInstance().currentUser
        val doc = connectionList[position]
        doc.get().addOnSuccessListener {
            Glide.with(holder.itemView).load(it["photoUrl"].toString()).apply(RequestOptions().override(150, 150)).into(holder.iv_profilePic)
            holder.tv_name.text = it["name"].toString()
        }
        holder.btn_unfriend.setOnClickListener {
            UserRepository.unfriendConnection(fbuser.uid, doc)
        }
    }
}

class ConnectionListHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    lateinit var iv_profilePic: ImageView
    lateinit var tv_name: TextView
    lateinit var btn_unfriend: Button
    init{
        iv_profilePic = itemView.findViewById(R.id.iv_Connection_pp)
        tv_name = itemView.findViewById(R.id.tv_connection_name)
        btn_unfriend = itemView.findViewById(R.id.btn_unfriend)
    }
}