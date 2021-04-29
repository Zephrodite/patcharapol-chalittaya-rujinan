package com.example.lendyproj

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import kotlinx.android.synthetic.main.fragment_bookshelf.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lendyproj.ui.login.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Exclude
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.view.*
import kotlinx.android.synthetic.main.book_content.view.*
import kotlinx.android.synthetic.main.book_content.view.titleTextView


class BookshelfFragment2 : Fragment()  {

    private val database = Firebase.database
    private lateinit var messagesListener: ValueEventListener
    private val listBooks:MutableList<Book> = ArrayList()
    val myRef = database.getReference("book")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookshelf2, container, false)

//        view.add_book_floating_button.setOnClickListener {
//            val in = Intent(getActivity(), AddBookActivity::class.java)
//            startActivity(in)
//        }

        view.newFloatingActionButton.setOnClickListener { v ->
            val intent = Intent(getActivity(), AddActivity::class.java)
            v.context.startActivity(intent)
        }

        listBooks.clear()
        setupRecyclerView(view.bookRecyclerView)
        setHasOptionsMenu(true)


        return view
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {

        val uid = Firebase.auth.currentUser!!.uid

        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listBooks.clear()


                dataSnapshot.children.forEach { child ->
                    if(child.child("currentUid").getValue(String::class.java) == uid) {
                        val book: Book? =
                            Book(child.child("title").getValue<String>(),
                                child.child("author").getValue<String>(),
                                child.child("description").getValue<String>(),
                                child.child("downloadUri").getValue<String>(),
                                child.child("currentUid").getValue<String>(),
                                child.child("bookId").getValue<String>(),
                                child.child("type").getValue<String>(),
                                child.child("price").getValue<String>(),
                                child.key)
                        book?.let { listBooks.add(it) }

                    }
                }

                recyclerView.adapter = BookshelfFragment.bookViewAdapter(listBooks)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }


        myRef.addValueEventListener(messagesListener)

        deleteSwipe(recyclerView)
    }
    class bookViewAdapter(private val values: List<Book>) :
        RecyclerView.Adapter<bookViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.book_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val book = values[position]
            holder.mTitleTextView.text = book.title
            holder.mAuthorTextView.text = book.author
            holder.mTypeTextView.text = "Type : " + book.type
            holder.mPriceTextView.text = "Price : " + book.price + " ฿"
            holder.mPosterImgeView?.let {
                Glide.with(holder.itemView.context)
                    .load(book.downloadUri)
                    .into(it)
            }

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, BookDetail3::class.java).apply {
                    putExtra("bookId", book.bookId)
                }
                v.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener{ v ->
                val intent = Intent(v.context, EditActivity::class.java).apply {
                    putExtra("bookId", book.bookId)
                }
                v.context.startActivity(intent)
                true
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mTitleTextView: TextView = view.titleTextView
            val mAuthorTextView: TextView = view.authorTextView
            val mPosterImgeView: ImageView? = view.posterImgeView
            val mTypeTextView: TextView = view.typeTextView
            val mPriceTextView: TextView = view.priceTextView
        }
    }

    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listBooks.get(viewHolder.adapterPosition).bookId?.let { myRef.child(it).setValue(null) }
                listBooks.removeAt(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private fun getShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        return shareIntent

    }

    private fun shareInfo() {
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
        if (getShareIntent().resolveActivity(requireActivity().packageManager) == null) {
            menu.findItem(R.id.share).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareInfo()
        }
        return super.onOptionsItemSelected(item)
    }



}