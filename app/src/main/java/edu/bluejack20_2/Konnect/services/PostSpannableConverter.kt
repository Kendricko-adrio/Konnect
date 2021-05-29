package edu.bluejack20_2.Konnect.services

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.view.UserProfileActivity

class PostSpannableConverter {

    private val TAG = "SPANNABLE_CONVERTER"
    public fun getPostMatchResults(post: String): HashMap<String, IntRange> {
        val pattern = "[@][a-zA-Z0-9-.]+".toRegex()
        var matchResults = pattern.findAll(post)
        var results: HashMap<String, IntRange> = HashMap<String, IntRange>()
        var users = mutableListOf<User>()

        for(match in matchResults) {
            results.put(match.value.substring(1), match.range)
        }
        return results
    }

    public fun convertPostSpannableTag(context: Context, post: String,
                                        idPostMap: HashMap<String, IntRange>): SpannableString {
        val spannablePost = SpannableString(post)
        for((id, pos) in idPostMap) {
            var clickableSpan = object: ClickableSpan() {
                override fun onClick(widget: View) {
                    Log.wtf(TAG, "Clicked")
                    val intent: Intent = Intent(context, UserProfileActivity::class.java)
                    intent.putExtra("userId", id)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ContextCompat.startActivity(context, intent, null)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)

                    ds.color = Color.BLUE
                    ds.isUnderlineText = false
                }
            }

            spannablePost.setSpan(
                clickableSpan,
                pos.first, pos.last+1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannablePost
    }
}