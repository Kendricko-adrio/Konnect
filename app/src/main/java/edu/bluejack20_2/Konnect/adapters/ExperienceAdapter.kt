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
import edu.bluejack20_2.Konnect.models.Experience
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.view.ExperienceInputActivity
import edu.bluejack20_2.Konnect.view.InstitutionProfileActivity
import kotlinx.android.synthetic.main.listview_row_experience.view.*

class ExperienceAdapter (var mctx: Context, var resources:Int, var items:List<Experience>, var isEdit: Boolean, var userId: String?): ArrayAdapter<Experience>(mctx, resources, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(resources, null)

        val imageView: ImageView = view.experience_row_image
        val institutionName: TextView = view.experience_row_institution_name
        val title: TextView = view.experience_row_title
        val employmentType: TextView = view.experience_row_employment
        val period: TextView = view.experience_row_period

        var mItem: Experience = items[position]

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(mctx)
            .applyDefaultRequestOptions(requestOptions)
            .load(mItem.institution.photoUrl)
            .into(imageView)

        view.experience_row_institution_name.text = mItem.institution.name
        view.experience_row_title.text = mItem.title
        view.experience_row_employment.text = mItem.employmentType.name
        view.experience_row_period.text = DateUtil.timestampToMonthYear(mItem.startDate)

        imageView.setOnClickListener {
            val intent = Intent(parent.context, InstitutionProfileActivity::class.java).apply {
                putExtra("institutionId", mItem.institution.id)
            }
            parent.context.startActivity(intent)
        }

        if(isEdit) {
            view.listview_edit_experience.visibility = View.VISIBLE

            view.listview_edit_experience.setOnClickListener {
                // Redirect to experience edit page
                val intent = Intent(context, ExperienceInputActivity::class.java).apply {
                    putExtra("experienceId", mItem.id)
                    putExtra("userId", userId)
                }
                mctx.startActivity(intent)
            }
        }

        return view
    }
}