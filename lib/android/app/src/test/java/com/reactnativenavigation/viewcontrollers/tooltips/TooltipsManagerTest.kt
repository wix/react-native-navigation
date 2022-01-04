package com.reactnativenavigation.viewcontrollers.tooltips

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.mocks.SimpleComponentViewController
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.OverlayAttachOptions
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.viewcontrollers.AttachedOverlayManager
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.ViewTooltip
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*

class TooltipsManagerTest : BaseTest() {

    lateinit var uut: AttachedOverlayManager
    private var hostViewController: ViewController<*>? = null
    private var anchorView: View? = null
    private var tooltipView: ViewTooltip.TooltipView? = null
    private var tooltipView2: ViewTooltip.TooltipView? = null
    lateinit var activity: Activity
    lateinit var childRegistry: ChildControllersRegistry
    lateinit var parent:FrameLayout
    lateinit var commandListener: CommandListener
    override fun beforeEach() {
        super.beforeEach()
        commandListener = Mockito.mock(CommandListener::class.java)
        childRegistry = ChildControllersRegistry()
        hostViewController = Mockito.mock(ViewController::class.java)

        activity = newActivity()
        tooltipView = spy(ViewTooltip.TooltipView(activity))
        tooltipView2 = spy(ViewTooltip.TooltipView(activity))
        whenever(hostViewController?.showAttachedOverlay(any(), any(), any()))
            .doReturnConsecutively(listOf(tooltipView,tooltipView2))
        anchorView = spy(View(activity))
        parent = FrameLayout(activity)
        parent.addView(anchorView)
        activity.setContentView(parent)
        uut = AttachedOverlayManager({ hostViewController }, { anchorView })
        idleMainLooper()

    }

    @Test
    fun `showTooltip - should call showTooltip of the found controller and calls success`() {
        val overlayAttachOptions = newAttachOptions()

        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        uut.show(tooltipController, overlayAttachOptions, commandListener)

        verify(hostViewController)?.showAnchoredOverlay(anchorView!!, overlayAttachOptions, tooltipController)
        verify(commandListener).onSuccess("TooltipId")
        verify(tooltipController).onViewDidAppear()
    }

    @Test
    fun `showTooltip - unfound host controller, should call listener error`() {
        val overlayAttachOptions = newAttachOptions()

        hostViewController = null
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        uut.show(tooltipController, overlayAttachOptions, commandListener)

        verify(commandListener).onError("Cannot find layout with id " + overlayAttachOptions.layoutId)
    }

    @Test
    fun `showTooltip - unfound anchor view, should call listener error`() {
        val overlayAttachOptions = newAttachOptions()

        anchorView = null
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        uut.show(tooltipController, overlayAttachOptions, commandListener)

        verify(commandListener).onError("Cannot find anchor view with id " + overlayAttachOptions.anchorId)
    }

    @Test
    fun `showTooltip - layout that cant show tooltip, should call listener error`() {
        val overlayAttachOptions = newAttachOptions()

        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        whenever(hostViewController?.showAnchoredOverlay(any(), any(), any())).thenReturn(null)
        uut.show(tooltipController, overlayAttachOptions, commandListener)

        verify(commandListener).onError("Parent could not create tooltip, it could be null parent")
    }

    @Test
    fun `showTooltip - should show multiple tooltips on same anchor`() {
        val overlayAttachOptions = newAttachOptions()

        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        val tooltipController2 = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId2", Options()))
        uut.show(tooltipController, overlayAttachOptions, commandListener)

        verify(hostViewController)?.showAnchoredOverlay(anchorView!!, overlayAttachOptions, tooltipController)
        verify(commandListener).onSuccess("TooltipId")
        verify(tooltipController).onViewDidAppear()

        uut.show(tooltipController2, overlayAttachOptions, commandListener)
        verify(hostViewController)?.showAnchoredOverlay(anchorView!!, overlayAttachOptions, tooltipController2)
        verify(commandListener).onSuccess("TooltipId2")
        verify(tooltipController2).onViewDidAppear()
    }

    @Test
    fun `contains - should return true for shown tooltip and false for not `() {
        val overlayAttachOptions = newAttachOptions()

        val id = "TooltipId"
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, id, Options()))
        val tooltipController2 = spy(SimpleComponentViewController(activity, childRegistry, id + "1", Options()))
        Java6Assertions.assertThat(id in uut).isFalse

        uut.show(tooltipController, overlayAttachOptions, commandListener)
        Java6Assertions.assertThat(id in uut).isTrue

        uut.show(tooltipController2, overlayAttachOptions, commandListener)
        Java6Assertions.assertThat(id + "1" in uut).isTrue

        uut.dismissAll(id, commandListener)
        Java6Assertions.assertThat(id in uut).isFalse
        Java6Assertions.assertThat(id + "1" in uut).isTrue
    }


    @Test
    fun `dismissTooltip - should call error when dismissing non existing tooltip`() {
        uut.dismissAll("imposterTooltip", commandListener)
        verify(commandListener).onError("Can't dismiss non-shown tooltip of id: imposterTooltip")
    }


    @Test
    fun `dismissTooltip - should hide the tooltip and destroy the controller `() {
        val overlayAttachOptions = newAttachOptions()

        val id = "TooltipId"
        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, id, Options()))
        Java6Assertions.assertThat(id in uut).isFalse

        uut.show(tooltipController, overlayAttachOptions, commandListener)
        val dismissCmdListener = Mockito.mock(CommandListener::class.java)

        uut.dismissAll(id, dismissCmdListener)


        verify(tooltipView)?.closeNow()
        verify(dismissCmdListener).onSuccess(id)
        verify(tooltipController).destroy()
    }

    @Test
    fun `dismissAllTooltips - should close all tooltips, destroy them and not keeping reference`() {
        val overlayAttachOptions = newAttachOptions()

        val tooltipController = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId", Options()))
        val tooltipController2 = spy(SimpleComponentViewController(activity, childRegistry, "TooltipId2", Options()))
        uut.show(tooltipController, overlayAttachOptions, commandListener)
        uut.show(tooltipController2, overlayAttachOptions, commandListener)

        Java6Assertions.assertThat("TooltipId" in uut).isTrue
        Java6Assertions.assertThat("TooltipId2" in uut).isTrue
        uut.dismissAll()

        verify(tooltipView)?.closeNow()
        verify(tooltipController).destroy()
        verify(tooltipView2)?.closeNow()
        verify(tooltipController2).destroy()
    }


    private fun newAttachOptions() = OverlayAttachOptions().apply {
        layoutId = Text("layoutId")
        anchorId = Text("anchorId")
        gravity = Text("top")
    }
}