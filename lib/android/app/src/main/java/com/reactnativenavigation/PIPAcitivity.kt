package com.reactnativenavigation

import android.app.ActivityManager
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.reactnativenavigation.parse.PIPActionButton
import com.reactnativenavigation.react.NavigationModule
import com.reactnativenavigation.viewcontrollers.ViewController
import java.util.*
import javax.annotation.Nonnull


class PIPActivity : AppCompatActivity() {

    companion object {
        var INSTANCE: PIPActivity? = null
    }

    private lateinit var mContentView: View
    private var mFromPictureInPictureMode: Boolean = false
    private lateinit var mViewController: ViewController<ViewGroup>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pip)
        loadView()
        INSTANCE = this;
    }

    override fun onResume() {
        super.onResume()
        if (mFromPictureInPictureMode) {
            (mContentView.parent as ViewGroup).removeView(mContentView)
            mFromPictureInPictureMode = false;
            navToLauncherTask(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mFromPictureInPictureMode) {
            NavigationModule.currentNavigator.closePIP(null)
            mFromPictureInPictureMode = false;
        }
    }

    override fun onPictureInPictureModeChanged(
            isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        mFromPictureInPictureMode = !isInPictureInPictureMode
    }

    fun navToLauncherTask(@Nonnull appContext: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val activityManager = appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            // iterate app tasks available and navigate to launcher task (browse task)
            val appTasks = activityManager.appTasks
            for (task in appTasks) {
                val baseIntent = task.taskInfo.baseIntent
                val categories = baseIntent.categories
                if (categories != null && categories.contains(Intent.CATEGORY_LAUNCHER)) {
                    task.moveToFront()
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        loadView()
    }

    private fun loadView() {
        var view = findViewById<ViewGroup>(R.id.rootView)
        view.removeAllViews()
        mViewController = NavigationModule.currentNavigator.pipController
        mContentView = mViewController.view
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 0)
        mContentView.layoutParams = params
        view.addView(mContentView)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mPictureInPictureParamsBuilder = PictureInPictureParams.Builder()
            val options = mViewController.options.pipOptions
            val actions = getActionButtons(options.actionButtons)
            mPictureInPictureParamsBuilder.setActions(actions)
            mPictureInPictureParamsBuilder.setAspectRatio(Rational(options.aspectRatio.numerator.get(), options.aspectRatio.denominator.get()))
            enterPictureInPictureMode(mPictureInPictureParamsBuilder.build())
        }
    }

    private fun getActionButtons(actionButtons: Array<PIPActionButton>?): ArrayList<RemoteAction> {
        val actions = ArrayList<RemoteAction>()
        return actions
    }
}
