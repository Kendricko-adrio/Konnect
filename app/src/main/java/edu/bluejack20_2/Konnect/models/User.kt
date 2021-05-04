package edu.bluejack20_2.Konnect.models

import com.google.common.collect.ImmutableList

data class User(
        val id: String = "",
        var name: String = "",
        var profilePicture: String = "",
        var summary: String = "",
        var city: City,
        var experiences: ImmutableList<Experience>,
        var educations: ImmutableList<Education>,
        var skills: ImmutableList<Skill>,
        var industryKnowledges: ImmutableList<Knowledge>,
        var toolsTechniques: ImmutableList<ToolTech>,
        var interpersonalSkills: ImmutableList<InterpersonalSkill>,
        var awards: ImmutableList<Award>,
        var testScores: ImmutableList<TestScore>,
        var languages: ImmutableList<Language>,
        var organizations: ImmutableList<Organization>,
        var connections: ImmutableList<User>
)
