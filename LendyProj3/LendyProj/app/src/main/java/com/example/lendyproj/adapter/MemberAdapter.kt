package com.example.lendyproj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lendyproj.R
import com.example.lendyproj.model.Member
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_member.view.*

class MemberAdapter(private val items: ArrayList<Member>,
                    private val listener: MemberListener
): RecyclerView.Adapter<MemberAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { listener.onItemClick() }
    }

    class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView) {
        fun bind(member: Member) {
            itemView.apply {

                when (member.generation == 1) {
                    true -> setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.lendybg))
                    false -> setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.lendytext))
                }

                textTitle.text = member.nickname.en
                val fullname = member.firstName.en + " " + member.lastName.en
                textAuthor.text = fullname
            }

            Picasso.get().load(member.avatarImage).into(itemView.imgBook)

        }
    }

}