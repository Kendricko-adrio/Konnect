package edu.bluejack20_2.Konnect.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import edu.bluejack20_2.Konnect.models.Skill
import edu.bluejack20_2.Konnect.view.SkillInputActivity
import kotlinx.android.synthetic.main.listview_row_education.view.*
import kotlinx.android.synthetic.main.listview_row_skill.view.*

class SkillAdapter (var mctx: Context, var resources:Int, var items:List<Skill>, var isEdit: Boolean, var userId: String): ArrayAdapter<Skill>(mctx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(resources, null)

        val name: TextView = view.skill_name

        var mItem: Skill = items[position]

        view.skill_name.text = mItem.name

        if(isEdit) {
            view.listview_edit_skill.visibility = View.VISIBLE

            view.listview_edit_skill.setOnClickListener {
                // Remove skill
                val intent = Intent(context, SkillInputActivity::class.java).apply {
                    putExtra("skillId", mItem.id)
                    putExtra("userId", userId)
                }
                mctx.startActivity(intent)
            }
        }

        return view
    }
}