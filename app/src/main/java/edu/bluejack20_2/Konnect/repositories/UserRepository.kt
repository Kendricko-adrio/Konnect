package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.firestore.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.models.*
import kotlinx.coroutines.tasks.await

object UserRepository {

    private const val TAG = "USER_REPO"

    private val db = FirebaseFirestore.getInstance()

    fun getUserByDoc(user: String): DocumentReference {
        return db.collection("users").document(user)
    }

    fun setUserBlock(user: DocumentReference){
        val fbUser = FirebaseAuth.getInstance().currentUser.uid
        db.collection("users").document(fbUser).update(
            hashMapOf<String, Any>(
                "block_user_ref" to FieldValue.arrayUnion(user)
            )
        ).addOnCompleteListener {

        }

        unfriendConnection(fbUser, user)

    }

    fun getUserByUsernameAndPassword(email: String, password: String): Query{
        return db.collection("users").whereEqualTo("email", email).whereEqualTo("password", password)
    }

    fun acceptConnection(user: String, userDoc: DocumentReference) {
        deleteDocFromInbound(user, userDoc)
        deleteDocFromOutbound(user, userDoc)
        addToConnections(user, userDoc)
    }

    fun declineConnection(user: String, userDoc: DocumentReference) {
        deleteDocFromInbound(user, userDoc)
        deleteDocFromOutbound(user, userDoc)
    }

    fun deleteDocFromInbound(user: String, deleteDoc: DocumentReference) {
        db.collection("users").document(user).update(
            hashMapOf<String, Any>(
                "inbound_request" to FieldValue.arrayRemove(deleteDoc)
            )
        ).addOnCompleteListener {
            Log.wtf("berhasil ga?", "berhasil gan menghapus data di array")
        }
    }

    fun unfriendConnection(userCurr: String, userDoc: DocumentReference) {
        val curr = db.collection("users").document(userCurr)
        db.collection("users").document(userCurr).update(
            hashMapOf<String, Any>(
                "connections_ref" to FieldValue.arrayRemove(userDoc)
            )
        ).addOnCompleteListener {
            Log.wtf("tag", "data masuk sih")
        }
        db.collection("users").document(userDoc.id).update(
            hashMapOf<String, Any>(
                "connections_ref" to FieldValue.arrayRemove(curr)
            )
        ).addOnCompleteListener {
        }
    }

    fun deleteDocFromOutbound(userDelete: String, outboundUser: DocumentReference) {
        val userToDelete = db.collection("users").document(userDelete)
        db.collection("users").document(outboundUser.id).update(
            hashMapOf<String, Any>(
                "outbound_request" to FieldValue.arrayRemove(userToDelete)
            )
        ).addOnCompleteListener {
            Log.wtf("berhasil ga?", "berhasil gan menghapus data di array")
        }
    }

    fun addToConnections(userCurr: String, userOutbound: DocumentReference) {
        val userCurrent = db.collection("users").document(userCurr)
        db.collection("users").document(userCurr).update(
            hashMapOf<String, Any>(
                "connections_ref" to FieldValue.arrayUnion(userOutbound)
            )
        )

        db.collection("users").document(userOutbound.id).update(
            hashMapOf<String, Any>(
                "connections_ref" to FieldValue.arrayUnion(userCurrent)
            )
        )
    }


    fun searchByName(name: String): Query{
        return db.collection("users").whereGreaterThanOrEqualTo("name", name).whereLessThan("name",
            "$name\uf8ff")
    }

    fun searchAllUser(): Query{
        return db.collection("users")
    }

