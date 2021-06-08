package edu.bluejack20_2.Konnect.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.models.SearchData
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.view.InstitutionProfileActivity
import edu.bluejack20_2.Konnect.view.UserProfileActivity
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.coroutines.coroutineContext

class SearchDetailAdapter(private var connectionList: MutableList<SearchData>) :
    RecyclerView.Adapter<SearchDetailHolder>(), Filterable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDetailHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_detail, parent, false)
        return SearchDetailHolder(view)
    }

    var connectionFilterList = mutableListOf<SearchData>()

    init {
        connectionFilterList = connectionList
    }

    override fun getItemCount(): Int {
        return connectionFilterList.size
    }

    override fun onBindViewHolder(holder: SearchDetailHolder, position: Int) {
        val doc = connectionFilterList[position]

        val iconSize = 100
        Glide.with(holder.itemView).load(doc.photoUrl.toString())
            .apply(RequestOptions().override(iconSize, iconSize)).into(holder.iv_profilePic)
        holder.tv_name.text = doc.name.toString()
        holder.itemView.setOnClickListener {
            if(doc.type == 0){
                val intent = Intent(holder.itemView.context, UserProfileActivity::class.java)
                intent.putExtra("userId", doc.doc?.id)
                holder.itemView.context.startActivity(intent)
            }
            if(doc.type == 1){
                val intent = Intent(holder.itemView.context, InstitutionProfileActivity::class.java)
                intent.putExtra("institutionId", doc.doc?.id)
                holder.itemView.context.startActivity(intent)
            }
//                ContextCompat.startActivity(holder.itemView.context ,intent)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val search = constraint.toString()
                val filterResult = FilterResults()
                if (search == "") {
                    connectionFilterList = connectionList
                    filterResult.values = connectionFilterList
                    return filterResult
                }
                val resultList = mutableListOf<SearchData>()
                for (row in connectionList) {

                    if (row.name.toString().toLowerCase(Locale.ROOT)
                            .contains(search.toLowerCase(Locale.ROOT))
                    ) {
                        resultList.add(row)
                    }

                }
                filterResult.values = resultList
                return filterResult

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                connectionFilterList = results?.values as MutableList<SearchData>
                notifyDataSetChanged()
            }
        }
    }
}

class SearchDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var iv_profilePic: ImageView
    lateinit var tv_name: TextView

    init {
        iv_profilePic = itemView.findViewById(R.id.iv_Connection_pp)
        tv_name = itemView.findViewById(R.id.tv_connection_name)
    }
}