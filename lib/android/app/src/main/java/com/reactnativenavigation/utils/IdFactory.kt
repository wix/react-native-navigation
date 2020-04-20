package com.reactnativenavigation.utils


class IdFactory {
    companion object {
        private val stringIdToIntId = HashMap<String, Int>()
        private var count = 0

        fun get(id: String): Int {
            return if (stringIdToIntId.containsKey(id)) {
                stringIdToIntId[id]!!
            } else {
                (++count).apply { stringIdToIntId[id] = count }
            }
        }
    }
}