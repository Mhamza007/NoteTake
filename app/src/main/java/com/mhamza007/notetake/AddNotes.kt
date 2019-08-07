package com.mhamza007.notetake

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {

    val dbTable = "Notes"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            var bundle : Bundle = intent.extras
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                et_title.setText(bundle.getString("Title"))
                et_des.setText(bundle.getString("Description"))
            }
        } catch (ex : Exception){
            ex.printStackTrace()
        }
    }

    fun addNote(view : View){
        var dbManager = DbManager(this)

        var value = ContentValues()
        value.put("Title", et_title.text.toString())
        value.put("Description", et_des.text.toString())

        if (id == 0) { //add note

            val ID = dbManager.insert(value)

            if (ID!! > 0) {
                Toast.makeText(this, "New Note Added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Can't Add New Note", Toast.LENGTH_SHORT).show()
            }
        } else { //update note

            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(value, "ID=?", selectionArgs)

            if (ID!! > 0) {
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Can't Update Note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
