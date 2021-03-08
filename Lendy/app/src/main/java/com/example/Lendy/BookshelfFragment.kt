package com.example.Lendy

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.fragment.app.Fragment
<<<<<<< HEAD
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
=======
<<<<<<< HEAD
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
=======
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
>>>>>>> 6a0eecd9212fce83a296dbfef9e350a36d30c2c2
import kotlinx.android.synthetic.main.fragment_bookshelf.view.*
>>>>>>> 9a30ec61a6ab078901badef8176b4958b2a3c9bf

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [BookshelfFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookshelfFragment : Fragment() {
<<<<<<< HEAD
    lateinit var aboutFragment: AboutFragment
    // TODO: Rename and change types of parameters
=======

>>>>>>> 9a30ec61a6ab078901badef8176b4958b2a3c9bf
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookshelf, container, false)
<<<<<<< HEAD
        return view
    }


=======

<<<<<<< HEAD
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
=======
        return view;
>>>>>>> 9a30ec61a6ab078901badef8176b4958b2a3c9bf
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
>>>>>>> 6a0eecd9212fce83a296dbfef9e350a36d30c2c2
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}