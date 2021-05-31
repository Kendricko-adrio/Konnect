package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.EducationDegree
import kotlinx.coroutines.tasks.await

object EducationDegreeRepository {
    private val TAG = "EDUCATION_DEG_REPO"
    private var db = FirebaseFirestore.getInstance()

    suspend fun getEducationDegreeDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun getAllEducationDegrees(): MutableList<EducationDegree> {
        var degs: MutableList<EducationDegree> = mutableListOf()
        val ref = db.collection("education_degrees").get().await()
        for(doc in ref.documents) {
            var deg = doc.toObject(EducationDegree::class.java)
            if(deg != null) {
                deg.id = doc.id
                degs.add(deg)
            }
        }
        Log.wtf(TAG, degs.toString())
        return degs
    }
}