package com.reactnativenavigation.react.modal

import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.RCTEventEmitter

open class ModalDismissEvent(viewTag: Int) : Event<RequestDismissModalEvent>(viewTag) {

    companion object{
        const val EVENT_NAME = "topModalDismiss"
    }

    override fun getEventName(): String {
        return EVENT_NAME
    }

    override fun dispatch(rctEventEmitter: RCTEventEmitter) {
        rctEventEmitter.receiveEvent(viewTag, eventName, null)
    }
}
open class RequestDismissModalEvent(viewTag: Int) : Event<RequestDismissModalEvent>(viewTag) {

    companion object{
        const val EVENT_NAME = "topRequestDismiss"
    }

    override fun getEventName(): String {
        return EVENT_NAME
    }

    override fun dispatch(rctEventEmitter: RCTEventEmitter) {
        rctEventEmitter.receiveEvent(viewTag, eventName, null)
    }
}

open class ShowModalEvent(viewTag: Int) : Event<ShowModalEvent>(viewTag) {

    companion object{
        const val EVENT_NAME = "topShow"
    }

    override fun getEventName(): String {
        return EVENT_NAME
    }

    override fun dispatch(rctEventEmitter: RCTEventEmitter) {
        rctEventEmitter.receiveEvent(viewTag, eventName, null)
    }
}