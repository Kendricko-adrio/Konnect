package edu.bluejack20_2.Konnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import edu.bluejack20_2.Konnect.models.Experience
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import kotlinx.android.synthetic.main.listview_row_user_relation.view.*

class UserRelationAdapter(var mctx: Context, var resources:Int, var items:List<User>): ArrayAdapter<User>(mctx, resources, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(resources, null)

        var mItem: User = items[position]

        GlideApp.with(mctx)
            .load(mItem.photoUrl)
            .into(view.user_relation_image)

        view.user_relation_name.text = mItem.name
        view.user_relation_username.text = mItem.username

        return view
    }

}