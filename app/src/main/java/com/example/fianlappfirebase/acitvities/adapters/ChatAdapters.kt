package com.example.fianlappfirebase.acitvities.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.extencionfuntions.infla
import com.example.fianlappfirebase.R
import com.example.fianlappfirebase.acitvities.Models.Messages_Model
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_left.view.*
import kotlinx.android.synthetic.main.fragment_chat_right.view.*
import java.text.SimpleDateFormat

val Global_Message = 1
val Mymessage = 2

private val layoutRight = R.layout.fragment_chat_right
private val layoutLeft = R.layout.fragment_chat_left


class ChatAdapters(val items: List<Messages_Model>, val userId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int) =
        if (items[position].authorId == userId) Mymessage else Global_Message


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Mymessage -> ViewHolderR(parent.infla(layoutRight))

            else -> ViewHolderL(parent.infla(layoutLeft))


        }

    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {

            Mymessage -> (holder as ViewHolderR).bind(items[position])

            Global_Message -> (holder as ViewHolderL).bind(items[position])

        }

    }

    class ViewHolderR(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messagesModel: Messages_Model) = with(itemView) {
            textViewMessageRight.text = messagesModel.message
            textViewTimeRight.text = SimpleDateFormat("hh:mm").format(messagesModel.sentAt)

            //picasso load image here
            Picasso.get().load(messagesModel.profileImageURL).resize(100,100)
                .centerCrop()
                .transform(CircleTrans())
                .into(imageViewProfileRight)

        }
    }

    class ViewHolderL(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messagesModel: Messages_Model) = with(itemView) {
            textViewMessageLeft.text = messagesModel.message
            textViewTimeLeft.text = SimpleDateFormat("hh:mm").format(messagesModel.sentAt)

            //picasso load image here
            Picasso.get().load(messagesModel.profileImageURL).resize(100,100)
                .centerCrop()
                .transform(CircleTrans())

                .into(imageViewProfileLeft )
        }
    }


}