package com.example.lendyproj

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.lendyproj.MemberAdapter
import com.example.lendyproj.MemberListener
import com.example.lendyproj.model.ListMemberItem
import com.example.lendyproj.model.Member
import kotlinx.android.synthetic.main.fragment_bookshelf.view.*
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerView as recyclerView1


class HomeFragment : Fragment(), MemberListener {

    lateinit var aboutFragment: AboutFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        setHasOptionsMenu(true)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val memberList = getDataFromJson()

        val memberAdapter = MemberAdapter(memberList, this)

        recyclerView1.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = memberAdapter
            onFlingListener = null
        }

        memberAdapter.notifyDataSetChanged()

    }

    private fun getDataFromJson(): ArrayList<Member> {
        val inputStream = requireActivity().assets.open("bnk48member.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val json = String(buffer, charset("UTF-8"))
        val listType = object : TypeToken<ListMemberItem>() {}.type
        val gson = Gson().fromJson<ListMemberItem>(json, listType)
        return gson.members
    }

    override fun onItemClick() {
//        val `in` = Intent(getActivity(), BookDetailActivity::class.java)
//        startActivity(`in`)
    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.options_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.aboutFragment -> {
//                val `in` = Intent(getActivity(), OptionsActivity::class.java)
//                startActivity(`in`)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}