package com.reactnativenavigation.react

import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.NavigationApplication
import com.reactnativenavigation.options.LayoutFactory
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.parsers.JSONParser
import com.reactnativenavigation.options.parsers.LayoutNodeParser
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.react.events.EventEmitter
import com.reactnativenavigation.utils.LaunchArgsParser
import com.reactnativenavigation.utils.Now
import com.reactnativenavigation.utils.SystemUiUtils.getStatusBarHeight
import com.reactnativenavigation.utils.UiThread
import com.reactnativenavigation.utils.UiUtils
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.reactnativenavigation.viewcontrollers.navigator.Navigator
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import java.util.Objects

class NavigationTurboModule(
    reactContext: ReactApplicationContext,
    private val layoutFactory: LayoutFactory
) : NativeRNNTurboModuleSpec(reactContext) {

    private val now = Now()
    private val jsonParser: JSONParser = JSONParser()
    private lateinit var eventEmitter: EventEmitter

    init {
        reactContext.addLifecycleEventListener(object : LifecycleEventListenerAdapter() {
            override fun onHostPause() {
                super.onHostPause()
                UiUtils.runOnMainThread {
                    navigator()?.onHostPause()
                }
            }

            override fun onHostResume() {
                eventEmitter = EventEmitter(reactContext)
                navigator()?.let { navigator: Navigator ->
                    navigator.setEventEmitter(eventEmitter)
                    layoutFactory.init(
                        activity(),
                        eventEmitter,
                        navigator.childRegistry,
                        (reactApplicationContext.applicationContext as NavigationApplication).externalComponents
                    )
                    UiUtils.runOnMainThread { navigator.onHostResume() }
                }
            }
        })
    }

    override fun getTypedExportedConstants(): MutableMap<String, Any> {
        val constants = mutableMapOf<String, Any>()
        constants[Constants.BACK_BUTTON_JS_KEY] = Constants.BACK_BUTTON_ID
        constants[Constants.BOTTOM_TABS_HEIGHT_KEY] =
            UiUtils.pxToDp(
                reactApplicationContext,
                UiUtils.getBottomTabsHeight(reactApplicationContext).toFloat()
            ).toDouble()
        constants[Constants.STATUS_BAR_HEIGHT_KEY] =
            UiUtils.pxToDp(reactApplicationContext, getStatusBarHeight(currentActivity).toFloat())
                .toDouble()
        constants[Constants.TOP_BAR_HEIGHT_KEY] = UiUtils.pxToDp(
            reactApplicationContext,
            UiUtils.getTopBarHeight(reactApplicationContext).toFloat()
        ).toDouble()
        return constants
    }

    override fun setRoot(commandId: String, layout: ReadableMap, promise: Promise) {
        Log.d("NavigationTurboModule", "setRoot ${Thread.currentThread()}")
        val layoutTree = LayoutNodeParser.parse(
            Objects.requireNonNull(
                jsonParser.parse(layout).optJSONObject("root")
            )
        )
        handle {
            Log.d("NavigationTurboModule", "setRoot handle ${Thread.currentThread()}")
            val viewController = layoutFactory.create(layoutTree)
            val activity = currentActivity
            if (activity == null) {
                promise.reject("ACTIVITY_NULL", "Activity is null")
                return@handle
            }
            val nav = navigator()
            nav?.setRoot(
                viewController,
                NativeCommandListener("setRoot", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun setDefaultOptions(options: ReadableMap?) {
        handle {
            val defaultOptions = parse(options)
            layoutFactory.defaultOptions = defaultOptions
            navigator()?.defaultOptions = defaultOptions
        }
    }

    override fun mergeOptions(componentId: String?, options: ReadableMap?) {
        handle { navigator()?.mergeOptions(componentId, parse(options)) }
    }

    override fun push(
        commandId: String,
        componentId: String,
        layout: ReadableMap,
        promise: Promise
    ) {
        val layoutTree = LayoutNodeParser.parse(jsonParser.parse(layout))
        handle {
            val viewController = layoutFactory.create(layoutTree)
            val nav = navigator()
            nav?.push(
                componentId,
                viewController,
                NativeCommandListener("push", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun pop(
        commandId: String,
        componentId: String,
        options: ReadableMap?,
        promise: Promise
    ) {
        handle {
            val nav = navigator()
            nav?.pop(
                componentId,
                parse(options),
                NativeCommandListener("pop", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun popTo(
        commandId: String,
        componentId: String,
        options: ReadableMap?,
        promise: Promise
    ) {
        handle {
            val nav = navigator()
            nav?.popTo(
                componentId,
                parse(options),
                NativeCommandListener("popTo", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun popToRoot(
        commandId: String,
        componentId: String,
        options: ReadableMap?,
        promise: Promise
    ) {
        handle {
            val nav = navigator()
            nav?.popToRoot(
                componentId,
                parse(options),
                NativeCommandListener("popToRoot", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun setStackRoot(
        commandId: String,
        componentId: String,
        children: ReadableArray,
        promise: Promise
    ) {
        handle {
            val _children = ArrayList<ViewController<*>>()
            for (i in 0..<children.size()) {
                val layoutTree = LayoutNodeParser.parse(jsonParser.parse(children.getMap(i)))
                _children.add(layoutFactory.create(layoutTree))
            }
            val nav = navigator()
            nav?.setStackRoot(
                componentId,
                _children,
                NativeCommandListener("setStackRoot", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun showModal(commandId: String, layout: ReadableMap, promise: Promise) {
        val layoutTree = LayoutNodeParser.parse(jsonParser.parse(layout))
        handle {
            val viewController = layoutFactory.create(layoutTree)
            val nav = navigator()
            nav?.showModal(
                viewController,
                NativeCommandListener("showModal", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun dismissModal(
        commandId: String,
        componentId: String,
        options: ReadableMap?,
        promise: Promise
    ) {
        handle {
            val nav = navigator()
            nav?.mergeOptions(componentId, parse(options))
            nav?.dismissModal(
                componentId,
                NativeCommandListener("dismissModal", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun dismissAllModals(commandId: String, options: ReadableMap?, promise: Promise) {
        handle {
            val nav = navigator()
            nav?.dismissAllModals(
                parse(options),
                NativeCommandListener("dismissAllModals", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun showOverlay(commandId: String, layout: ReadableMap, promise: Promise) {
        val layoutTree = LayoutNodeParser.parse(jsonParser.parse(layout))
        handle {
            val viewController = layoutFactory.create(layoutTree)
            val nav = navigator()
            nav?.showOverlay(
                viewController,
                NativeCommandListener("showOverlay", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun dismissOverlay(commandId: String, componentId: String, promise: Promise) {
        handle {
            val nav = navigator()
            nav?.dismissOverlay(
                componentId,
                NativeCommandListener("dismissOverlay", commandId, promise, eventEmitter, now, nav)
            )
        }
    }

    override fun dismissAllOverlays(commandId: String, promise: Promise) {
        handle {
            val nav = navigator()
            nav?.dismissAllOverlays(
                NativeCommandListener(
                    "dismissAllOverlays",
                    commandId,
                    promise,
                    eventEmitter,
                    now,
                    nav
                )
            )
        }
    }

    override fun getLaunchArgs(commandId: String, promise: Promise) {
        promise.resolve(LaunchArgsParser.parse(activity()))
    }

    override fun getNavigationState(commandId: String, promise: Promise) {
        handle {
            val state = navigator()?.getNavigationState()
            promise.resolve(state?.let { mapToWritable(it) })
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapToWritable(map: Map<String, Any?>): WritableMap {
        val writableMap = Arguments.createMap()
        for ((key, value) in map) {
            when (value) {
                null -> writableMap.putNull(key)
                is String -> writableMap.putString(key, value)
                is Int -> writableMap.putInt(key, value)
                is Double -> writableMap.putDouble(key, value)
                is Boolean -> writableMap.putBoolean(key, value)
                is Map<*, *> -> writableMap.putMap(key, mapToWritable(value as Map<String, Any?>))
                is List<*> -> writableMap.putArray(key, listToWritable(value))
            }
        }
        return writableMap
    }

    @Suppress("UNCHECKED_CAST")
    private fun listToWritable(list: List<*>): WritableArray {
        val writableArray = Arguments.createArray()
        for (value in list) {
            when (value) {
                null -> writableArray.pushNull()
                is String -> writableArray.pushString(value)
                is Int -> writableArray.pushInt(value)
                is Double -> writableArray.pushDouble(value)
                is Boolean -> writableArray.pushBoolean(value)
                is Map<*, *> -> writableArray.pushMap(mapToWritable(value as Map<String, Any?>))
                is List<*> -> writableArray.pushArray(listToWritable(value))
            }
        }
        return writableArray
    }

    private fun parse(mergeOptions: ReadableMap?): Options {
        val ctx = reactApplicationContext
        return if (mergeOptions ==
            null
        ) Options.EMPTY else Options.parse(
            ctx,
            TypefaceLoader(reactApplicationContext),
            jsonParser.parse(mergeOptions)
        )
    }

    private fun navigator(): Navigator? {
        val navigator = activity()?.navigator
        if (navigator == null) {
            Log.e("NavigationTurboModule", "navigator is null!")
        }
        return navigator
    }

    private fun handle(task: Runnable) {
        UiThread.post {
            activity()?.let {
                if (it.isFinishing) {
                    return@let
                }
                task.run()
            }
        }
    }

    private fun activity(): NavigationActivity? {
        val activity = reactApplicationContext.currentActivity as NavigationActivity?
        if (activity == null) {
            Log.e("NavigationTurboModule", "current activity is null!")
        }
        return currentActivity as NavigationActivity?
    }

    companion object {
        const val NAME = "RNNTurboModule"
    }

}
