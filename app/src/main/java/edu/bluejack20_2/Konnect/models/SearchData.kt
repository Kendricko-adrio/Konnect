package edu.bluejack20_2.Konnect.models

import com.google.firebase.firestore.DocumentReference

class SearchData(name: String?, photoUrl: String, doc: DocumentReference, type: Int) {
    var name: String? = ""
    var photoUrl: String? = ""
    var doc: DocumentReference? = null
    var type: Int? = null

    init{
        this.name = name
        this.photoUrl = photoUrl
        this.doc = doc
        this.type = type
    }

}