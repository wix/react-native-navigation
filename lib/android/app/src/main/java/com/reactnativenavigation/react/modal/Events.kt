package com.reactnativenavigation.react.modal

import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.RCTEventEmitter

open class RequestCloseModalEvent(viewTag: Int) : Event<RequestCloseModalEvent>(viewTag) {

    companion object{
        const val EVENT_NAME = "topRequestClose"
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