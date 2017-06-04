package com.reactnativenavigation.layout;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.SideMenuController;
import com.reactnativenavigation.viewcontrollers.StackController;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.ArrayList;
import java.util.List;

public class LayoutFactory {

	private final Activity activity;
	private final ReactInstanceManager reactInstanceManager;

	public LayoutFactory(Activity activity, final ReactInstanceManager reactInstanceManager) {
		this.activity = activity;
		this.reactInstanceManager = reactInstanceManager;
	}

	public ViewController create(final LayoutNode node) {
		switch (node.type) {
			case Container:
				return createContainer(node);
			case ContainerStack:
				return createContainerStack(node);
			case BottomTabs:
				return createBottomTabs(node);
			case SideMenuRoot:
				return createSideMenuRoot(node);
			case SideMenuCenter:
				return createSideMenuContent(node);
			case SideMenuLeft:
				return createSideMenuLeft(node);
			case SideMenuRight:
				return createSideMenuRight(node);
			default:
				throw new IllegalArgumentException("Invalid node type: " + node.type);
		}
	}

	private ViewController createSideMenuRoot(LayoutNode node) {
		SideMenuController sideMenuLayout = new SideMenuController(activity, node.id);
		for (LayoutNode child : node.children) {
			ViewController childLayout = create(child);
			switch (child.type) {
				case SideMenuCenter:
					sideMenuLayout.setCenterController(childLayout);
					break;
				case SideMenuLeft:
					sideMenuLayout.setLeftController(childLayout);
					break;
				case SideMenuRight:
					sideMenuLayout.setRightController(childLayout);
					break;
				default:
					throw new IllegalArgumentException("Invalid node type in sideMenu: " + node.type);
			}
		}
		return sideMenuLayout;
	}

	private ViewController createSideMenuContent(LayoutNode node) {
		return create(node.children.get(0));
	}

	private ViewController createSideMenuLeft(LayoutNode node) {
		return create(node.children.get(0));
	}

	private ViewController createSideMenuRight(LayoutNode node) {
		return create(node.children.get(0));
	}

	private ViewController createContainer(LayoutNode node) {
		String id = node.id;
		String name = node.data.optString("name");
		NavigationOptions navigationOptions = NavigationOptions.parse(node.data.optJSONObject("navigationOptions"));
		return new ReactRootViewController(activity, id, name, navigationOptions, reactInstanceManager);
	}

	private ViewController createContainerStack(LayoutNode node) {
		StackController stackController = new StackController(activity, node.id);
		for (LayoutNode child : node.children) {
			stackController.push(create(child));
		}
		return stackController;
	}

	private ViewController createBottomTabs(LayoutNode node) {
		final BottomTabsController tabsContainer = new BottomTabsController(activity, node.id);
		List<ViewController> tabs = new ArrayList<>();
		for (int i = 0; i < node.children.size(); i++) {
			tabs.add(create(node.children.get(i)));
		}
		tabsContainer.setTabs(tabs);
		return tabsContainer;
	}
}
