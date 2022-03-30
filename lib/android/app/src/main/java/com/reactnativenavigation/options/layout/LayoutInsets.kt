package com.reactnativenavigation.options.layout

import com.reactnativenavigation.utils.dp
import org.json.JSONObject

class LayoutInsets(
    var top: Int?=null,
    var left: Int?=null,
    var bottom: Int?=null,
    var right: Int?=null
) {
     fun merge(toMerge: LayoutInsets?, defaults: LayoutInsets?) {
        toMerge?.let { options->
            options.top?.let { this.top = it  }
            options.bottom?.let { this.bottom = it  }
            options.left?.let { this.left = it  }
            options.right?.let { this.right = it  }
        }

        defaults?.let {
            options->
            top = top?:options.top
            left = left?:options.left
            right = right?:options.right
            bottom = bottom?:options.bottom
        }
    }

    companion object{
        fun parse(jsonObject: JSONObject?): LayoutInsets {
            return LayoutInsets(
                jsonObject?.optInt("top")?.dp,
                jsonObject?.optInt("left")?.dp,
                jsonObject?.optInt("bottom")?.dp,
                jsonObject?.optInt("right")?.dp
            )
        }
    }

     fun hasValue(): Boolean {
        return top!=null || bottom!=null || left!=null || right!=null
    }

}