    fun createUserFirebase(user: FirebaseUser) {
        val findDoc = db.collection("users").document(user.uid)
        findDoc.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    // Create new user
                    val data = hashMapOf(
                        "name" to user.displayName,
                        "email" to user.email,
                        "phoneNumber" to user.phoneNumber,
                        "providerId" to user.providerId,
                        "photoUrl" to user.photoUrl.toString()
                    )
                    db.collection("users").document(user.uid).set(data)
                } else {
                    // User already exists
                    Log.wtf("test", "DocumentSnapshot data: ${document.get("name")}")
                }
            }

    }

    suspend fun getCurrentUser(): User? {
        // Get the current user authentication
        val email = FirebaseAuth.getInstance().currentUser?.email

        if (email != null) {
            val userRef = db.collection("users")
            val query = userRef.whereEqualTo("email", email).get().await()
            val user: User
            try {
                user = query.documents.first().toObject(User::class.java)!!
                user.id = query.documents.first().id
                return user
            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }
        return null
    }

    suspend fun getUserDocRef(ref: DocumentReference): DocumentSnapshot {
        return ref.get().await()
    }

    suspend fun getUserByDocument(id: String): User? {
        // We need to load several references from the user document
        // City : Reference
        // Experiences : Array<Reference>

        val userRef = db.collection("users").document(id)
        val query = userRef.get().await()
        val user: User
        val experiences: MutableList<Experience> = mutableListOf<Experience>()
        val educations: MutableList<Education> = mutableListOf<Education>()
        val skills: MutableList<Skill> = mutableListOf<Skill>()

        try {
            // User
            user = query.toObject(User::class.java)!!

            // City
            val cityRef = query["city_ref"] as DocumentReference
            val city = CityRepository.getCityDocRef(cityRef)
            val cityObj = city.toObject(City::class.java)

            // Experiences
            val experiencesRef = query["experiences_ref"] as List<DocumentReference>
            for (experienceRef in experiencesRef) {
                val experience = ExperienceRepository.getExperienceDocRef(experienceRef)
                val experienceObj = experience.toObject(Experience::class.java)

                // Experience's Institution
                val institutionRef = experience["institution_ref"] as DocumentReference
                val institution = InstitutionRepository.getInstitutionDocRef(institutionRef)
                val institutionObj = institution.toObject(Institution::class.java)

                // Experience's Employment Type
                val employmentTypeRef = experience["employment_type_ref"] as DocumentReference
                val employmentType =
                    EmploymentTypeRepository.getEmploymentTypeDocRef(employmentTypeRef)
                val employmentTypeObj = employmentType.toObject(EmploymentType::class.java)

                if (experienceObj != null) {
                    if (institutionObj != null) {
                        experienceObj.institution = institutionObj
                    }
                    if (employmentTypeObj != null) {
                        experienceObj.employmentType = employmentTypeObj
                    }
                    experiences.add(experienceObj)
                }
            }

            // Educations
            val educationsRef = query["educations_ref"] as List<DocumentReference>
            for (educationRef in educationsRef) {
                val education = EducationRepository.getEducationDocRef(educationRef)
                val educationObj = education.toObject(Education::class.java)

                // Education's Institution
                val institutionRef = education["institution_ref"] as DocumentReference
                val institution = InstitutionRepository.getInstitutionDocRef(institutionRef)
                val institutionObj = institution.toObject(Institution::class.java)

                // Education's Degree
                val educationDegreeRef = education["education_degree_ref"] as DocumentReference
                val educationDegree =
                    EducationDegreeRepository.getEducationDegreeDocRef(educationDegreeRef)
                val educationDegreeObj = educationDegree.toObject(EducationDegree::class.java)

                // Study Field
                val studyFieldRef = education["study_field_ref"] as DocumentReference
                Log.wtf(TAG, studyFieldRef.toString())
                val studyField = StudyFieldRepository.getStudyFieldDocRef(studyFieldRef)
                Log.wtf(TAG, studyField.toString())
                val studyFieldObj = studyField.toObject(StudyField::class.java)
                Log.wtf(TAG, studyFieldObj.toString())

                if (educationObj != null) {
                    if (institutionObj != null) {
                        educationObj.institution = institutionObj
                    }
                    if (educationDegreeObj != null) {
                        educationObj.educationDegree = educationDegreeObj
                    }
                    if (studyFieldObj != null) {
                        educationObj.studyField = studyFieldObj
                    }
                    educations.add(educationObj)
                }
            }

            // Skills
            val skillsRef = query["skills_ref"] as List<DocumentReference>
            for (skillRef in skillsRef) {
                val skill = SkillRepository.getSkillDocRef(skillRef)
                val skillObj = skill.toObject(Skill::class.java)

                if (skillObj != null) {
                    skills.add(skillObj)
                }
            }

            // Add all retrieved attributes to the user object
            if (cityObj != null) {
                user.city = cityObj
            }
            user.experiences = experiences
            user.educations = educations
            user.skills = skills

            return user
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return null
    }
}

