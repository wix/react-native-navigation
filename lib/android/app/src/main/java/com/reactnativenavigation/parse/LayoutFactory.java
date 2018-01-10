package com.reactnativenavigation.parse;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.viewcontrollers.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.SideMenuController;
import com.reactnativenavigation.viewcontrollers.StackController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.views.ComponentViewCreator;
import com.reactnativenavigation.views.TopTabsLayoutCreator;

import java.util.ArrayList;
import java.util.List;

public class LayoutFactory {

	private final Activity activity;
	private final ReactInstanceManager reactInstanceManager;
    private Options defaultOptions;
    private final TypefaceLoader typefaceManager;

    public LayoutFactory(Activity activity, final ReactInstanceManager reactInstanceManager, Options defaultOptions) {
		this.activity = activity;
		this.reactInstanceManager = reactInstanceManager;
        this.defaultOptions = defaultOptions;
        typefaceManager = new TypefaceLoader(activity);
    }

	public ViewController create(final LayoutNode node) {
		switch (node.type) {
			case Component:
				return createComponent(node);
			case Stack:
				return createStack(node);
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
            case TopTabs:
                return createTopTabs(node);
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

	private ViewController createComponent(LayoutNode node) {
		String id = node.id;
		String name = node.data.optString("name");
		Options options = Options.parse(typefaceManager, node.data.optJSONObject("options"), defaultOptions);
		return new ComponentViewController(activity,
                id,
                name,
                new ComponentViewCreator(reactInstanceManager),
                options
        );
	}

	private ViewController createStack(LayoutNode node) {
		StackController stackController = new StackController(activity, node.id);
		for (LayoutNode child : node.children) {
			stackController.push(create(child), null);
		}
		return stackController;
	}

	private ViewController createBottomTabs(LayoutNode node) {
		final BottomTabsController tabsComponent = new BottomTabsController(activity, node.id);
		List<ViewController> tabs = new ArrayList<>();
		for (int i = 0; i < node.children.size(); i++) {
			tabs.add(create(node.children.get(i)));
		}
		tabsComponent.setTabs(tabs);
		return tabsComponent;
	}

    private ViewController createTopTabs(LayoutNode node) {
        final List<TopTabController> tabs = new ArrayList<>();
        for (int i = 0; i < node.children.size(); i++) {
            TopTabController tabController = (TopTabController) create(node.children.get(i));
            tabController.setTabIndex(i);
            tabs.add(tabController);
        }
        Options options = Options.parse(typefaceManager, node.getNavigationOptions(), defaultOptions);
        return new TopTabsController(activity, node.id, tabs, new TopTabsLayoutCreator(activity, tabs), options);
    }
}
