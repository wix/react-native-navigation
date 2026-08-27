package com.reactnativenavigation.views.element

import android.view.View
import android.view.ViewGroup

/** Fabric applies React zIndex by ordering native children, not by setting View.z. */
internal object ViewHierarchyComparator : Comparator<View> {
    override fun compare(first: View, second: View): Int {
        val firstPath = drawingPath(first)
        val secondPath = drawingPath(second)
        for (index in 0 until minOf(firstPath.size, secondPath.size)) {
            val elevationOrder = firstPath[index].first.compareTo(secondPath[index].first)
            if (elevationOrder != 0) return elevationOrder
            val siblingOrder = firstPath[index].second.compareTo(secondPath[index].second)
            if (siblingOrder != 0) return siblingOrder
        }
        return firstPath.size.compareTo(secondPath.size)
    }

    private fun drawingPath(view: View): List<Pair<Float, Int>> {
        val path = mutableListOf<Pair<Float, Int>>()
        var child = view
        var parent = child.parent as? ViewGroup
        while (parent != null) {
            path.add(child.z to parent.indexOfChild(child))
            child = parent
            parent = child.parent as? ViewGroup
        }
        return path.asReversed()
    }
}
