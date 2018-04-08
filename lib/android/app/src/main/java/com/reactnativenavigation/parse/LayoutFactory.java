package com.reactnativenavigation.parse;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.SideMenuController;
import com.reactnativenavigation.viewcontrollers.StackController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.views.ComponentViewCreator;
import com.reactnativenavigation.views.titlebar.TitleBarButtonCreator;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;
import com.reactnativenavigation.views.toptabs.TopTabsLayoutCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayoutFactory {

	private final Activity activity;
	private final ReactInstanceManager reactInstanceManager;
    private Map<String, ExternalComponentCreator> externalComponentCreators;
    private Options defaultOptions;
    private final TypefaceLoader typefaceManager;

    public LayoutFactory(Activity activity, final ReactInstanceManager reactInstanceManager, Map<String, ExternalComponentCreator> externalComponentCreators, Options defaultOptions) {
		this.activity = activity;
		this.reactInstanceManager = reactInstanceManager;
        this.externalComponentCreators = externalComponentCreators;
        this.defaultOptions = defaultOptions;
        typefaceManager = new TypefaceLoader(activity);
    }

	public ViewController create(final LayoutNode node) {
		switch (node.type) {
			case Component:
				return createComponent(node);
            case ExternalComponent:
                return createExternalComponent(node);
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
        SideMenuController sideMenuLayout = new SideMenuController(activity, node.id, getOptions(node));
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
        return new ComponentViewController(activity,
                id,
                name,
                new ComponentViewCreator(reactInstanceManager),
                getOptions(node)
        );
	}

    private ViewController createExternalComponent(LayoutNode node) {
        final ExternalComponent externalComponent = ExternalComponent.parse(node.data);
        return new ExternalComponentViewController(activity,
                node.id,
                externalComponent,
                externalComponentCreators.get(externalComponent.name.get()),
                reactInstanceManager,
                getOptions(node)
        );
    }

	private ViewController createStack(LayoutNode node) {
        StackController stackController = new StackController(activity,
                new TitleBarButtonCreator(reactInstanceManager),
                new TitleBarReactViewCreator(reactInstanceManager),
                new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreator(reactInstanceManager)),
                new TopBarController(),
                node.id,
                getOptions(node)
        );
        addChildrenToStack(node.children, stackController);
        return stackController;
	}

    private void addChildrenToStack(List<LayoutNode> children, StackController stackController) {
        for (LayoutNode child : children) {
            stackController.push(create(child));
        }
    }

    private ViewController createBottomTabs(LayoutNode node) {
        final BottomTabsController tabsComponent = new BottomTabsController(activity, new ImageLoader(), node.id, getOptions(node));
		List<ViewController> tabs = new ArrayList<>();
		for (int i = 0; i < node.children.size(); i++) {
            tabs.add(create(node.children.get(i)));
		}
		tabsComponent.setTabs(tabs);
		return tabsComponent;
	}

    private ViewController createTopTabs(LayoutNode node) {
        final List<ViewController> tabs = new ArrayList<>();
        for (int i = 0; i < node.children.size(); i++) {
            ViewController tabController = create(node.children.get(i));
            Options options = getOptions(node.children.get(i));
            options.setTopTabIndex(i);
            tabs.add(tabController);
        }
        return new TopTabsController(activity, node.id, tabs, new TopTabsLayoutCreator(activity, tabs), getOptions(node));
    }

    private Options getOptions(LayoutNode node) {
        return Options.parse(typefaceManager, node.getNavigationOptions(), defaultOptions);
    }
}
