package com.reactnativenavigation.options;

import android.app.Activity;
import android.content.Context;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.Assertions;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.RenderChecker;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabPresenter;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsAnimator;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.BottomTabsAttacher;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.component.ComponentPresenter;
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentPresenter;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentViewController;
import com.reactnativenavigation.viewcontrollers.sidemenu.SideMenuController;
import com.reactnativenavigation.viewcontrollers.sidemenu.SideMenuPresenter;
import com.reactnativenavigation.viewcontrollers.stack.StackControllerBuilder;
import com.reactnativenavigation.viewcontrollers.stack.StackPresenter;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.component.ComponentViewCreator;
import com.reactnativenavigation.views.stack.topbar.TopBarBackgroundViewCreator;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.toptabs.TopTabsLayoutCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import static com.reactnativenavigation.options.Options.parse;
import static com.reactnativenavigation.utils.CollectionUtils.*;

import org.json.JSONObject;

public class LayoutFactory {
	private Activity activity;
	private ChildControllersRegistry childRegistry;
	private final ReactInstanceManager reactInstanceManager;
	private EventEmitter eventEmitter;
	private Map<String, ExternalComponentCreator> externalComponentCreators;
	private @NonNull Options defaultOptions = new Options();
	private TypefaceLoader typefaceManager;

	public void setDefaultOptions(@NonNull Options defaultOptions) {
		Assertions.assertNotNull(defaultOptions);
		this.defaultOptions = defaultOptions;
	}

	public LayoutFactory(final ReactInstanceManager reactInstanceManager) {
		this.reactInstanceManager = reactInstanceManager;
	}

	public void init(Activity activity, EventEmitter eventEmitter, ChildControllersRegistry childRegistry, Map<String, ExternalComponentCreator> externalComponentCreators) {
		this.activity = activity;
		this.eventEmitter = eventEmitter;
		this.childRegistry = childRegistry;
		this.externalComponentCreators = externalComponentCreators;
		typefaceManager = new TypefaceLoader(activity);
	}

	public ViewController<?> create(final LayoutNode node) {
		final ReactContext context = reactInstanceManager.getCurrentReactContext();
		switch (node.type) {
			case Component:
				return createComponent(node);
			case ExternalComponent:
				return createExternalComponent(context, node);
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

	private ViewController<?> createSideMenuRoot(LayoutNode node) {
		SideMenuController sideMenuController = new SideMenuController(activity,
				childRegistry,
				node.id,
				parseOptions( node.getOptions()),
				new SideMenuPresenter(),
				new Presenter(activity, defaultOptions)
		);
		ViewController<?> childControllerCenter = null, childControllerLeft = null, childControllerRight = null;

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

	private ViewController<?> createSideMenuContent(LayoutNode node) {
		return create(node.children.get(0));
	}

	private ViewController<?> createSideMenuLeft(LayoutNode node) {
		return create(node.children.get(0));
	}

	private ViewController<?> createSideMenuRight(LayoutNode node) {
		return create(node.children.get(0));
	}

	private ViewController<?> createComponent(LayoutNode node) {
		String id = node.id;
		String name = node.data.optString("name");
		return new ComponentViewController(activity,
				childRegistry,
				id,
				name,
				new ComponentViewCreator(reactInstanceManager),
				parseOptions(node.getOptions()),
				new Presenter(activity, defaultOptions),
				new ComponentPresenter(defaultOptions)
		);
	}

	private ViewController<?> createExternalComponent(ReactContext context, LayoutNode node) {
		final ExternalComponent externalComponent = ExternalComponent.parse(node.data);
		return new ExternalComponentViewController(activity,
				childRegistry,
				node.id,
				new Presenter(activity, defaultOptions),
				externalComponent,
				externalComponentCreators.get(externalComponent.name.get()),
				reactInstanceManager,
				new EventEmitter(context),
				new ExternalComponentPresenter(),
				parseOptions(node.getOptions())
		);
	}

	private ViewController<?> createStack(LayoutNode node) {
		return new StackControllerBuilder(activity, eventEmitter)
				.setChildren(createChildren(node.children))
				.setChildRegistry(childRegistry)
				.setTopBarController(new TopBarController())
				.setId(node.id)
				.setInitialOptions(parseOptions(node.getOptions()))
				.setStackPresenter(new StackPresenter(activity,
						new TitleBarReactViewCreator(reactInstanceManager),
						new TopBarBackgroundViewCreator(reactInstanceManager),
						new TitleBarButtonCreator(reactInstanceManager),
						new IconResolver(activity, new ImageLoader()),
						new TypefaceLoader(activity),
						new RenderChecker(),
						defaultOptions
				))
				.setPresenter(new Presenter(activity, defaultOptions))
				.build();
	}

	private List<ViewController<?>> createChildren(List<LayoutNode> children) {
		List<ViewController<?>> result = new ArrayList<>();
		for (LayoutNode child : children) {
			result.add(create(child));
		}
		return result;
	}

	private ViewController<?> createBottomTabs(LayoutNode node) {
		List<ViewController<?>> tabs = map(node.children, this::create);
		BottomTabsPresenter bottomTabsPresenter = new BottomTabsPresenter(tabs, defaultOptions, new BottomTabsAnimator());
		return new BottomTabsController(activity,
				tabs,
				childRegistry,
				eventEmitter,
				new ImageLoader(),
				node.id,
				parseOptions( node.getOptions()),
				new Presenter(activity, defaultOptions),
				new BottomTabsAttacher(tabs, bottomTabsPresenter, defaultOptions),
				bottomTabsPresenter,
				new BottomTabPresenter(activity, tabs, new ImageLoader(), new TypefaceLoader(activity), defaultOptions));
	}

	private ViewController<?> createTopTabs(LayoutNode node) {
		final List<ViewController<?>> tabs = new ArrayList<>();
		for (int i = 0; i < node.children.size(); i++) {
			ViewController<?> tabController = create(node.children.get(i));
			Options options = parseOptions(node.children.get(i).getOptions());
			options.setTopTabIndex(i);
			tabs.add(tabController);
		}
		return new TopTabsController(activity, childRegistry, node.id, tabs, new TopTabsLayoutCreator(activity, tabs)
				, parseOptions(node.getOptions()), new Presenter(activity, defaultOptions));
	}

    private Options parseOptions(JSONObject jsonOptions) {
        Context context = reactInstanceManager.getCurrentReactContext();
        if (context == null) {
            context = activity == null ? NavigationApplication.instance : activity;
        }
        if (typefaceManager == null) {
            typefaceManager = new TypefaceLoader(context);
        }
        return parse(context, typefaceManager, jsonOptions);
    }
	@NonNull
	@RestrictTo(RestrictTo.Scope.TESTS)
	public Options getDefaultOptions() {
		return defaultOptions;
	}
}
