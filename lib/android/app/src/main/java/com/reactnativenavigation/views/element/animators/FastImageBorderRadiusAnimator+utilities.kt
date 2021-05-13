package com.reactnativenavigation.views.element.animators

import android.view.View
import android.view.ViewGroup
import com.facebook.react.views.view.ReactViewGroup
import com.reactnativenavigation.R
import com.reactnativenavigation.utils.ViewTags
import com.reactnativenavigation.utils.borderRadius

/**
 * Gets the border radius for a react-native-fast-image view that
 * is eventually embedded in other components. It might be the case
 * that the FastImage doesn't have a border radius, in this case
 * a inherited border radius will be looked up. Example of such a case
 *
 * @example
 *
 * <View style={{ borderRadius: 30, overflow: "hidden" }}>
 *     <FastImage {...} />
 * </View>
 */
fun FastImageBorderRadiusAnimator.getInheritedBorderRadius(v: View): Float {
    val borderRadius = getBorderRadius(v)
    if (borderRadius > 0f) {
        return borderRadius
    }

    return when(val parentView = getParent(v)) {
        null -> 0f
        else -> getInheritedBorderRadius(parentView)
    }
}

private fun getBorderRadius(v: View): Float {
    // checking if the view has borderRadius and overflow hidden.
    // when the overflow isn't hidden the borderRadius wouldn't apply.
    if (v is ReactViewGroup && v.borderRadius > 0f && v.overflow == "hidden") {
        return v.borderRadius
    }

    return 0f;
}

private fun getParent(view: View): ViewGroup? = try {
    when(view.parent) {
        null -> null
        else -> ViewTags.get<ViewGroup>(view, R.id.original_parent, view.parent as ViewGroup)
    }
// TODO: can we handle this better?: java.lang.ClassCastException: android.view.ViewRootImpl cannot be cast to android.view.ViewGroup
} catch (e: ClassCastException) {
    null
}
