package com.example.lendyproj

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.bumptech.glide.Glide
import com.example.lendyproj.ui.login.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.book_content.view.*
import kotlinx.android.synthetic.main.fragment_bookshelf.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile2.*
import kotlinx.android.synthetic.main.fragment_profile2.view.*

@Suppress("UNREACHABLE_CODE")
class ProfileFragment2 : Fragment() {
    lateinit var aboutFragment: AboutFragment
    private lateinit var mAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var firebaseAuth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
    val listProfiles:MutableList<UserId> = ArrayList()
    lateinit var auth: FirebaseAuth
    var adapter: ArrayAdapter<String>? = null
    val itemlist = ArrayList<String>()
//    val userid = user?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile2, container, false)



        firebaseAuth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().getReference("uid")



        view.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(requireActivity(), PreLoginActivity::class.java))
        }

        val uid2 = Firebase.auth.currentUser!!.uid
        val rootRef = FirebaseDatabase.getInstance().getReference("uid")
        val usersRef = rootRef.child(uid2)
        val usernameRef = usersRef.child("username")
        val imageRef = usersRef.child("profileImageUri")
        val currentUserid = Firebase.auth.currentUser!!.uid
        view.email_txt_2.text  =Firebase.auth.currentUser!!.email

        view.watchlist_button1.setOnClickListener {
            val `in` = Intent(getActivity(), WatchListActivity::class.java)
            startActivity(`in`)

        }

        view.settingProfileButton.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfile::class.java)
            intent.putExtra("currentUserid", currentUserid)
            requireActivity()?.startActivity(intent)
        }

        usersRef.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("profileImageUri").getValue(String::class.java).toString() != "No image") {
                    images(snapshot.child("profileImageUri").getValue(String::class.java).toString())
                }
                val usernametxt = snapshot.child("username").getValue(String::class.java)
                view.name_txt_2.text = usernametxt.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        setHasOptionsMenu(true)

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aboutFragment -> {
                val `in` = Intent(getActivity(), OptionsActivity::class.java)
                startActivity(`in`)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun images(url: String) {
        Glide.with(this)
            .load(url)
            .into(profile_image)
    }

}