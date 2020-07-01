package com.reactnativenavigation.viewcontrollers.fakes

import android.app.Activity
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.parse.Options
import com.reactnativenavigation.presentation.Presenter
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.ParentController
import com.reactnativenavigation.viewcontrollers.ViewController
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController
import org.mockito.Mockito.mock

class FakeParentController @JvmOverloads constructor(
        activity: Activity,
        childRegistry: ChildControllersRegistry,
        private val child: ViewController<*>,
        id: String = "Parent" + CompatUtils.generateViewId(),
        presenter: Presenter = mock(Presenter::class.java),
        initialOptions: Options = Options.EMPTY
) : ParentController<CoordinatorLayout>(activity, childRegistry, id, presenter, initialOptions) {
    init {
        child.parentController = this
    }
    override fun getCurrentChild(): ViewController<*> = child

    override fun createView() = CoordinatorLayout(activity).apply {
        addView(child.view)
    }

    override fun getChildControllers() = listOf(child)

    override fun sendOnNavigationButtonPressed(buttonId: String?) = child.sendOnNavigationButtonPressed(buttonId)
}