package com.reactnativenavigation

import android.app.Application
import com.facebook.react.ReactApplication
import com.facebook.react.soloader.OpenSourceMergedSoMapping
import com.facebook.soloader.SoLoader
import com.reactnativenavigation.react.ReactGateway
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator
import java.io.IOException

abstract class NavigationApplication : Application(), ReactApplication {
    var reactGateway: ReactGateway? = null
        private set
    val externalComponents: MutableMap<String, ExternalComponentCreator> = HashMap()

    override fun onCreate() {
        super.onCreate()
        instance = this


        reactGateway = createReactGateway()
    }

    /**
     * Subclasses of NavigationApplication may override this method to create the singleton instance
     * of [ReactGateway]. For example, subclasses may wish to provide a custom [ReactNativeHost]
     * with the ReactGateway. This method will be called exactly once, in the application's [.onCreate] method.
     *
     * Custom [ReactNativeHost]s must be sure to include [com.reactnativenavigation.react.NavigationPackage]
     *
     * @return a singleton [ReactGateway]
     */
    protected fun createReactGateway(): ReactGateway {
        return ReactGateway(this)
    }

    /**
     * Register a native View which can be displayed using the given `name`
     * @param name Unique name used to register the native view
     * @param creator Used to create the view at runtime
     */
    @Suppress("unused")
    fun registerExternalComponent(name: String, creator: ExternalComponentCreator) {
        if (externalComponents.containsKey(name)) {
            throw RuntimeException("A component has already been registered with this name: $name")
        }
        externalComponents[name] = creator
    }

    companion object {
        @JvmField
        var instance: NavigationApplication? = null
    }
}
