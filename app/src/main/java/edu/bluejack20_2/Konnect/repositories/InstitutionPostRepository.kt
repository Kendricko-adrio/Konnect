package edu.bluejack20_2.Konnect.repositories

import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.InstitutionPost
import kotlinx.coroutines.tasks.await

object InstitutionPostRepository {
    private val TAG = "INST_POST_REPO"
    private var db = FirebaseFirestore.getInstance()

    suspend fun getInstitutionPostByInstitution(institutionId: String): MutableList<InstitutionPost> {
        var posts: MutableList<InstitutionPost> = mutableListOf()
        val ref = db.collection("institution_posts")
            .whereEqualTo("institution_ref", db.document("/institutions/$institutionId"))
            .get().await()

        for(doc in ref.documents) {
            var post = doc.toObject(InstitutionPost::class.java)
            if(post != null) {
                post.id = doc.id
                posts.add(post)
            }
        }

        return posts
    }
}