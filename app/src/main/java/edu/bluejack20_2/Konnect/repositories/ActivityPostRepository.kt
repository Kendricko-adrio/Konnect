package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import kotlinx.coroutines.tasks.await

object ActivityPostRepository {
    private var db = FirebaseFirestore.getInstance()
    val TAG = "ACTIVITY_POST_REPO"

    suspend fun getAll(): List<ActivityPost> {
        val list = mutableListOf<ActivityPost>()
        val data = db.collection("activity_posts").get().await()

        try {
            for(post in data.documents) {
                val userRef = post["user_ref"] as DocumentReference
                val user = getActivityPostUser(userRef)

                val pObj = post.toObject(ActivityPost::class.java)
                var uObj = user.toObject(User::class.java)

                if(pObj != null && uObj != null) {
                    pObj.user = uObj
                    list.add(pObj)
                }
            }

        }catch(e: Exception) {
            Log.d(TAG, e.toString())
        }

        return list
    }

    suspend fun getActivityPostUser(ref: DocumentReference): DocumentSnapshot {
        val snapshot = ref.get().await()
        return snapshot
    }
}