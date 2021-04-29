package com.example.lendyproj

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lendyproj.ui.login.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Exclude
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.book_content.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment()  {

    private val database1 = Firebase.database
    private lateinit var messagesListener1: ValueEventListener
    private val listBooks1:MutableList<Book> = ArrayList()
    val myRef1 = database1.getReference("book")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        view.add_book_floating_button.setOnClickListener {
//            val `in` = Intent(getActivity(), AddBookActivity::class.java)
//            startActivity(`in`)
//        }
        view.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString()!=null) {
                    setupRecyclerView1(view.bookRecyclerView, s.toString())
                } else {
                    setupRecyclerView1(view.bookRecyclerView, "")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })



        listBooks1.clear()
        setupRecyclerView(view.bookRecyclerView)
        setHasOptionsMenu(true)


        return view
    }

    private fun setupRecyclerView1(recyclerView1: RecyclerView, searchTxt: String) {
        val firebaseQuery = myRef1.orderByChild("title").startAt(searchTxt).endAt(searchTxt + "\uf8ff")
        firebaseQuery.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listBooks1.clear()
                snapshot.children.forEach { child ->


                    val book1: Book? =
                        Book(child.child("title").getValue<String>(),
                            child.child("author").getValue<String>(),
                            child.child("description").getValue<String>(),
                            child.child("downloadUri").getValue<String>(),
                            child.child("currentUid").getValue<String>(),
                            child.child("bookId").getValue<String>(),
                            child.child("type").getValue<String>(),
                            child.child("price").getValue<String>(),
                            child.key)
                    book1?.let { listBooks1.add(it) }
                }

                recyclerView1.adapter = bookViewAdapter1(listBooks1)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setupRecyclerView(recyclerView1: RecyclerView) {
        val uid = Firebase.auth.currentUser!!.uid

        messagesListener1 = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listBooks1.clear()
                snapshot.children.forEach { child ->
                    if(child.child("currentUid").getValue(String::class.java) != uid) {
                        val book1: Book? =
                            Book(child.child("title").getValue<String>(),
                                child.child("author").getValue<String>(),
                                child.child("description").getValue<String>(),
                                child.child("downloadUri").getValue<String>(),
                                child.child("currentUid").getValue<String>(),
                                child.child("bookId").getValue<String>(),
                                child.child("type").getValue<String>(),
                                child.child("price").getValue<String>(),
                                child.key)
                        book1?.let { listBooks1.add(it) }
                    }
                    recyclerView1.adapter = HomeFragment.bookViewAdapter1(listBooks1)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef1.addValueEventListener(messagesListener1)
    }
    class bookViewAdapter1(private val values1: List<Book>) :
        RecyclerView.Adapter<bookViewAdapter1.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.book_content, parent, false)

            return ViewHolder(view)
        }

        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: ViewHolder, position1: Int) {
            val book = values1[position1]
            holder.mTitleTextView1.text = book.title
            holder.mAuthorTextView1.text = book.author
            holder.mTypeTextView1!!.text   = book.type
            holder.mPriceTextView1!!.text = book.price
            holder.mPosterImgeView1?.let {
                Glide.with(holder.itemView.context)
                    .load(book.downloadUri)
                    .into(it)
            }

            holder.itemView.setOnClickListener { v ->
                val intent1 = Intent(v.context, BookDetail::class.java).apply {
                    putExtra("bookId", book.bookId)
                    putExtra("userId", book.currentUid)

                }
                v.context.startActivity(intent1)
            }


        }

        override fun getItemCount() = values1.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mTitleTextView1: TextView = view.titleTextView
            val mAuthorTextView1: TextView = view.authorTextView
            val mPosterImgeView1: ImageView? = view.posterImgeView
            val mTypeTextView1: TextView? = view.typeTextView
            val mPriceTextView1: TextView? = view.priceTextView
        }
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

