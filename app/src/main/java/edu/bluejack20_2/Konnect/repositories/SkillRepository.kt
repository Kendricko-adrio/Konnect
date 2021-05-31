package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.Skill
import kotlinx.coroutines.tasks.await

object SkillRepository {
    private var db = FirebaseFirestore.getInstance()
    val TAG = "SKILL_REPO"

    suspend fun getSkillDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun addSkill(skill: Skill, userId: String) {
        Log.wtf(TAG, skill.toString() + " " + userId)
        val skillRef = db.collection("skills").document()

        val skillMap = hashMapOf(
            "name" to skill.name
        )
        skillRef.set(skillMap).await()

        val userRef = db.collection("users").document(userId)

        userRef.update("skills_ref", FieldValue.arrayUnion(db.document("/skills/${skillRef.id}"))).await()
    }

    suspend fun deleteSkill(skillId: String, userId: String) {
        val userRef = db.collection("users").document(userId)

        userRef.update("skills_ref", FieldValue.arrayRemove(db.document("/skills/${skillId}"))).await()

        db.collection("skills").document(skillId).delete().await()
    }

}