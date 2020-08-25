package com.example.fianlappfirebase.acitvities.Fragemnts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.extencionfuntions.toastt
import com.example.fianlappfirebase.R
import com.example.fianlappfirebase.acitvities.Models.Messages_Model
import com.example.fianlappfirebase.acitvities.Models.RxBus
import com.example.fianlappfirebase.acitvities.Models.TotalMessagesEvent
import com.example.fianlappfirebase.acitvities.adapters.ChatAdapters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatFragment : Fragment() {

    private lateinit var adapter: ChatAdapters
    private var messageList: ArrayList<Messages_Model> = ArrayList()

    private lateinit var _view: View
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private lateinit var chatDbRef: CollectionReference
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val chatsubcription: ListenerRegistration? = null

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
        subscribeCHatMessenger()

        registerForContextMenu(_view.recyclreview)


        return _view


    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!

    }

    private fun setUpChatDb() {
        chatDbRef = store.collection("chat")
        //chatDbRef = store.collection("chat0")


    }

    private fun setUpChatbtn() {
        _view.imageButtonchatsend.setOnClickListener {

            val messageText = _view.editTextmessage.text.toString()
            if (messageText.isNotEmpty()) {
                val photo =
                    currentUser.photoUrl?.let { currentUser.photoUrl.toString() } ?: run { "" }
                val message = Messages_Model(
                    currentUser.uid, messageText, photo, Date()
                )

                //salvato in firebase
                saveMessage(message)
                _view.editTextmessage.setText("")
            }
        }


    }

    private fun setUpRecyclerview() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapters(messageList, currentUser.uid)
        _view.recyclreview.setHasFixedSize(true)
        _view.recyclreview.layoutManager = layoutManager
        _view.recyclreview.itemAnimator = DefaultItemAnimator()
        _view.recyclreview.adapter = adapter

    }

    private fun saveMessage(message: Messages_Model) {
        val newMessage = HashMap<String, Any>()

        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageURL"] = message.profileImageURL
        newMessage["sentAt"] = message.sentAt
        //
        chatDbRef.add(newMessage)
            .addOnCompleteListener {
                activity!!.toastt("Message added!")
            }
            .addOnFailureListener {
                activity!!.toastt("Message erorr,try again idk! :) ")
            }


    }


    private fun subscribeCHatMessenger() {

        chatDbRef
            .orderBy("sentAt", Query.Direction.DESCENDING)
            .limit(20)

            .addSnapshotListener(object : EventListener,
                com.google.firebase.firestore.EventListener<QuerySnapshot> {

                override fun onEvent(
                    snapshot: QuerySnapshot?,
                    exeption: FirebaseFirestoreException?
                ) {

                    exeption?.let {
                        activity?.toastt("exeption")
                        return
                    }
                    snapshot?.let {
                        messageList.clear()
                        val message = it.toObjects(Messages_Model::class.java)
                        messageList.addAll(message.asReversed())
                        adapter.notifyDataSetChanged()
                        _view.recyclreview.smoothScrollToPosition(messageList.size)
                        RxBus.publish(TotalMessagesEvent(messageList.size))
                    }

                }


            })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        chatsubcription?.remove()


    }


}

