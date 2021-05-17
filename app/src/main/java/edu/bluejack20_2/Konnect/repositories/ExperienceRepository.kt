package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.Experience
import kotlinx.coroutines.tasks.await

object ExperienceRepository {

    private var db = FirebaseFirestore.getInstance()
    val TAG = "EXPERIENCE_REPO"

    suspend fun getExperienceDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }
}