package com.reactnativenavigation.viewcontrollers.tooltips

import android.app.Activity
import android.view.View
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.mocks.SimpleComponentViewController
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.OverlayAttachOptions
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.viewcontrollers.TooltipsManager
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.ViewTooltip
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TooltipsManagerTest : BaseTest() {

    lateinit var uut: TooltipsManager
    private var hostViewController: ViewController<*>? = null
    private var anchorView: View? = null
    private var tooltipView: ViewTooltip.TooltipView? = null
    lateinit var activity: Activity
    lateinit var childRegistry: ChildControllersRegistry
    lateinit var commandListener: CommandListener
    override fun beforeEach() {
        super.beforeEach()
        commandListener = Mockito.mock(CommandListener::class.java)
        childRegistry = ChildControllersRegistry()
        hostViewController = Mockito.mock(ViewController::class.java)
        tooltipView = Mockito.mock(ViewTooltip.TooltipView::class.java)
        whenever(hostViewController?.showTooltip(any(), any(), any())).thenReturn(tooltipView)
        anchorView = Mockito.mock(View::class.java)
        activity = newActivity()
        uut = TooltipsManager({ hostViewController }, { anchorView })
    }

    @Test
    fun `showTooltip - should call showTooltip of the found controller and calls success`() {
        val overlayAttachOptions = newAttachOptions()

        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        uut.showTooltip(tooltipController, overlayAttachOptions, commandListener)

        verify(hostViewController)?.showTooltip(anchorView!!, overlayAttachOptions, tooltipController)
        verify(commandListener).onSuccess("TooltipId")
        verify(tooltipController).onViewDidAppear()
    }

    @Test
    fun `showTooltip - unfound host controller, should call listener error`() {
        val overlayAttachOptions = newAttachOptions()

        hostViewController = null
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        uut.showTooltip(tooltipController, overlayAttachOptions, commandListener)

        verify(commandListener).onError("Cannot find layout with id " + overlayAttachOptions.layoutId)
    }

    @Test
    fun `showTooltip - unfound anchor view, should call listener error`() {
        val overlayAttachOptions = newAttachOptions()

        anchorView = null
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        uut.showTooltip(tooltipController, overlayAttachOptions, commandListener)

        verify(commandListener).onError("Cannot find anchor view with id " + overlayAttachOptions.anchorId)
    }

    @Test
    fun `showTooltip - layout that cant show tooltip, should call listener error`() {
        val overlayAttachOptions = newAttachOptions()

        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        whenever(hostViewController?.showTooltip(any(), any(), any())).thenReturn(null)
        uut.showTooltip(tooltipController, overlayAttachOptions, commandListener)

        verify(commandListener).onError("Parent could not create tooltip, it could be null parent")
    }

    @Test
    fun `contains - should return true for shown tooltip and false for not `() {
        val overlayAttachOptions = newAttachOptions()

        val id = "TooltipId"
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, id, Options()))
        val tooltipController2 = spy(SimpleComponentViewController(activity, childRegistry, id + "1", Options()))
        Java6Assertions.assertThat(id in uut).isFalse

        uut.showTooltip(tooltipController, overlayAttachOptions, commandListener)
        Java6Assertions.assertThat(id in uut).isTrue

        uut.showTooltip(tooltipController2, overlayAttachOptions, commandListener)
        Java6Assertions.assertThat(id + "1" in uut).isTrue

        uut.dismissTooltip(id, commandListener)
        Java6Assertions.assertThat(id in uut).isFalse
        Java6Assertions.assertThat(id + "1" in uut).isTrue
    }


    @Test
    fun `dismissTooltip - should call error when dismissing non existing tooltip`() {
        uut.dismissTooltip("imposterTooltip", commandListener)
        verify(commandListener).onError("Can't dismiss non-shown tooltip of id: imposterTooltip")
    }


    @Test
    fun `dismissTooltip - should hide the tooltip and destroy the controller `() {
        val overlayAttachOptions = newAttachOptions()

        val id = "TooltipId"
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, id, Options()))
        Java6Assertions.assertThat(id in uut).isFalse

        uut.showTooltip(tooltipController, overlayAttachOptions, commandListener)
        val dismissCmdListener = Mockito.mock(CommandListener::class.java)

        uut.dismissTooltip(id, dismissCmdListener)


        verify(tooltipView)?.close()
        verify(dismissCmdListener).onSuccess(any())
        verify(tooltipController).destroy()
    }


    private fun newAttachOptions() = OverlayAttachOptions().apply {
        layoutId = Text("layoutId")
        anchorId = Text("anchorId")
        gravity = Text("top")
    }
}