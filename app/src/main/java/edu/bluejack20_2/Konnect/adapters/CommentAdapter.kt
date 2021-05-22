package edu.bluejack20_2.Konnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import edu.bluejack20_2.Konnect.models.PostComment
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import kotlinx.android.synthetic.main.listview_row_comment.view.*

class CommentAdapter (var mctx: Context, var resources:Int, var items:List<PostComment>): ArrayAdapter<PostComment>(mctx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(resources, null)

        val userImage: ImageView = view.comment_row_image
        val userName: TextView = view.comment_name
        val commentDate: TextView = view.comment_date
        val commentContent: TextView = view.comment_content

        var mItem: PostComment = items[position]

        GlideApp.with(mctx)
            .load(mItem.user.photoUrl)
            .into(userImage)

        userName.text = mItem.user.name
        commentDate.text = DateUtil.timestampToStandardDate(mItem.createdAt)
        commentContent.text = mItem.content

        return view
    }
}