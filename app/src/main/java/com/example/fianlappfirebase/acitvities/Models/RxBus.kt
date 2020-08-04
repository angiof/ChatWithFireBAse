package com.example.fianlappfirebase.acitvities.Models


import io.reactivex.subjects.PublishSubject
import java.util.*

object RxBus {

    private val publisher = PublishSubject.create<Any>()


    fun publish(event: Any){
        publisher.onNext(event)
    }

    fun <T>listen (evntType : Class<T>): io.reactivex.Observable<T> = publisher.ofType(evntType)



}