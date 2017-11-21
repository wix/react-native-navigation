package com.aurelhubert.ahbottomnavigation;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 */
public class AHBottomNavigationFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

	private int navigationBarHeight = 0;
	private long lastSnackbarUpdate = 0;

	public AHBottomNavigationFABBehavior(int navigationBarHeight) {
		this.navigationBarHeight = navigationBarHeight;
	}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
		if (dependency != null && dependency instanceof Snackbar.SnackbarLayout) {
			return true;
		} else if (dependency != null && dependency instanceof AHBottomNavigation) {
			return true;
		}
		return super.layoutDependsOn(parent, child, dependency);
	}

	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
		updateFloatingActionButton(child, dependency);
		return super.onDependentViewChanged(parent, child, dependency);
	}

	/**
	 * Update floating action button bottom margin
	 */
	private void updateFloatingActionButton(FloatingActionButton child, View dependency) {
		if (child != null && dependency != null && dependency instanceof Snackbar.SnackbarLayout) {
			lastSnackbarUpdate = System.currentTimeMillis();
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
			int fabDefaultBottomMargin = p.bottomMargin;
			child.setY(dependency.getY() - fabDefaultBottomMargin);
		} else if (child != null && dependency != null && dependency instanceof AHBottomNavigation) {
			// Hack to avoid moving the FAB when the AHBottomNavigation is moving (showing or hiding animation)
			if (System.currentTimeMillis() - lastSnackbarUpdate < 30) {
				return;
			}
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
			int fabDefaultBottomMargin = p.bottomMargin;
			child.setY(dependency.getY() - fabDefaultBottomMargin);
		}
	}

}