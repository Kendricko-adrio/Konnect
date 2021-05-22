package edu.bluejack20_2.Konnect.adapters

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Chat

class ChatTextAdapter(var chatArr: MutableList<Chat>) : RecyclerView.Adapter<ChatTextHolder>() {

    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatTextHolder {
        if (viewType == MSG_TYPE_LEFT) {

            var view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_left, parent, false)
            return ChatTextHolder(view)
        } else {
            var view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_right, parent, false)
            return ChatTextHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return chatArr.size
    }

    override fun onBindViewHolder(holder: ChatTextHolder, position: Int) {
        var chat = chatArr[position]
        holder.text.text = chat.text

        if (getItemViewType(position) == MSG_TYPE_LEFT) {
            Glide.with(holder.itemView).load(chat.toPhoto).apply(RequestOptions().override(50, 50))
                .into(holder.image)

        } else {

            Glide.with(holder.itemView).load(chat.fromPhoto)
                .apply(RequestOptions().override(50, 50)).into(holder.image)
        }

//        Glide.with(holder.itemView).load(user.photoURL).apply(RequestOptions().override(150, 150)).into(holder.photoURL)
//        holder.btn_send.setOnClickListener(View.OnClickListener {
//            val message = holder.user_text
////            holder.user_text.text = "" as Editable
//
//
//        })
    }

    override fun getItemViewType(position: Int): Int {
        var fuser = FirebaseAuth.getInstance().currentUser
        if (chatArr[position].from.id.equals(fuser.uid)) {
            return MSG_TYPE_RIGHT
        }
        return MSG_TYPE_LEFT

    }

}

class ChatTextHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var text: TextView
    lateinit var image: CircleImageView

    init {
        text = view.findViewById(R.id.tvUserText)
        image = view.findViewById(R.id.ivUserPicture)

    }
}