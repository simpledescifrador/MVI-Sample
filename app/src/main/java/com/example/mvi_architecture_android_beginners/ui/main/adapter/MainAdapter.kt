package com.example.mvi_architecture_android_beginners.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvi_architecture_android_beginners.data.model.User
import com.example.mvi_architecture_android_beginners.databinding.ItemLayoutBinding

class MainAdapter(private val users: ArrayList<User>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    interface EventListener {
        fun onUserClick(user: User)
    }

    private lateinit var eventListener: EventListener

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class ViewHolder(private val _itemBinding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(_itemBinding.root) {
        val root = _itemBinding.root

        fun bind(user: User) {
            _itemBinding.textViewUserName.text = user.name
            _itemBinding.textViewUserEmail.text = user.email
            Glide.with(_itemBinding.imageViewAvatar.context)
                .load(user.avatar)
                .into(_itemBinding.imageViewAvatar)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
        holder.root.setOnClickListener {
            eventListener.onUserClick(users[position])
        }
    }

    override fun getItemCount(): Int = users.size

    fun addUsers(users: List<User>) {
        this.users.addAll(users)
        notifyDataSetChanged()
    }
}