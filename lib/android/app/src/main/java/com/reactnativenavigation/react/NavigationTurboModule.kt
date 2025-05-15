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
                    navigator().onHostPause()
                }
            }

            override fun onHostResume() {
                eventEmitter = EventEmitter(reactContext)
                navigator().setEventEmitter(eventEmitter)
                layoutFactory.init(
                    activity(),
                    eventEmitter,
                    navigator().getChildRegistry(),
                    (activity().application as NavigationApplication).externalComponents
                )
                UiUtils.runOnMainThread { navigator().onHostResume() }
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
            navigator().setRoot(
                viewController,
                NativeCommandListener("setRoot", commandId, promise, eventEmitter, now)
            )
        }
    }

    override fun setDefaultOptions(options: ReadableMap?) {
        handle {
            val defaultOptions = parse(options)
            layoutFactory.defaultOptions = defaultOptions
            navigator().defaultOptions = defaultOptions
        }
    }

    override fun mergeOptions(componentId: String?, options: ReadableMap?) {
        handle { navigator().mergeOptions(componentId, parse(options)) }
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
            navigator().push(
                componentId,
                viewController,
                NativeCommandListener("push", commandId, promise, eventEmitter, now)
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
            navigator().pop(
                componentId,
                parse(options),
                NativeCommandListener("pop", commandId, promise, eventEmitter, now)
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
            navigator().popTo(
                componentId,
                parse(options),
                NativeCommandListener("popTo", commandId, promise, eventEmitter, now)
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
            navigator().popToRoot(
                componentId,
                parse(options),
                NativeCommandListener("popToRoot", commandId, promise, eventEmitter, now)
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
            navigator().setStackRoot(
                componentId,
                _children,
                NativeCommandListener("setStackRoot", commandId, promise, eventEmitter, now)
            )
        }
    }

    override fun showModal(commandId: String, layout: ReadableMap, promise: Promise) {
        val layoutTree = LayoutNodeParser.parse(jsonParser.parse(layout))
        handle {
            val viewController = layoutFactory.create(layoutTree)
            navigator().showModal(
                viewController,
                NativeCommandListener("showModal", commandId, promise, eventEmitter, now)
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
            navigator().mergeOptions(componentId, parse(options))
            navigator().dismissModal(
                componentId,
                NativeCommandListener("dismissModal", commandId, promise, eventEmitter, now)
            )
        }
    }

    override fun dismissAllModals(commandId: String, options: ReadableMap?, promise: Promise) {
        handle {
            navigator().dismissAllModals(
                parse(options),
                NativeCommandListener("dismissAllModals", commandId, promise, eventEmitter, now)
            )
        }
    }

    override fun showOverlay(commandId: String, layout: ReadableMap, promise: Promise) {
        val layoutTree = LayoutNodeParser.parse(jsonParser.parse(layout))
        handle {
            val viewController = layoutFactory.create(layoutTree)
            navigator().showOverlay(
                viewController,
                NativeCommandListener("showOverlay", commandId, promise, eventEmitter, now)
            )
        }
    }

    override fun dismissOverlay(commandId: String, componentId: String, promise: Promise) {
        handle {
            navigator().dismissOverlay(
                componentId,
                NativeCommandListener("dismissOverlay", commandId, promise, eventEmitter, now)
            )
        }
    }

    override fun dismissAllOverlays(commandId: String, promise: Promise) {
        handle {
            navigator().dismissAllOverlays(
                NativeCommandListener(
                    "dismissAllOverlays",
                    commandId,
                    promise,
                    eventEmitter,
                    now
                )
            )
        }
    }

    override fun getLaunchArgs(commandId: String, promise: Promise) {
        promise.resolve(LaunchArgsParser.parse(activity()))

    }

    private fun parse(mergeOptions: ReadableMap?): Options {
        val ctx = reactApplicationContext
        return if (mergeOptions ==
            null
        ) Options.EMPTY else Options.parse(
            ctx,
            TypefaceLoader(activity()),
            jsonParser.parse(mergeOptions)
        )
    }

    private fun navigator(): Navigator {
        return activity().navigator
    }

    private fun handle(task: Runnable) {
        UiThread.post {
            if (currentActivity != null && !activity().isFinishing) {
                task.run()
            }
        }
    }

    private fun activity(): NavigationActivity {
        return currentActivity as NavigationActivity
    }

    companion object {
        const val NAME = "RNNTurboModule"
    }

}
