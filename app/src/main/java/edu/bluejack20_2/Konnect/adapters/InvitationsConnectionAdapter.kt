package edu.bluejack20_2.Konnect.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.repositories.UserRepository

class InvitationsConnectionAdapter(private val connectionList : MutableList<DocumentReference>): RecyclerView.Adapter<InvitationsConnectionHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationsConnectionHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.connection_request_detail, parent,false)
        return InvitationsConnectionHolder(view)
    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    override fun onBindViewHolder(holder: InvitationsConnectionHolder, position: Int) {
        val doc = connectionList[position]
        doc.get().addOnSuccessListener {
            Glide.with(holder.itemView).load(it["photoUrl"].toString()).apply(RequestOptions().override(150, 150)).into(holder.iv_profilePic)
            holder.tv_name.text = it["name"].toString()
            val iconSize = 100
            Glide.with(holder.itemView).load(R.drawable.decline).apply(RequestOptions().override(iconSize, iconSize)).into(holder.ib_decline)
            Glide.with(holder.itemView).load(R.drawable.accept).apply(RequestOptions().override(iconSize, iconSize)).into(holder.ib_accept)
//            holder.ib_accept.setImageResource(R.drawable.accept)
            holder.ib_accept.setOnClickListener{
                val currUser = FirebaseAuth.getInstance().currentUser.uid
                UserRepository.acceptConnection(currUser, doc)
            }

            holder.ib_decline.setOnClickListener{
                val currUser = FirebaseAuth.getInstance().currentUser.uid
                UserRepository.declineConnection(currUser, doc)
            }
        }
    }
}

class InvitationsConnectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    lateinit var iv_profilePic: ImageView
    lateinit var tv_name: TextView
    lateinit var ib_accept: ImageButton
    lateinit var ib_decline: ImageButton
    init{
        iv_profilePic = itemView.findViewById(R.id.iv_Connection_pp)
        tv_name = itemView.findViewById(R.id.tv_connection_name)
        ib_accept = itemView.findViewById(R.id.ib_accept)
        ib_decline = itemView.findViewById(R.id.ib_decline)
    }
}