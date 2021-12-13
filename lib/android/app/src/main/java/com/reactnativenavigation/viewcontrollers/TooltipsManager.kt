package com.reactnativenavigation.viewcontrollers

import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class TooltipsManager {
    private val registry = mutableMapOf<String, ViewController<*>>()

    fun add(id:String, controller: ViewController<*>){
        registry[id] = controller
    }

    fun remove(id:String):ViewController<*>?{
        return registry[id]
    }
}