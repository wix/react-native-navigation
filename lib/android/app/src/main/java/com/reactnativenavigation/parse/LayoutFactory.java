package com.reactnativenavigation.parse;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.ReactContainerViewCreator;
import com.reactnativenavigation.viewcontrollers.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.SideMenuController;
import com.reactnativenavigation.viewcontrollers.StackController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.overlay.DialogViewController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.views.ContainerViewCreator;
import com.reactnativenavigation.views.TopTabCreator;

import java.util.ArrayList;
import java.util.List;

public class LayoutFactory {

	private final Activity activity;
	private final ReactInstanceManager reactInstanceManager;
    private NavigationOptions defaultOptions;

    public LayoutFactory(Activity activity, final ReactInstanceManager reactInstanceManager, NavigationOptions defaultOptions) {
		this.activity = activity;
		this.reactInstanceManager = reactInstanceManager;
        this.defaultOptions = defaultOptions;
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
			case CustomDialog:
				return createDialogContainer(node);
            case TopTabsContainer:
                return createTopTabsContainer(node);
            case TopTab:
                return createTopTabContainer(node);
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
		NavigationOptions navigationOptions = NavigationOptions.parse(node.data.optJSONObject("navigationOptions"), defaultOptions);
		return new ContainerViewController(activity, id, name, new ContainerViewCreator(new ReactContainerViewCreator(reactInstanceManager)), navigationOptions);
	}

	private ViewController createContainerStack(LayoutNode node) {
		StackController stackController = new StackController(activity, node.id);
		for (LayoutNode child : node.children) {
			stackController.push(create(child), null);
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

	private ViewController createDialogContainer(LayoutNode node) {
		String id = node.id;
		String name = node.data.optString("name");
		ReactContainerViewCreator creator = new ReactContainerViewCreator(reactInstanceManager);
		return new DialogViewController(activity, id, name, creator);
	}

    private ViewController createTopTabsContainer(LayoutNode node) {
        final List<ViewController> tabs = new ArrayList<>();
        for (LayoutNode child : node.children) {
            tabs.add(create(child));
        }
        return new TopTabsController(activity, node.id, tabs);
    }

    private TopTabController createTopTabContainer(LayoutNode node) {
        String name = node.data.optString("name");
        return new TopTabController(activity,
                node.id,
                name,
                new TopTabCreator(new ReactContainerViewCreator(reactInstanceManager)));
    }
}
