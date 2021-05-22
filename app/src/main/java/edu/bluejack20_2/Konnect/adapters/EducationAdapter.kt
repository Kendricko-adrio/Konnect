package edu.bluejack20_2.Konnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.Education
import edu.bluejack20_2.Konnect.services.DateUtil
import kotlinx.android.synthetic.main.listview_row_education.view.*

class EducationAdapter (var mctx: Context, var resources:Int, var items:List<Education>): ArrayAdapter<Education>(mctx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(resources, null)

        val imageView: ImageView = view.education_row_image
        val institutionName: TextView = view.education_institution_name
        val title: TextView = view.education_title
        val period: TextView = view.education_period

        var mItem: Education = items[position]

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(mctx)
            .applyDefaultRequestOptions(requestOptions)
            .load(mItem.institution.photoUrl)
            .into(imageView)

        institutionName.text = mItem.institution.name
        title.text = mItem.educationDegree.name + ", " + mItem.studyField.name
        period.text = DateUtil.timestampToYear(mItem.startDate).toString() + " - " + DateUtil.timestampToYear(mItem.endDate).toString()

        return view
    }
}