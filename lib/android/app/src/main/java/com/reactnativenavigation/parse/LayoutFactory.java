package com.reactnativenavigation.parse;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.SideMenuController;
import com.reactnativenavigation.viewcontrollers.StackController;
import com.reactnativenavigation.viewcontrollers.StackControllerBuilder;
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
    private final ChildControllersRegistry childRegistry;
	private final ReactInstanceManager reactInstanceManager;
    private EventEmitter eventEmitter;
    private Map<String, ExternalComponentCreator> externalComponentCreators;
    private Options defaultOptions;
    private final TypefaceLoader typefaceManager;

    public LayoutFactory(Activity activity, ChildControllersRegistry childRegistry, final ReactInstanceManager reactInstanceManager, EventEmitter eventEmitter, Map<String, ExternalComponentCreator> externalComponentCreators, Options defaultOptions) {
		this.activity = activity;
        this.childRegistry = childRegistry;
        this.reactInstanceManager = reactInstanceManager;
        this.eventEmitter = eventEmitter;
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
		SideMenuController sideMenuController = new SideMenuController(activity, childRegistry, node.id, parseNodeOptions(node));
		ViewController childControllerCenter = null, childControllerLeft = null, childControllerRight = null;

		for (LayoutNode child : node.children) {
			switch (child.type) {
				case SideMenuCenter:
					childControllerCenter = create(child);
					childControllerCenter.setParentController(sideMenuController);
					break;
				case SideMenuLeft:
					childControllerLeft = create(child);
					childControllerLeft.setParentController(sideMenuController);
					break;
				case SideMenuRight:
					childControllerRight = create(child);
					childControllerRight.setParentController(sideMenuController);
					break;
				default:
					throw new IllegalArgumentException("Invalid node type in sideMenu: " + node.type);
			}
		}

		// Need to set the center controller first, otherwise "onPress" events on the JS components
		// of the left and right drawers are not handled properly.
		//
		// See https://github.com/wix/react-native-navigation/issues/2835
		//
		if (childControllerCenter != null) {
			sideMenuController.setCenterController(childControllerCenter);
		}

		if (childControllerLeft != null) {
			sideMenuController.setLeftController(childControllerLeft);
		}

		if (childControllerRight != null) {
			sideMenuController.setRightController(childControllerRight);
		}

		return sideMenuController;
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
                childRegistry,
                id,
                name,
                new ComponentViewCreator(reactInstanceManager),
                parseNodeOptions(node)
        );
	}

    private ViewController createExternalComponent(LayoutNode node) {
        final ExternalComponent externalComponent = ExternalComponent.parse(node.data);
        return new ExternalComponentViewController(activity,
                node.id,
                externalComponent,
                externalComponentCreators.get(externalComponent.name.get()),
                reactInstanceManager,
                parseNodeOptions(node)
        );
    }

	private ViewController createStack(LayoutNode node) {
        StackController stackController = new StackControllerBuilder(activity)
                .setChildRegistry(childRegistry)
                .setTopBarButtonCreator(new TitleBarButtonCreator(reactInstanceManager))
                .setTitleBarReactViewCreator(new TitleBarReactViewCreator(reactInstanceManager))
                .setTopBarBackgroundViewController(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreator(reactInstanceManager)))
                .setTopBarController(new TopBarController())
                .setId(node.id)
                .setInitialOptions(parseNodeOptions(node))
                .createStackController();
        addChildrenToStack(node.children, stackController);
        return stackController;
	}

    private void addChildrenToStack(List<LayoutNode> children, StackController stackController) {
        for (LayoutNode child : children) {
            stackController.push(create(child), new CommandListenerAdapter());
        }
    }

    private ViewController createBottomTabs(LayoutNode node) {
        List<ViewController> tabs = new ArrayList<>();
        for (int i = 0; i < node.children.size(); i++) {
            tabs.add(create(node.children.get(i)));
        }
        return new BottomTabsController(activity, tabs, childRegistry, eventEmitter, new ImageLoader(), node.id, parseNodeOptions(node));
	}

    private ViewController createTopTabs(LayoutNode node) {
        final List<ViewController> tabs = new ArrayList<>();
        for (int i = 0; i < node.children.size(); i++) {
            ViewController tabController = create(node.children.get(i));
            Options options = parseNodeOptions(node.children.get(i));
            options.setTopTabIndex(i);
            tabs.add(tabController);
        }
        return new TopTabsController(activity, childRegistry, node.id, tabs, new TopTabsLayoutCreator(activity, tabs), parseNodeOptions(node));
    }

    private Options parseNodeOptions(LayoutNode node) {
        return Options.parse(typefaceManager, node.getNavigationOptions(), defaultOptions);
    }
}
