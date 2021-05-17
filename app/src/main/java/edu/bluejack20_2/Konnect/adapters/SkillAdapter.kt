package edu.bluejack20_2.Konnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import edu.bluejack20_2.Konnect.models.Skill
import kotlinx.android.synthetic.main.listview_row_skill.view.*

class SkillAdapter (var mctx: Context, var resources:Int, var items:List<Skill>): ArrayAdapter<Skill>(mctx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(resources, null)

        val name: TextView = view.skill_name

        var mItem: Skill = items[position]

        view.skill_name.text = mItem.name

        return view
    }
}