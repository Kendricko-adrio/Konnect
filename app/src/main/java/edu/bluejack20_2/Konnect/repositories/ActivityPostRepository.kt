package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.PostComment
import edu.bluejack20_2.Konnect.models.User
import kotlinx.coroutines.tasks.await

object ActivityPostRepository {

    private var db = FirebaseFirestore.getInstance()
    private val TAG = "ACTIVITY_POST_REPO"

    suspend fun getAll(): List<ActivityPost> {
        val list = mutableListOf<ActivityPost>()
        val data = db.collection("activity_posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

        try {
            for(post in data.documents) {
                val userRef = post["user_ref"] as DocumentReference
                val user = getActivityPostUser(userRef)

                val pObj = post.toObject(ActivityPost::class.java)!!
                pObj.id = post.id
                var uObj = user.toObject(User::class.java)

                if(pObj != null && uObj != null) {
                    pObj.user = uObj
                    list.add(pObj)
                }
            }

        }catch(e: Exception) {
            Log.d(TAG, e.toString())
        }

        return list
    }

    suspend fun getActivityPostUser(ref: DocumentReference): DocumentSnapshot {
        val snapshot = ref.get().await()
        return snapshot
    }

    suspend fun addPost(post: ActivityPost, user: User) {
        val ref = db.collection("activity_posts").document()

        // Send it to firestore here
        val post = hashMapOf(
            "content" to post.content,
            "createdAt" to post.createdAt,
            "media" to post.media,
            "user_ref" to db.document("/users/" + user.id)
        )

        ref.set(post).await()
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

            // User
            val userRef = query["user_ref"] as DocumentReference
            val user = UserRepository.getUserDocRef(userRef)
            val userObj = user.toObject(User::class.java)!!
            userObj.id = user.id

            // Comments
            if(query["comments_ref"] != null) {
                val commentsRef = query["comments_ref"] as List<DocumentReference>
                for (commentRef in commentsRef) {
                    Log.wtf(TAG, "comments")
                    val comment = PostCommentRepository.getCommentDocRef(commentRef)
                    val commentObj = comment.toObject(PostComment::class.java)

                    // Comment's user
                    val commentUserRef = comment["user_ref"] as DocumentReference
                    val commentUser = UserRepository.getUserDocRef(commentUserRef)
                    val commentUserObj = commentUser.toObject(User::class.java)

                    if(commentObj != null) {
                        if(commentUserObj != null) {
                            commentObj.user = commentUserObj
                        }
                        comments.add(commentObj)
                    }
                }
            }

            // Likes
            if(query["likes_ref"] != null) {
                val likesRef = query["likes_ref"] as List<DocumentReference>
                for (likeRef in likesRef) {
                    Log.wtf(TAG, "likes")
                    val like = PostLikeRepository.getLikeDocRef(likeRef)
                    val likeObj = like.toObject(User::class.java)!!
                    likeObj.id = like.id

                    if(likeObj != null) {
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
        }catch(e: Exception) {
            Log.d(TAG, e.toString())
        }
        return null
    }
}