package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.DateUtil
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

object StoryRepository {
    private val TAG = "STORY_REPO"

    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance()

    suspend fun getStoriesFromConnections(userId: String): LinkedHashMap<User, MutableList<Story>> {

        // Group stories based on the user
        val stories: LinkedHashMap<User, MutableList<Story>> = LinkedHashMap<User, MutableList<Story>>()
        val now = DateUtil.convertLocalDateTimetoDate(LocalDateTime.now())

        // Get all connection
        var connections = UserRepository.getConnections(userId)
        val user = UserRepository.getUserByDocument(userId)!!
        connections.add(0, user)
        Log.wtf(TAG, now.toString())

        val currentUser = UserRepository.getCurrentUser()
        if (currentUser != null) {
            stories.put(currentUser, mutableListOf<Story>())
        }

        for(connection in connections) {
            stories.put(connection, mutableListOf<Story>())
            // Retrieve all stories not deleted yet
            val querySnapshot = db.collection("stories")
                .whereEqualTo("user_ref", db.document("/users/" + connection.id))
                .whereGreaterThan("deletedAt", Timestamp(now))
                .orderBy("deletedAt", Query.Direction.DESCENDING)
                .get()
                .await()

            Log.wtf(TAG, connection.username.toString())
            Log.wtf(TAG, querySnapshot.documents.size.toString())


            for(document in querySnapshot.documents) {
                var story = document.toObject(Story::class.java)

                if (story != null) {
                    if(story.media != "") {
                        val httpReference = storageRef.getReferenceFromUrl(story.media)
                        httpReference.metadata.addOnSuccessListener {
                            story.mediaType = it.contentType!!
                        }
                    }
                    stories.get(connection)?.add(story)
                }
            }

            if(stories.get(connection)?.size == 0) {
                // Empty story
                stories.remove(connection)
            }
        }
        return stories
    }

    suspend fun getStoryByUserId(userId: String): MutableList<Story> {
        val stories: MutableList<Story> = mutableListOf()
        val now = DateUtil.convertLocalDateTimetoDate(LocalDateTime.now())

        val querySnapshot = db.collection("stories")
            .whereEqualTo("user_ref", db.document("/users/$userId"))
            .whereGreaterThan("deletedAt", Timestamp(now))
            .orderBy("deletedAt", Query.Direction.DESCENDING)
            .get()
            .await()

        for(document in querySnapshot.documents) {
            var story = document.toObject(Story::class.java)
            Log.wtf(TAG, "Story: " + story.toString())
            if(story != null) {
                if(story.media != "") {
                    val httpReference = storageRef.getReferenceFromUrl(story.media)
                    httpReference.metadata.addOnSuccessListener {
                        Log.wtf(TAG, it.contentType)
                        story.mediaType = it.contentType!!
                    }.await()
                }
                stories.add(story)
            }
        }
        return stories
    }

    suspend fun addStory(story: Story) {
        val ref = db.collection("stories").document()

        val map = hashMapOf(
            "user_ref" to db.document("/users/${story.user.id}"),
            "media" to story.media,
            "createdAt" to story.createdAt,
            "deletedAt" to story.deletedAt
        )

        ref.set(map).await()
    }

}