package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.Konnect.models.*
import kotlinx.coroutines.tasks.await

object ActivityPostRepository {

    private var db = FirebaseFirestore.getInstance()
    private val TAG = "ACTIVITY_POST_REPO"
    private val storageRef = FirebaseStorage.getInstance()

    suspend fun getAll(): List<ActivityPost> {
        val expList = mutableListOf<Experience>()
        val list = mutableListOf<ActivityPost>()
        val currUser =
            db.collection("users").document(FirebaseAuth.getInstance().currentUser.uid).get()
                .await()
        val listOfConenctions = mutableListOf<DocumentReference>()
        val dataFromFirestore = currUser["connections_ref"] as MutableList<DocumentReference>?

        listOfConenctions += currUser.reference
        if (dataFromFirestore != null) {
            listOfConenctions += dataFromFirestore
        }

        Log.wtf("list of data", listOfConenctions.toString())

        val data = db.collection("activity_posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .whereIn("user_ref", listOfConenctions)
            .get()
            .await()

        try {
            for (post in data.documents) {

                val userRef = post["user_ref"] as DocumentReference
                val user = getActivityPostUser(userRef)

                val pObj = post.toObject(ActivityPost::class.java)!!
                pObj.id = post.id

                if(pObj.media != "") {
                    val httpReference = storageRef.getReferenceFromUrl(pObj.media)
                    httpReference.metadata.addOnSuccessListener {
                        pObj.mediaType = it.contentType!!
                    }
                }

                var uObj = user.toObject(User::class.java)

                val experiences = mutableListOf<Experience>()
                if(user["experiences_ref"] != null) {
                    val experiencesRef = user["experiences_ref"] as List<DocumentReference>
                    for (experienceRef in experiencesRef) {
                        val experience = ExperienceRepository.getExperienceDocRef(experienceRef)
                        val experienceObj = experience.toObject(Experience::class.java)

                        // Experience's Institution
                        val institutionRef = experience["institution_ref"] as DocumentReference
                        val institution = InstitutionRepository.getInstitutionDocRef(institutionRef)
                        val institutionObj = institution.toObject(Institution::class.java)!!
                        institutionObj.id = institution.id

                        if (experienceObj != null) {
                            experienceObj.id = experience.id
                            experienceObj.institution = institutionObj
                            experiences.add(experienceObj)
                        }
                    }
                }

                if (uObj != null) {
                    uObj.experiences = experiences
                    pObj.user = uObj
                    list.add(pObj)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }

        return list
    }

    suspend fun getActivityPostUser(ref: DocumentReference): DocumentSnapshot {
        val snapshot = ref.get().await()
        return snapshot
    }

    suspend fun addPost(post: ActivityPost, user: User): String {
        val ref = db.collection("activity_posts").document()

        // Send it to firestore here
        val post = hashMapOf(
            "content" to post.content,
            "createdAt" to post.createdAt,
            "media" to post.media,
            "user_ref" to db.document("/users/" + user.id)
        )

        ref.set(post).await()
        return ref.id
    }

    suspend fun getPostByDocument(id: String): ActivityPost? {
        val postRef = db.collection("activity_posts").document(id)
        val query = postRef.get().await()
        val post: ActivityPost
        val comments: MutableList<PostComment> = mutableListOf<PostComment>()
        val likes: MutableList<User> = mutableListOf<User>()

        try {
            // Post
            post = query.toObject(ActivityPost::class.java)!!
            post.id = query.id

            if(post.media != "") {
                val httpReference = storageRef.getReferenceFromUrl(post.media)
                httpReference.metadata.addOnSuccessListener {
                    post.mediaType = it.contentType!!
                }
            }

            // User
            val userRef = query["user_ref"] as DocumentReference
            val user = UserRepository.getUserDocRef(userRef)
            val userObj = user.toObject(User::class.java)!!
            userObj.id = user.id

            // Comments
            if (query["comments_ref"] != null) {
                val commentsRef = query["comments_ref"] as List<DocumentReference>
                for (commentRef in commentsRef) {
                    Log.wtf(TAG, "comments")
                    val comment = PostCommentRepository.getCommentDocRef(commentRef)
                    val commentObj = comment.toObject(PostComment::class.java)

                    // Comment's user
                    val commentUserRef = comment["user_ref"] as DocumentReference
                    val commentUser = UserRepository.getUserDocRef(commentUserRef)
                    val commentUserObj = commentUser.toObject(User::class.java)

                    if (commentObj != null) {
                        if (commentUserObj != null) {
                            commentObj.user = commentUserObj
                        }
                        comments.add(commentObj)
                    }
                }
            }

            // Likes
            if (query["likes_ref"] != null) {
                val likesRef = query["likes_ref"] as List<DocumentReference>
                for (likeRef in likesRef) {
                    Log.wtf(TAG, "likes")
                    val like = PostLikeRepository.getLikeDocRef(likeRef)
                    val likeObj = like.toObject(User::class.java)!!
                    likeObj.id = like.id

                    if (likeObj != null) {
                        likes.add(likeObj)
                    }
                }
            }

            if (userObj != null) {
                post.user = userObj
            }
            if (comments.size > 0) {
                post.comments = comments
            }
            if (likes.size > 0) {
                post.likes = likes
            }
            return post
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return null
    }

    suspend fun dislikePost(postId: String, userId: String) {
        val postRef = db.collection("activity_posts").document(postId)
        postRef.update("likes_ref", FieldValue.arrayRemove(db.document("/users/$userId")))
            .await()
    }

    suspend fun likePost(postId: String, userId: String) {
        val postRef = db.collection("activity_posts").document(postId)
        postRef.update("likes_ref", FieldValue.arrayUnion(db.document("/users/$userId")))
            .await()
    }
}