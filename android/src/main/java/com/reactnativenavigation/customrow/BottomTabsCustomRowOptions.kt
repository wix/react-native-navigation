package com.reactnativenavigation.customrow

import android.graphics.Color
import com.facebook.react.bridge.ReadableMap

/**
 * Mirrors the iOS-side `RNNBottomTabsCustomRowOptions` data shape. All
 * fields are optional. Defaults are chosen to give an Android equivalent
 * of the iOS 26 floating glass pill on Android 12+ (RenderEffect blur),
 * and an opaque material chrome on older versions.
 */
data class BottomTabsCustomRowOptions(
    val height: Float? = null,
    val backgroundColor: Int? = null,
    val backgroundEffect: BackgroundEffect? = null,
    val cornerRadius: Float? = null,
    val horizontalMargin: Float? = null,
    val bottomMargin: Float? = null,
) {
    enum class BackgroundEffect { Glass, Blur, None }

    companion object {
        fun fromMap(map: ReadableMap?): BottomTabsCustomRowOptions {
            if (map == null) return BottomTabsCustomRowOptions()
            return BottomTabsCustomRowOptions(
                height = map.optFloat("height"),
                backgroundColor = map.optColor("backgroundColor"),
                backgroundEffect = map.optEffect("backgroundEffect"),
                cornerRadius = map.optFloat("cornerRadius"),
                horizontalMargin = map.optFloat("horizontalMargin"),
                bottomMargin = map.optFloat("bottomMargin"),
            )
        }

        private fun ReadableMap.optFloat(key: String): Float? {
            if (!hasKey(key) || isNull(key)) return null
            return getDouble(key).toFloat()
        }

        private fun ReadableMap.optColor(key: String): Int? {
            if (!hasKey(key) || isNull(key)) return null
            // Color may arrive as a number (Android-style int) or a wrapped
            // theme-color object. Support both shallowly.
            return when (getType(key)) {
                com.facebook.react.bridge.ReadableType.Number -> getInt(key)
                com.facebook.react.bridge.ReadableType.Map -> {
                    val sub = getMap(key)
                    val light = sub?.let { if (it.hasKey("light")) it.getInt("light") else null }
                    light ?: sub?.let { if (it.hasKey("color")) it.getInt("color") else null }
                }
                com.facebook.react.bridge.ReadableType.String -> {
                    runCatching { Color.parseColor(getString(key)) }.getOrNull()
                }
                else -> null
            }
        }

        private fun ReadableMap.optEffect(key: String): BackgroundEffect? {
            if (!hasKey(key) || isNull(key)) return null
            return when (getString(key)?.lowercase()) {
                "glass" -> BackgroundEffect.Glass
                "blur" -> BackgroundEffect.Blur
                "none" -> BackgroundEffect.None
                else -> null
            }
        }
    }
}
