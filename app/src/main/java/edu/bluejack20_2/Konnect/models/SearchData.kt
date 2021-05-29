package edu.bluejack20_2.Konnect.models

import com.google.firebase.firestore.DocumentReference

class SearchData(name: String?, photoUrl: String, doc: DocumentReference) {
    var name: String? = ""
    var photoUrl: String? = ""
    var doc: DocumentReference? = null

    init{
        this.name = name
        this.photoUrl = photoUrl
        this.doc = doc
    }

}