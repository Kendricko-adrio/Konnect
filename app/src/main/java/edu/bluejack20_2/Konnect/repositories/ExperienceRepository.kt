package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.EmploymentType
import edu.bluejack20_2.Konnect.models.Experience
import edu.bluejack20_2.Konnect.models.Institution
import kotlinx.coroutines.tasks.await

object ExperienceRepository {

    private var db = FirebaseFirestore.getInstance()
    val TAG = "EXPERIENCE_REPO"

    suspend fun getExperienceDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun getAllEmploymentType(): MutableList<EmploymentType> {
        var employmentTypes: MutableList<EmploymentType> = mutableListOf<EmploymentType>()
        val ref = db.collection("employment_types").get().await()
        for(doc in ref.documents) {
            var employmentType = doc.toObject(EmploymentType::class.java)
            if (employmentType != null) {
                employmentType.id = doc.id
                employmentTypes.add(employmentType)
            }
        }
        return employmentTypes
    }

    suspend fun getExperienceById(id: String): Experience {
        val experienceRef = db.collection("experiences").document(id)
        val query = experienceRef.get().await()
        val experience: Experience

        try {
            experience = query.toObject(Experience::class.java)!!
            experience.id = query.id

            // Institution
            val institution = loadInstitution(query)

            // Employment Type
            val type = loadEmploymentType(query)

            experience.institution = institution
            experience.employmentType = type

            return experience

        }catch(e: Exception) {
            Log.d(TAG, e.toString())
        }
        return Experience()
    }

    suspend fun loadInstitution(query: DocumentSnapshot): Institution {
        if(query["institution_ref"] == null) {
            return Institution()
        }

        val institutionRef = query["institution_ref"] as DocumentReference
        val institution = InstitutionRepository.getInstitutionDocRef(institutionRef)
        val institutionObj = institution.toObject(Institution::class.java)!!
        institutionObj.id = institution.id

        return institutionObj
    }

    suspend fun loadEmploymentType(query: DocumentSnapshot): EmploymentType {
        if(query["employment_type_ref"] == null) {
            return EmploymentType()
        }

        val typeRef = query["employment_type_ref"] as DocumentReference
        val type = EmploymentTypeRepository.getEmploymentTypeDocRef(typeRef)
        val typeObj = type.toObject(EmploymentType::class.java)!!
        typeObj.id = type.id

        return typeObj
    }

    suspend fun updateExperience(experience: Experience) {
        db.collection("experiences").document(experience.id).update(
            "title", experience.title,
            "institution_ref", db.document("/institutions/" + experience.institution.id),
            "employment_type_ref", db.document("/employment_types/" + experience.employmentType.id),
            "startDate", experience.startDate,
            "working", experience.isWorking,
            "endDate", experience.endDate,
            "description", experience.description
        ).await()
    }

    suspend fun addExperience(experience: Experience, userId: String) {
        val experienceRef = db.collection("experiences").document()

        val expMap = hashMapOf(
            "title" to experience.title,
            "institution_ref" to db.document("/institutions/" + experience.institution.id),
            "employment_type_ref" to db.document("/employment_types/" + experience.employmentType.id),
            "startDate" to experience.startDate,
            "endDate" to experience.endDate,
            "working" to experience.isWorking,
            "description" to experience.description
        )

        experienceRef.set(expMap).await()

        val userRef = db.collection("users").document(userId)

        userRef.update("experiences_ref", FieldValue.arrayUnion(db.document("/experiences/${experienceRef.id}"))).await()
    }
}