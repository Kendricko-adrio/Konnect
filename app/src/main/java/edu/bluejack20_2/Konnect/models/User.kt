package edu.bluejack20_2.Konnect.models

data class User(
        var id: String = "",
        var name: String = "",
        var email: String = "",
        var photoUrl: String = "",
        var summary: String = "",
        var city: City = City(),
        var experiences : MutableList<Experience> = mutableListOf<Experience>(),
        var educations : MutableList<Education> = mutableListOf<Education>(),
        var skills : MutableList<Skill> = mutableListOf<Skill>(),
        var knowledges : MutableList<Knowledge> = mutableListOf<Knowledge>(),
        var toolTechs : MutableList<ToolTech> = mutableListOf<ToolTech>(),
        var interpersonalSkills : MutableList<InterpersonalSkill> = mutableListOf<InterpersonalSkill>(),
        var awards : MutableList<Award> = mutableListOf<Award>(),
        var testScores : MutableList<TestScore> = mutableListOf(),
        var languages : MutableList<Language> = mutableListOf(),
        var organizations : MutableList<Organization> = mutableListOf(),
        var connections : MutableList<User> = mutableListOf(),
        var activityPosts : MutableList<ActivityPost> = mutableListOf()
)
