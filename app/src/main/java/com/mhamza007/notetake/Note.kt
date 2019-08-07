package com.mhamza007.notetake

class Note {

    var noteID : Int? = null
    var noteName : String? = null
    var noteDes : String? = null

    constructor(nodeID: Int, nodeName: String, nodeDes: String) {
        this.noteID = nodeID
        this.noteName = nodeName
        this.noteDes = nodeDes
    }
}