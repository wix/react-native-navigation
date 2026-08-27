package com.reactnativenavigation.views.element

import android.view.View
import android.widget.FrameLayout
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class ViewHierarchyComparatorTest {
    private val context = RuntimeEnvironment.getApplication()

    @Test
    fun preservesFabricSiblingOrderRegardlessOfTransitionDeclarationOrder() {
        val parent = FrameLayout(context)
        val back = View(context)
        val front = View(context)
        parent.addView(back)
        parent.addView(front)

        assertThat(listOf(front, back).sortedWith(ViewHierarchyComparator))
            .containsExactly(back, front)
    }

    @Test
    fun comparesNestedViewsUsingTheirAncestorOrder() {
        val root = FrameLayout(context)
        val backParent = FrameLayout(context)
        val frontParent = FrameLayout(context)
        root.addView(backParent)
        root.addView(frontParent)
        backParent.addView(View(context))
        val back = View(context)
        val front = View(context)
        backParent.addView(back)
        frontParent.addView(front)

        assertThat(listOf(front, back).sortedWith(ViewHierarchyComparator))
            .containsExactly(back, front)
    }

    @Test
    fun respectsAndroidElevationBeforeSiblingIndex() {
        val parent = FrameLayout(context)
        val elevated = View(context).apply { elevation = 4f }
        val back = View(context)
        parent.addView(elevated)
        parent.addView(back)

        assertThat(listOf(elevated, back).sortedWith(ViewHierarchyComparator))
            .containsExactly(back, elevated)
    }

    @Test
    fun ordersAnAncestorBeforeItsDescendant() {
        val root = FrameLayout(context)
        val parent = FrameLayout(context)
        val child = View(context)
        root.addView(parent)
        parent.addView(child)

        assertThat(listOf(child, parent).sortedWith(ViewHierarchyComparator))
            .containsExactly(parent, child)
        assertThat(ViewHierarchyComparator.compare(child, child)).isZero()
    }

    @Test
    fun detachedViewsRetainDeclarationOrder() {
        val first = View(context)
        val second = View(context)

        assertThat(listOf(first, second).sortedWith(ViewHierarchyComparator))
            .containsExactly(first, second)
    }
}
