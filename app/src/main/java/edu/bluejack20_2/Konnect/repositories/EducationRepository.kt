package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.*
import kotlinx.coroutines.tasks.await

object EducationRepository {
    private var db = FirebaseFirestore.getInstance()
    val TAG = "EDUCATION_REPO"

    suspend fun getEducationDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun getEducationById(id: String): Education {
        val educationRef = db.collection("educations").document(id)
        val query = educationRef.get().await()
        val education: Education

        try {
            education = query.toObject(Education::class.java)!!
            education.id = query.id

            // Institution
            val institution = ExperienceRepository.loadInstitution(query)

            // Education degree
            val deg = loadDegree(query)

            // Study field
            val field = loadField(query)

            education.institution = institution
            education.educationDegree = deg
            education.studyField = field

            return education

        }catch(e: Exception) {
            Log.d(TAG, e.toString())
        }
        return Education()
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

    private suspend fun loadDegree(query: DocumentSnapshot): EducationDegree {
        if(query["education_degree_ref"] == null) {
            return EducationDegree()
        }

        val ref = query["education_degree_ref"] as DocumentReference
        val entity = EducationDegreeRepository.getEducationDegreeDocRef(ref)
        val obj = entity.toObject(EducationDegree::class.java)!!
        obj.id = entity.id

        return obj
    }

    private suspend fun loadField(query: DocumentSnapshot): StudyField {
        if(query["study_field_ref"] == null) {
            return StudyField()
        }

        val ref = query["study_field_ref"] as DocumentReference
        val entity = StudyFieldRepository.getStudyFieldDocRef(ref)
        val obj = entity.toObject(StudyField::class.java)!!
        obj.id = entity.id

        return obj
    }

    suspend fun updateEducation(education: Education) {
        db.collection("educations").document(education.id).update(
            "institution_ref", db.document("/institutions/" + education.institution.id),
            "education_degree_ref", db.document("/education_degrees/" + education.educationDegree.id),
            "study_field_ref", db.document("/study_fields/" + education.studyField.id),
            "startDate", education.startDate,
            "endDate", education.endDate,
            "description", education.description
        ).await()
    }

    suspend fun addEducation(education: Education, userId: String) {
        val educationRef = db.collection("educations").document()

        val eduMap = hashMapOf(
            "institution_ref" to db.document("/institutions/" + education.institution.id),
            "study_field_ref" to db.document("/study_fields/" + education.studyField.id),
            "education_degree_ref" to db.document("/education_degrees/" + education.educationDegree.id),
            "startDate" to education.startDate,
            "endDate" to education.endDate,
            "description" to education.description
        )

        educationRef.set(eduMap).await()

        val userRef = db.collection("users").document(userId)

        userRef.update("educations_ref", FieldValue.arrayUnion(db.document("/educations/${educationRef.id}"))).await()

    }

}