package com.example.lendyproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lendyproj.ui.login.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_watch_list.*
import kotlinx.android.synthetic.main.book_content.view.*
import kotlinx.android.synthetic.main.fragment_bookshelf.view.*

class WatchListActivity : AppCompatActivity() {

    private val database = Firebase.database
    private lateinit var messagesListener: ValueEventListener
    private val listBooks:MutableList<WatchList> = ArrayList()
    val myRef = database.getReference("watchlist")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_list)

        listBooks.clear()
        setupRecyclerView(bookRecyclerView)

    }
    private fun setupRecyclerView(recyclerView: RecyclerView) {

        val uid = Firebase.auth.currentUser!!.uid

        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listBooks.clear()
                dataSnapshot.children.forEach { child ->
                    if(child.child("currentUid").getValue(String::class.java) == uid) {
                        val watchlist: WatchList? =
                            WatchList(child.child("title").getValue<String>(),
                                child.child("date").getValue<String>(),
                                child.child("description").getValue<String>(),
                                child.child("downloadUri").getValue<String>(),
                                child.child("currentUid").getValue<String>(),
                                child.child("watchListId").getValue<String>(),
                                child.key)
                        watchlist?.let { listBooks.add(it) }

                    }
                }

                recyclerView.adapter = watchlistViewAdapter(listBooks)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }


        myRef.addValueEventListener(messagesListener)

        deleteSwipe(recyclerView)
    }
    class watchlistViewAdapter(private val values: List<WatchList>) :
        RecyclerView.Adapter<watchlistViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.watchlist_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val book = values[position]
            holder.mTitleTextView.text = book.title
            holder.mDateTextView.text = book.date
            holder.mPosterImgeView?.let {
                Glide.with(holder.itemView.context)
                    .load(book.downloadUri)
                    .into(it)
            }

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, BookDetail2::class.java).apply {
                    putExtra("watchListId", book.watchListId)
                }
                v.context.startActivity(intent)
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mTitleTextView: TextView = view.titleTextView
            val mDateTextView: TextView = view.dateTextView
            val mPosterImgeView: ImageView? = view.posterImgeView
        }
    }

    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listBooks.get(viewHolder.adapterPosition).watchListId?.let { myRef.child(it).setValue(null) }
                listBooks.removeAt(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}