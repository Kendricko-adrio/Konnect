package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
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

    suspend fun getStoriesFromConnections(userId: String): LinkedHashMap<User, MutableList<Story>> {

        // Group stories based on the user
        val stories: LinkedHashMap<User, MutableList<Story>> = LinkedHashMap<User, MutableList<Story>>()
        val now = DateUtil.convertLocalDateTimetoDate(LocalDateTime.now())

        // Get all connection
        val connections = UserRepository.getConnections(userId)
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
                .get()
                .await()

            for(document in querySnapshot.documents) {
                var story = document.toObject(Story::class.java)
                if (story != null) {
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

}