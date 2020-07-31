package com.example.fianlappfirebase.acitvities.Fragemnts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fianlappfirebase.R
import com.example.fianlappfirebase.acitvities.Models.Messages_Model
import com.example.fianlappfirebase.acitvities.adapters.ChatAdapters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {

    private  lateinit var adapter: ChatAdapters
    private var messageList: ArrayList<Messages_Model> = ArrayList()

    private lateinit var _view: View
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser:FirebaseUser
    private  lateinit var chatDbRef:CollectionReference
    private val store: FirebaseFirestore= FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_chat, container, false)



        setUpChatDb()
        setUpCurrentUser()
        setUpRecyclerview()
        setUpChatbtn()
        return _view


    }

    private fun setUpCurrentUser() {
        currentUser=mAuth.currentUser!!

    }

    private fun setUpChatDb() {
        chatDbRef =store.collection("chat")

    }

    private fun setUpChatbtn() {
        _view.imageButtonchatsend.setOnClickListener {

            val messageText=_view.editTextmessage.text.toString()
            if (messageText.isNotEmpty()){
                val message=Messages_Model(currentUser.uid,messageText,currentUser.photoUrl.toString(),Date())
                //salvato in firebase
                _view.editTextmessage.setText("")
            }
        }


    }

    private fun setUpRecyclerview() {
        val layoutManager=LinearLayoutManager(context)
        adapter= ChatAdapters(messageList,currentUser.uid)
        _view.recyclreview.setHasFixedSize(true)
        _view.recyclreview.layoutManager=layoutManager
        _view.recyclreview.itemAnimator=DefaultItemAnimator()
        _view.recyclreview.adapter=adapter

    }


}