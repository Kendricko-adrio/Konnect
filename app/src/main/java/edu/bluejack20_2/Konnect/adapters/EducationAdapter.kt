package edu.bluejack20_2.Konnect.adapters

import android.content.Context
import android.content.Intent
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
import edu.bluejack20_2.Konnect.view.EducationInputActivity
import edu.bluejack20_2.Konnect.view.InstitutionProfileActivity
import kotlinx.android.synthetic.main.listview_row_education.view.*
import kotlinx.android.synthetic.main.listview_row_experience.view.*

class EducationAdapter (var mctx: Context, var resources:Int, var items:List<Education>, var isEdit: Boolean, var userId: String?): ArrayAdapter<Education>(mctx, resources, items) {
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

        imageView.setOnClickListener {
            val intent = Intent(parent.context, InstitutionProfileActivity::class.java).apply {
                putExtra("institutionId", mItem.institution.id)
            }
            parent.context.startActivity(intent)
        }

        if(isEdit) {
            view.listview_edit_education.visibility = View.VISIBLE

            view.listview_edit_education.setOnClickListener {
                // Redirect to education edit page
                val intent = Intent(context, EducationInputActivity::class.java).apply {
                    putExtra("educationId", mItem.id)
                    putExtra("userId", userId)
                }

                mctx.startActivity(intent)
            }
        }

        return view
    }
}