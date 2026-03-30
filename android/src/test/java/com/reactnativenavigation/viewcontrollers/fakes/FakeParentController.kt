package com.reactnativenavigation.viewcontrollers.fakes

import android.app.Activity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import org.mockito.kotlin.mock
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.parent.ParentController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class FakeParentController @JvmOverloads constructor(
        activity: Activity,
        childRegistry: ChildControllersRegistry,
        private val child: ViewController<*>,
        id: String = "Parent" + CompatUtils.generateViewId(),
        presenter: Presenter = mock(),
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