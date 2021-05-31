package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.StudyField
import kotlinx.coroutines.tasks.await

object StudyFieldRepository {
    private val TAG = "STUDY_FIELD_REPO"
    private var db = FirebaseFirestore.getInstance()

    suspend fun getStudyFieldDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun getAllFields(): MutableList<StudyField> {
        var fields : MutableList<StudyField> = mutableListOf()
        val ref = db.collection("study_fields").get().await()
        for(doc in ref.documents) {
            var field = doc.toObject(StudyField::class.java)
            if(field != null) {
                field.id = doc.id
                fields.add(field)
            }
        }
        Log.wtf(TAG, fields.toString())
        return fields
    }
}