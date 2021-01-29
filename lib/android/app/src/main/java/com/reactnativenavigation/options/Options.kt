package com.reactnativenavigation.options

import android.content.Context
import androidx.annotation.CheckResult
import com.reactnativenavigation.options.AnimationsOptions.Companion.parse
import com.reactnativenavigation.options.BottomTabsOptions.Companion.parse
import com.reactnativenavigation.options.params.NullNumber
import com.reactnativenavigation.options.params.NullText
import com.reactnativenavigation.options.parsers.TypefaceLoader
import org.json.JSONObject

open class Options {
    
    var topBar = TopBarOptions()
    
    var topTabs = TopTabsOptions()
    
    var topTabOptions = TopTabOptions()
    
    var bottomTabOptions = BottomTabOptions()
    
    var bottomTabsOptions = BottomTabsOptions()
    
    var overlayOptions = OverlayOptions()
    
   var fabOptions = FabOptions()
    
    var animations = AnimationsOptions()
    
    var sideMenuRootOptions = SideMenuRootOptions()
    
    var modal = ModalOptions()
    
    var navigationBar = NavigationBarOptions()
    
    var statusBar = StatusBarOptions()
    
    var layout = LayoutOptions()
    fun setTopTabIndex(i: Int) {
        topTabOptions.tabIndex = i
    }

    @CheckResult
    open fun copy(): Options {
        val result = Options()
        result.topBar.mergeWith(topBar)
        result.topTabs.mergeWith(topTabs)
        result.topTabOptions.mergeWith(topTabOptions)
        result.bottomTabOptions.mergeWith(bottomTabOptions)
        result.bottomTabsOptions.mergeWith(bottomTabsOptions)
        result.overlayOptions = overlayOptions
        result.fabOptions.mergeWith(fabOptions)
        result.sideMenuRootOptions.mergeWith(sideMenuRootOptions)
        result.animations.mergeWith(animations)
        result.modal.mergeWith(modal)
        result.navigationBar.mergeWith(navigationBar)
        result.statusBar.mergeWith(statusBar)
        result.layout.mergeWith(layout)
        return result
    }

    @CheckResult
    fun mergeWith(other: Options): Options {
        val result = copy()
        result.topBar.mergeWith(other.topBar)
        result.topTabs.mergeWith(other.topTabs)
        result.topTabOptions.mergeWith(other.topTabOptions)
        result.bottomTabOptions.mergeWith(other.bottomTabOptions)
        result.bottomTabsOptions.mergeWith(other.bottomTabsOptions)
        result.fabOptions.mergeWith(other.fabOptions)
        result.animations.mergeWith(other.animations)
        result.sideMenuRootOptions.mergeWith(other.sideMenuRootOptions)
        result.modal.mergeWith(other.modal)
        result.navigationBar.mergeWith(other.navigationBar)
        result.statusBar.mergeWith(other.statusBar)
        result.layout.mergeWith(other.layout)
        return result
    }

    fun withDefaultOptions(defaultOptions: Options): Options {
        topBar.mergeWithDefault(defaultOptions.topBar)
        topTabOptions.mergeWithDefault(defaultOptions.topTabOptions)
        topTabs.mergeWithDefault(defaultOptions.topTabs)
        bottomTabOptions.mergeWithDefault(defaultOptions.bottomTabOptions)
        bottomTabsOptions.mergeWithDefault(defaultOptions.bottomTabsOptions)
        fabOptions.mergeWithDefault(defaultOptions.fabOptions)
        animations.mergeWithDefault(defaultOptions.animations)
        sideMenuRootOptions.mergeWithDefault(defaultOptions.sideMenuRootOptions)
        modal.mergeWithDefault(defaultOptions.modal)
        navigationBar.mergeWithDefault(defaultOptions.navigationBar)
        statusBar.mergeWithDefault(defaultOptions.statusBar)
        layout.mergeWithDefault(defaultOptions.layout)
        return this
    }

    fun clearTopBarOptions(): Options {
        topBar = TopBarOptions()
        return this
    }

    fun clearBottomTabsOptions(): Options {
        bottomTabsOptions = BottomTabsOptions()
        return this
    }

    fun clearTopTabOptions(): Options {
        topTabOptions = TopTabOptions()
        return this
    }

    fun clearTopTabsOptions(): Options {
        topTabs = TopTabsOptions()
        return this
    }

    fun clearBottomTabOptions(): Options {
        bottomTabOptions = BottomTabOptions()
        return this
    }

    fun clearAnimationOptions(): Options {
        animations = AnimationsOptions()
        return this
    }

    fun clearFabOptions(): Options {
        fabOptions = FabOptions()
        return this
    }

    fun clearOneTimeOptions(): Options {
        bottomTabsOptions.currentTabId = NullText()
        bottomTabsOptions.currentTabIndex = NullNumber
        return this
    }

    companion object {
        
        val EMPTY = Options()
        @JvmStatic
        fun parse(context: Context?, typefaceManager: TypefaceLoader?, json: JSONObject?): Options {
            val result = Options()
            if (json == null) return result
            result.topBar = TopBarOptions.parse(context, typefaceManager, json.optJSONObject("topBar"))
            result.topTabs = TopTabsOptions.parse(context, json.optJSONObject("topTabs"))
            result.topTabOptions = TopTabOptions.parse(typefaceManager, json.optJSONObject("topTab"))
            result.bottomTabOptions = BottomTabOptions.parse(context, typefaceManager, json.optJSONObject("bottomTab"))
            result.bottomTabsOptions = parse(context, json.optJSONObject("bottomTabs"))
            result.overlayOptions = OverlayOptions.parse(json.optJSONObject("overlay"))
            result.fabOptions = FabOptions.parse(context, json.optJSONObject("fab"))
            result.sideMenuRootOptions = SideMenuRootOptions.parse(json.optJSONObject("sideMenu"))
            result.animations = parse(json.optJSONObject("animations"))
            result.modal = ModalOptions.parse(json)
            result.navigationBar = NavigationBarOptions.parse(context, json.optJSONObject("navigationBar"))
            result.statusBar = StatusBarOptions.parse(context, json.optJSONObject("statusBar"))
            result.layout = LayoutOptions.parse(context, json.optJSONObject("layout"))
            return result
        }
    }
}