package edu.bluejack20_2.Konnect.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.view.UserProfileActivity
import kotlin.coroutines.coroutineContext

class SearchDetailAdapter(private val connectionList : MutableList<DocumentReference>): RecyclerView.Adapter<SearchDetailHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDetailHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.search_detail, parent,false)
        return SearchDetailHolder(view)
    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    override fun onBindViewHolder(holder: SearchDetailHolder, position: Int) {
        val doc = connectionList[position]
        doc.get().addOnSuccessListener {
            val iconSize = 100
            Glide.with(holder.itemView).load(it["photoUrl"].toString()).apply(RequestOptions().override(iconSize, iconSize)).into(holder.iv_profilePic)
            holder.tv_name.text = it["name"].toString()
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, UserProfileActivity::class.java)
                intent.putExtra("userId", it.id.toString())
                holder.itemView.context.startActivity(intent)
//                ContextCompat.startActivity(holder.itemView.context ,intent)
            }
        }
    }
}

class SearchDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    lateinit var iv_profilePic: ImageView
    lateinit var tv_name: TextView
    init{
        iv_profilePic = itemView.findViewById(R.id.iv_Connection_pp)
        tv_name = itemView.findViewById(R.id.tv_connection_name)
    }
}