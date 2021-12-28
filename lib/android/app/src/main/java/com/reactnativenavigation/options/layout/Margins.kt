package com.reactnativenavigation.options.layout

import com.reactnativenavigation.options.abstraction.IOptions
import org.json.JSONObject

class Margins(
    var top: Int?=null,
    var left: Int?=null,
    var bottom: Int?=null,
    var right: Int?=null
): IOptions<Margins> {
    override fun merge(toMerge: Margins?, defaults: Margins?) {
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
        fun parse(jsonObject: JSONObject?): Margins {
            return Margins(
                jsonObject?.optInt("top"),
                jsonObject?.optInt("left"),
                jsonObject?.optInt("bottom"),
                jsonObject?.optInt("right")
            )
        }
    }

    override fun hasValue(): Boolean {
        return top!=null || bottom!=null || left!=null || right!=null
    }

}
