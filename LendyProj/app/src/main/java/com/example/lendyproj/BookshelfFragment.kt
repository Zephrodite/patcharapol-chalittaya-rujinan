package com.example.lendyproj

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lendyproj.model.ListMemberItem
import com.example.lendyproj.model.Member
import com.example.lendyproj.ui.login.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import kotlinx.android.synthetic.main.fragment_bookshelf.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class BookshelfFragment : Fragment(), MemberListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookshelf, container, false)

        view.add_book_floating_button.setOnClickListener {
            val `in` = Intent(getActivity(), AddBookActivity::class.java)
            startActivity(`in`)
        }

        setHasOptionsMenu(true)


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val memberList = getDataFromJson()

        val memberAdapterVer2 = MemberAdapterVer2(memberList, this)

        recyclerView_1.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = memberAdapterVer2
            onFlingListener = null
        }

        memberAdapterVer2.notifyDataSetChanged()

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

