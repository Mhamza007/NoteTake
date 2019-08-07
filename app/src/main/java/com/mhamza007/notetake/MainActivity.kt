package com.mhamza007.notetake

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Add Dummy data
//        listNotes.add(Note(1, "1st Note", "Description of the text should be displayed here. I am adding text to make it little bit longer"))
//        listNotes.add(Note(2, "2nd Note", "Description of the text should be displayed here. I am adding text to make it little bit longer"))
//        listNotes.add(Note(3, "3rd Note", "Description of the text should be displayed here. I am adding text to make it little bit longer"))
//        listNotes.add(Note(4, "4th Note", "Description of the text should be displayed here. I am adding text to make it little bit longer"))

        //Load Notes from Databse
        loadNotes("%")

    }

    override fun onStart() {
        super.onStart()

        //Load Notes from Databse
        loadNotes("%")
    }

    override fun onResume() {
        super.onResume()

        //Load Notes from Databse
        loadNotes("%")
    }

    fun loadNotes(title: String){
        //Load Notes from Databse
        var dbManager = DbManager(this)
        val projection = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projection, "Title like?", selectionArgs,"Title")
        listNotes.clear()

        if (cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))

            } while (cursor.moveToNext())
        }

        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        lv_notes.adapter = myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                loadNotes("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadNotes("%")
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId){
            R.id.add_note -> {
                //Go to Add Note Activity
                startActivity(Intent(this, AddNotes::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter : BaseAdapter {

        private var context : Context? = null
        var listNotesAdapter = ArrayList<Note>()
        constructor(context: Context, listNotesAdapter : ArrayList<Note>): super(){
            this.context = context
            this.listNotesAdapter = listNotesAdapter
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view = layoutInflater.inflate(R.layout.ticket, null)
            val myNote = listNotesAdapter[position]
            view.tv_title.text = myNote.noteName
            view.tv_desc.text = myNote.noteDes

            //delete note
            view.delete.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadNotes("%")
                Toast.makeText(this.context, "Note deleted", Toast.LENGTH_SHORT).show()
            }

            //update note
            view.edit.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                updateNote(myNote)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }
    }

    fun updateNote(note: Note){

        var intent = Intent(this, AddNotes::class.java)
        intent.putExtra("ID", note.noteID)
        intent.putExtra("Title", note.noteName)
        intent.putExtra("Description", note.noteDes)
        startActivity(intent)
    }
}
