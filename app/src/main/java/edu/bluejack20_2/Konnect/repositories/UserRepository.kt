package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
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


    suspend fun getAll(): MutableList<User> {
        var users = mutableListOf<User>()
        var usersRef = db.collection("users").get().await()
        for(userRef in usersRef) {
            var u = userRef.toObject(User::class.java)
            u.id = userRef.id
            users.add(u)
        }
        return users
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
                }
            }

    }

    suspend fun getCurrentUser(): User? {
        var connections: MutableList<User> = mutableListOf<User>()
        // Get the current user authentication
        val email = FirebaseAuth.getInstance().currentUser?.email

        if (email != null) {
            val userRef = db.collection("users")
            val query = userRef.whereEqualTo("email", email).get().await()
            val user: User
            try {
                user = query.documents.first().toObject(User::class.java)!!
                user.id = query.documents.first().id
                connections = loadConnections(query.documents.first())
                user.connections = connections
//                user.block_user_ref = query.documents[0]["block_user_ref"] as MutableList<DocumentReference>


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

    fun getUserDocumentReference(ref: DocumentReference): Task<DocumentSnapshot> {
        return ref.get()
    }

    suspend fun getUserByDocument(id: String): User? {
        // We need to load several references from the user document
        // City : Reference
        // Experiences : Array<Reference>

        val userRef = db.collection("users").document(id)
        val query = userRef.get().await()
        val user: User
        var experiences: MutableList<Experience> = mutableListOf<Experience>()
        var educations: MutableList<Education> = mutableListOf<Education>()
        var skills: MutableList<Skill> = mutableListOf<Skill>()
        var connections: MutableList<User> = mutableListOf<User>()
        var inboundRequests: MutableList<User> = mutableListOf<User>()
        var outboundRequests: MutableList<User> = mutableListOf<User>()

        try {
            // User
            user = query.toObject(User::class.java)!!
            user.id = query.id

            // City
            val cityObj = loadUserCity(query)

            // Experiences
            experiences = loadUserExperiences(query)

            // Educations
            educations = loadUserEducations(query)

            // Skills
            skills = loadUserSkills(query)

            // Connections
            connections = loadConnections(query)

            // Request
            inboundRequests = loadInboundRequest(query)
            outboundRequests = loadOutboundRequest(query)


            // Add all retrieved attributes to the user object
            if (cityObj != null) {
                user.city = cityObj
            }
            user.experiences = experiences
            user.educations = educations
            user.skills = skills
            user.connections = connections
            user.inbound = inboundRequests
            user.outbound = outboundRequests

            return user
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return null
    }

    fun getUserNotification(id: String): Query {
        return db.collection("notifications")
            .whereEqualTo("user_ref", db.document("/users/$id"))
            .orderBy("createdAt", Query.Direction.DESCENDING)
    }

    suspend fun getConnections(id: String): MutableList<User> {
        val userRef = db.collection("users").document(id)
        val query = userRef.get().await()
        val user: User
        var connections: MutableList<User>

        try {
            connections = loadConnectionsDetail(query)

            return connections

        }catch(e: Exception) {
            Log.d(TAG, e.toString())
        }
        return mutableListOf()
    }

    suspend fun loadUserExperiences(query: DocumentSnapshot): MutableList<Experience> {
        val experiences = mutableListOf<Experience>()
        if(query["experiences_ref"] == null) {
            return experiences
        }
        val experiencesRef = query["experiences_ref"] as List<DocumentReference>
        for (experienceRef in experiencesRef) {
            val experience = ExperienceRepository.getExperienceDocRef(experienceRef)
            val experienceObj = experience.toObject(Experience::class.java)

            // Experience's Institution
            val institutionRef = experience["institution_ref"] as DocumentReference
            val institution = InstitutionRepository.getInstitutionDocRef(institutionRef)
            val institutionObj = institution.toObject(Institution::class.java)!!
            institutionObj.id = institution.id

            // Experience's Employment Type
            val employmentTypeRef = experience["employment_type_ref"] as DocumentReference
            val employmentType =
                EmploymentTypeRepository.getEmploymentTypeDocRef(employmentTypeRef)
            val employmentTypeObj = employmentType.toObject(EmploymentType::class.java)

            if (experienceObj != null) {
                experienceObj.id = experience.id
                if (institutionObj != null) {
                    experienceObj.institution = institutionObj
                }
                if (employmentTypeObj != null) {
                    experienceObj.employmentType = employmentTypeObj
                }
                experiences.add(experienceObj)
            }
        }
        return experiences
    }

    suspend fun loadUserEducations(query: DocumentSnapshot): MutableList<Education> {
        val educations = mutableListOf<Education>()
        if(query["educations_ref"] == null) {
            return educations
        }
        val educationsRef = query["educations_ref"] as List<DocumentReference>
        for (educationRef in educationsRef) {
            val education = EducationRepository.getEducationDocRef(educationRef)
            val educationObj = education.toObject(Education::class.java)

            // Education's Institution
            val institutionRef = education["institution_ref"] as DocumentReference
            val institution = InstitutionRepository.getInstitutionDocRef(institutionRef)
            val institutionObj = institution.toObject(Institution::class.java)!!
            institutionObj.id = institution.id

            // Education's Degree
            val educationDegreeRef = education["education_degree_ref"] as DocumentReference
            val educationDegree =
                EducationDegreeRepository.getEducationDegreeDocRef(educationDegreeRef)
            val educationDegreeObj = educationDegree.toObject(EducationDegree::class.java)

            // Study Field
            val studyFieldRef = education["study_field_ref"] as DocumentReference
            val studyField = StudyFieldRepository.getStudyFieldDocRef(studyFieldRef)
            val studyFieldObj = studyField.toObject(StudyField::class.java)

            if (educationObj != null) {
                educationObj.id = education.id
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
        return educations
    }

    suspend fun loadUserSkills(query: DocumentSnapshot): MutableList<Skill> {

        val skills = mutableListOf<Skill>()
        if(query["skills_ref"] == null) {
            return skills
        }
        val skillsRef = query["skills_ref"] as List<DocumentReference>
        for (skillRef in skillsRef) {
            val skill = SkillRepository.getSkillDocRef(skillRef)
            val skillObj = skill.toObject(Skill::class.java)

            if (skillObj != null) {
                skillObj.id = skill.id
                skills.add(skillObj)
            }
        }
        return skills
    }

    suspend fun loadUserCity(query: DocumentSnapshot): City {
        if(query["city_ref"] == null) {
            return City()
        }
        val cityRef = query["city_ref"] as DocumentReference
        val city = CityRepository.getCityDocRef(cityRef)
        val cityObj = city.toObject(City::class.java)!!
        return cityObj
    }

    suspend fun loadConnections(query: DocumentSnapshot): MutableList<User> {
        val connections = mutableListOf<User>()
        if(query["connections_ref"] == null) {
            return connections
        }
        val connectionsRef = query["connections_ref"] as List<DocumentReference>
        for (connectionRef in connectionsRef) {
            val connection = getUserDocRef(connectionRef)
            val connectionObj = connection.toObject(User::class.java)

            if(connectionObj != null) {
                connectionObj.id = connectionRef.id
                connections.add(connectionObj)
            }
        }
        return connections
    }

    suspend fun loadConnectionsDetail(query: DocumentSnapshot): MutableList<User> {
        val connections = mutableListOf<User>()
        var city = City()
        if(query["connections_ref"] == null) {
            return connections
        }
        val connectionsRef = query["connections_ref"] as List<DocumentReference>
        var experiences = mutableListOf<Experience>()
        for (connectionRef in connectionsRef) {
            val connection = getUserDocRef(connectionRef)
            val connectionObj = connection.toObject(User::class.java)

            experiences = loadUserExperiences(connection)

            city = loadUserCity(connection)

            if(connectionObj != null) {
                connectionObj.experiences = experiences
                connectionObj.city = city
                connectionObj.id = connectionRef.id
                connections.add(connectionObj)
            }
        }
        return connections
    }

    suspend fun loadInboundRequest(query: DocumentSnapshot): MutableList<User> {
        val requests = mutableListOf<User>()
        if(query["inbound_request"] == null) {
            return requests
        }
        val requestsRef = query["inbound_request"] as List<DocumentReference>
        for (requestRef in requestsRef) {
            val request = getUserDocRef(requestRef)
            val requestObj = request.toObject(User::class.java)
            if(requestObj != null) {
                requestObj.id = request.id
                requests.add(requestObj)
            }
        }
        return requests
    }

    suspend fun loadOutboundRequest(query: DocumentSnapshot): MutableList<User> {
        val requests = mutableListOf<User>()
        val ref = query["outbound_request"] ?: return requests
        val requestsRef = ref as List<DocumentReference>
        for (requestRef in requestsRef) {
            val request = getUserDocRef(requestRef)
            val requestObj = request.toObject(User::class.java)

            if(requestObj != null) {
                requestObj.id = request.id
                requests.add(requestObj)
            }
        }
        return requests
    }

    suspend fun addFriend(requesterId: String, requestedId: String) {
        val requesterRef = db.collection("users").document(requesterId)
        val requestedRef = db.collection("users").document(requestedId)

        requesterRef.update("outbound_request", FieldValue.arrayUnion(db.document("/users/$requestedId"))).await()
        requestedRef.update("inbound_request", FieldValue.arrayUnion(db.document("/users/$requesterId"))).await()
    }

    suspend fun acceptFriend(requesterId: String, requestedId: String) {
        val requesterRef = db.collection("users").document(requesterId)
        val requestedRef = db.collection("users").document(requestedId)

        requesterRef.update("outbound_request", FieldValue.arrayRemove(db.document("/users/$requestedId"))).await()
        requestedRef.update("inbound_request", FieldValue.arrayRemove(db.document("/users/$requesterId"))).await()

        requestedRef.update("connections_ref", FieldValue.arrayUnion(db.document("/users/$requesterId"))).await()
        requesterRef.update("connections_ref", FieldValue.arrayUnion(db.document("/users/$requestedId"))).await()
    }

    suspend fun declineFriend(requesterId: String, requestedId: String) {
        val requesterRef = db.collection("users").document(requesterId)
        val requestedRef = db.collection("users").document(requestedId)

        requesterRef.update("outbound_request", FieldValue.arrayRemove(db.document("/users/$requestedId"))).await()
        requestedRef.update("inbound_request", FieldValue.arrayRemove(db.document("/users/$requesterId"))).await()
    }

    suspend fun updateUserProfile(user: User) {

        db.collection("users").document(user.id).update(
            "name", user.name,
            "photoUrl", user.photoUrl,
            "dob", user.dob,
            "summary", user.summary
        ).await()
    }
}

