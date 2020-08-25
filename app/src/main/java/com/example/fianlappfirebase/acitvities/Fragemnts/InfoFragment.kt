package com.example.fianlappfirebase.acitvities.Fragemnts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.extencionfuntions.toastt
import com.example.fianlappfirebase.R
import com.example.fianlappfirebase.acitvities.Models.RxBus
import com.example.fianlappfirebase.acitvities.Models.TotalMessagesEvent
import com.example.fianlappfirebase.acitvities.adapters.CircleTrans
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_left.view.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import java.lang.Exception

class InfoFragment : Fragment() {
    lateinit var _view: View
    lateinit var currentUser: FirebaseUser

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var chatDbRef: CollectionReference
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val chatsubcription: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_info, container, false)

        setUpCurrentUser()
        setUpCurrentUserForUi()


        setUpChatDb()

       // subscribeTotalMessagesFirebaseStyle()
        totalMessagesEventBusReactiveStyle()

        return _view
    }

    private fun subscribeTotalMessagesFirebaseStyle() {

        chatDbRef.addSnapshotListener(object : java.util.EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {

                exception?.let {
                    activity!!.toastt("failed ")
                    return
                }
                querySnapshot?.let {
                    _view.textViewInfoTotalMessages.text="${it.size()}"
                }

            }
        })

    }

    private fun setUpCurrentUserForUi() {
        _view.textViewInfoEmail.text = currentUser.email
        _view.textViewInfoName.text = currentUser.displayName?.let {
            currentUser.displayName
        } ?: run { getString(R.string.info_no_name) }

        currentUser.photoUrl?.let {


            Picasso.get().load(currentUser.photoUrl).resize(300, 300)
                .centerCrop().transform(CircleTrans()).into(_view.imageViewInfoAvatar)
        } ?: kotlin.run {
            Picasso.get().load(R.drawable.ic_person).resize(300, 300)
                .centerCrop().transform(CircleTrans()).into(_view.imageViewInfoAvatar)


        }
    }


    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!

    }

    private fun setUpChatDb() {
        chatDbRef = store.collection("chat")

    }

    private fun totalMessagesEventBusReactiveStyle(){
        RxBus.listen(TotalMessagesEvent::class.java).subscribe({
            _view.textViewInfoTotalMessages.text="${it.total}"
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chatsubcription?.remove()

    }




}
