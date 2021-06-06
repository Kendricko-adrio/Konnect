package edu.bluejack20_2.Konnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import edu.bluejack20_2.Konnect.models.Institution
import edu.bluejack20_2.Konnect.models.InstitutionPost
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.services.GlideApp
import kotlinx.android.synthetic.main.layout_activity_institution_post.view.*

class InstitutionPostAdapter(var mctx: Context, var resources: Int, var items:List<InstitutionPost>, var institution: Institution): ArrayAdapter<InstitutionPost>(mctx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(resources, null)

        var mItem: InstitutionPost = items[position]

        GlideApp.with(mctx)
            .load(institution.photoUrl)
            .into(view.identity_profile_picture)

        GlideApp.with(mctx)
            .load(mItem.media)
            .into(view.post_media)

        view.identity_name.text = institution.name
        view.identity_date.text = DateUtil.timestampToStandardTime(mItem.createdAt)

        return view
    }
}