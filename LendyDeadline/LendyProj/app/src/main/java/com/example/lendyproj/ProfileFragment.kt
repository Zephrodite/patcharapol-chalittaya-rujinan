package com.example.lendyproj

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {
    lateinit var aboutFragment: AboutFragment
    private lateinit var mAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        view.name_txt.text = "Name: " + currentUser.displayName
        view.email_txt.text = "Email: " + currentUser.email

        Glide.with(this).load(currentUser?.photoUrl).into(view.profile_image)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        view.watchlist_button.setOnClickListener {
            val `in` = Intent(getActivity(), WatchListActivity::class.java)
            startActivity(`in`)

        }


        view.sign_out_btn.setOnClickListener {
            mAuth = FirebaseAuth.getInstance()
            Firebase.auth.signOut()
            mAuth.signOut()
            googleSignInClient.signOut().addOnCompleteListener{
                val `in` = Intent(getActivity(), PreLoginActivity::class.java)
                startActivity(`in`)
            }
        }

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

}