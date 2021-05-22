package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class User(
        var id: String = "UserID",
        var name: String = "User Name",
        var email: String = "User Email",
        var photoUrl: String = "https://firebasestorage.googleapis.com/v0/b/konnect-867f9.appspot.com/o/avatar.jpg?alt=media&token=5aacd675-2d73-438c-93e3-418e37c20a8a",
        var summary: String = "Summary...",
        var dob: Timestamp = Timestamp.now(),
        var city: City = City(),
        var experiences: MutableList<Experience> = mutableListOf<Experience>(),
        var educations: MutableList<Education> = mutableListOf<Education>(),
        var skills: MutableList<Skill> = mutableListOf<Skill>(),
        var knowledges: MutableList<Knowledge> = mutableListOf<Knowledge>(),
        var toolTechs: MutableList<ToolTech> = mutableListOf<ToolTech>(),
        var interpersonalSkills: MutableList<InterpersonalSkill> = mutableListOf<InterpersonalSkill>(),
        var awards: MutableList<Award> = mutableListOf<Award>(),
        var testScores: MutableList<TestScore> = mutableListOf(),
        var languages: MutableList<Language> = mutableListOf(),
        var organizations: MutableList<Organization> = mutableListOf(),
        var connections: MutableList<User> = mutableListOf(),
        var activityPosts: MutableList<ActivityPost> = mutableListOf()
)
