package com.reactnativenavigation.views.animations

import android.animation.Animator
import android.view.View
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.AnimationOptions
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class BaseViewAnimatorTest : BaseTest() {
    private lateinit var uut: BaseViewAnimator<View>
    private lateinit var view: View
    private lateinit var defaultAnimatorCreator: ViewAnimatorCreator

    private val defaultShowAnimator: Animator
        get() = defaultAnimatorCreator.getShowAnimator(view, BaseViewAnimator.HideDirection.Up, 0f)
    private val defaultHideAnimator: Animator
        get() = defaultAnimatorCreator.getHideAnimator(view, BaseViewAnimator.HideDirection.Up, 0f)


    override fun beforeEach() {
        view = View(newActivity())
        defaultAnimatorCreator = DefaultViewAnimatorCreatorFake()
        uut = BaseViewAnimator(
                hideDirection = BaseViewAnimator.HideDirection.Up,
                view = view,
                defaultAnimatorCreator = defaultAnimatorCreator
        )
    }

    @Test
    fun show() {
        view.visibility = View.GONE

        assertNotAnimating()
        uut.show()
        assertAnimatingShow()
    }

    @Test
    fun show_doesNothingIsAlreadyAnimatingShow() {
        view.visibility = View.GONE
        uut.show()

        val animator = uut.showAnimator
        uut.show()
        assertThat(animator).isEqualTo(uut.showAnimator)
    }

    @Test
    fun show_updatesTranslationDy() {
        view.visibility = View.GONE
        val options = spy<AnimationOptions>()
        whenever(options.hasValue()).thenReturn(true)

        uut.show(options, 123f)
        verify(options).setValueDy(View.TRANSLATION_Y, -123f, 0f)
    }

    @Test
    fun show_usesDefaultShowAnimatorIfAnimationIsNotSpecifiedInOptions() {
        view.visibility = View.GONE
        uut.show(mock())
        assertThat(uut.showAnimator).isEqualTo(defaultShowAnimator)
    }

    @Test
    fun hide() {
        assertNotAnimating()
        val onAnimationEnd = mock<Runnable>()
        uut.hide(mock(), 0f, onAnimationEnd)
        assertAnimatingHide()

        uut.hideAnimator.end()
        verify(onAnimationEnd).run()
    }

    @Test
    fun hide_doesNothingIfAlreadyAnimatingHide() {
        uut.hide(mock())

        val animator = uut.hideAnimator
        uut.hide(mock())
        assertThat(animator).isEqualTo(uut.hideAnimator)
    }

    @Test
    fun hide_updatesTranslationDy() {
        val options = spy<AnimationOptions>()
        whenever(options.hasValue()).thenReturn(true)

        uut.hide(options, 123f)
        verify(options).setValueDy(View.TRANSLATION_Y, 0f, -123f)
    }

    @Test
    fun hide_usesDefaultHideAnimatorIfAnimationIsNotSpecifiedInOptions() {
        uut.hide(mock())
        assertThat(uut.hideAnimator).isEqualTo(defaultHideAnimator)
    }

    private fun assertNotAnimating() = assertThat(uut.isAnimatingHide() && uut.isAnimatingShow()).isFalse()
    private fun assertAnimatingShow() = assertThat(uut.isAnimatingShow() && !uut.isAnimatingHide()).isTrue()
    private fun assertAnimatingHide() = assertThat(uut.isAnimatingHide() && !uut.isAnimatingShow()).isTrue()
}