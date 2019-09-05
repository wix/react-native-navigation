#import "RNNStackPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "RNNStackController.h"
#import "RNNCustomTitleView.h"

@interface RNNStackPresenter() {
	RNNReactComponentRegistry* _componentRegistry;
	UIView* _customTopBar;
	UIView* _customTopBarBackground;
	RNNReactView* _customTopBarBackgroundReactView;
}

@end
@implementation RNNStackPresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry:(RNNNavigationOptions *)defaultOptions {
	self = [super initWithDefaultOptions:defaultOptions];
	_componentRegistry = componentRegistry;
	return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	RNNStackController* stack = self.boundViewController;
	RNNNavigationOptions * withDefault = [options withDefault:[self defaultOptions]];
	
	self.interactivePopGestureDelegate = [InteractivePopGestureDelegate new];
	self.interactivePopGestureDelegate.navigationController = stack;
	self.interactivePopGestureDelegate.originalDelegate = stack.interactivePopGestureRecognizer.delegate;
	stack.interactivePopGestureRecognizer.delegate = self.interactivePopGestureDelegate;

	[stack setInteractivePopGestureEnabled:[withDefault.popGesture getWithDefaultValue:YES]];
	[stack setRootBackgroundImage:[withDefault.rootBackgroundImage getWithDefaultValue:nil]];
	[stack setNavigationBarTestId:[withDefault.topBar.testID getWithDefaultValue:nil]];
	[stack setNavigationBarVisible:[withDefault.topBar.visible getWithDefaultValue:YES] animated:[withDefault.topBar.animate getWithDefaultValue:YES]];
	[stack hideBarsOnScroll:[withDefault.topBar.hideOnScroll getWithDefaultValue:NO]];
	[stack setNavigationBarNoBorder:[withDefault.topBar.noBorder getWithDefaultValue:NO]];
	[stack setBarStyle:[RCTConvert UIBarStyle:[withDefault.topBar.barStyle getWithDefaultValue:@"default"]]];
	[stack setNavigationBarTranslucent:[withDefault.topBar.background.translucent getWithDefaultValue:NO]];
	[stack setNavigationBarClipsToBounds:[withDefault.topBar.background.clipToBounds getWithDefaultValue:NO]];
	[stack setNavigationBarBlur:[withDefault.topBar.background.blur getWithDefaultValue:NO]];
	[stack setTopBarBackgroundColor:[withDefault.topBar.background.color getWithDefaultValue:nil]];
	[stack setNavigationBarLargeTitleVisible:[withDefault.topBar.largeTitle.visible getWithDefaultValue:NO]];
	[stack setNavigationBarLargeTitleFontFamily:[withDefault.topBar.largeTitle.fontFamily getWithDefaultValue:nil] fontSize:[withDefault.topBar.largeTitle.fontSize getWithDefaultValue:nil] color:[withDefault.topBar.largeTitle.color getWithDefaultValue:nil]];
	[stack setNavigationBarFontFamily:[withDefault.topBar.title.fontFamily getWithDefaultValue:nil] fontSize:[withDefault.topBar.title.fontSize getWithDefaultValue:nil] color:[withDefault.topBar.title.color getWithDefaultValue:nil]];
	[stack setBackButtonColor:[withDefault.topBar.backButton.color getWithDefaultValue:nil]];
}

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options {
	RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
	if (withDefault.topBar.background.component.name.hasValue) {
		[self presentBackgroundComponent];
	}
}

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options {
	RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
	RNNStackController* navigationController = self.boundViewController;
	[navigationController setTopBarBackgroundColor:[withDefault.topBar.background.color getWithDefaultValue:nil]];
	[navigationController setNavigationBarFontFamily:[withDefault.topBar.title.fontFamily getWithDefaultValue:nil] fontSize:[withDefault.topBar.title.fontSize getWithDefaultValue:nil] color:[withDefault.topBar.title.color getWithDefaultValue:[UIColor blackColor]]];
	[navigationController setNavigationBarLargeTitleVisible:[withDefault.topBar.largeTitle.visible getWithDefaultValue:NO]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    [super mergeOptions:options resolvedOptions:resolvedOptions];
	RNNStackController* stack = self.boundViewController;

	if (options.popGesture.hasValue) {
		[stack setInteractivePopGestureEnabled:options.popGesture.get];
	}
	
	if (options.rootBackgroundImage.hasValue) {
		[stack setRootBackgroundImage:options.rootBackgroundImage.get];
	}
	
	if (options.topBar.testID.hasValue) {
		[stack setNavigationBarTestId:options.topBar.testID.get];
	}
	
	if (options.topBar.visible.hasValue) {
		[stack setNavigationBarVisible:options.topBar.visible.get animated:[options.topBar.animate getWithDefaultValue:YES]];
	}
	
	if (options.topBar.hideOnScroll.hasValue) {
		[stack hideBarsOnScroll:[options.topBar.hideOnScroll get]];
	}
	
	if (options.topBar.noBorder.hasValue) {
		[stack setNavigationBarNoBorder:[options.topBar.noBorder get]];
	}
	
	if (options.topBar.barStyle.hasValue) {
		[stack setBarStyle:[RCTConvert UIBarStyle:options.topBar.barStyle.get]];
	}
	
	if (options.topBar.background.translucent.hasValue) {
		[stack setNavigationBarTranslucent:[options.topBar.background.translucent get]];
	}
	
	if (options.topBar.background.clipToBounds.hasValue) {
		[stack setNavigationBarClipsToBounds:[options.topBar.background.clipToBounds get]];
	}
	
	if (options.topBar.background.blur.hasValue) {
		[stack setNavigationBarBlur:[options.topBar.background.blur get]];
	}
	
	if (options.topBar.background.color.hasValue) {
		[stack setTopBarBackgroundColor:options.topBar.background.color.get];
	}
	
	if (options.topBar.largeTitle.visible.hasValue) {
		[stack setNavigationBarLargeTitleVisible:options.topBar.largeTitle.visible.get];
	}
	
	if (options.topBar.backButton.color.hasValue) {
		[stack setBackButtonColor:options.topBar.backButton.color.get];
	}

	RNNLargeTitleOptions *largeTitleOptions = options.topBar.largeTitle;
	if (largeTitleOptions.color.hasValue || largeTitleOptions.fontSize.hasValue || largeTitleOptions.fontFamily.hasValue) {
		[stack setNavigationBarLargeTitleFontFamily:[options.topBar.largeTitle.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.largeTitle.fontSize getWithDefaultValue:nil] color:[options.topBar.largeTitle.color getWithDefaultValue:nil]];
	}

	RNNNavigationOptions * withDefault = (RNNNavigationOptions *) [[options mergeInOptions:resolvedOptions] withDefault:[self defaultOptions]];
	[stack setNavigationBarFontFamily:[withDefault.topBar.title.fontFamily getWithDefaultValue:nil]
							 fontSize:[withDefault.topBar.title.fontSize getWithDefaultValue:nil]
								color:[withDefault.topBar.title.color getWithDefaultValue:nil]];
	
	if (options.topBar.component.name.hasValue) {
		[self setCustomNavigationBarView:options perform:nil];
	}
	
	if (options.topBar.background.component.name.hasValue) {
		[self setCustomNavigationComponentBackground:options perform:nil];
	}
}

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
		dispatch_group_t group = dispatch_group_create();
		
		dispatch_group_enter(group);
		dispatch_async(dispatch_get_main_queue(), ^{
			[self setCustomNavigationBarView:options perform:^{
				dispatch_group_leave(group);
			}];
		});
		
		dispatch_group_enter(group);
		dispatch_async(dispatch_get_main_queue(), ^{
			[self setCustomNavigationComponentBackground:options perform:^{
				dispatch_group_leave(group);
			}];
		});
		
		dispatch_group_wait(group, DISPATCH_TIME_FOREVER);
		
		dispatch_async(dispatch_get_main_queue(), ^{
			if (readyBlock) {
				readyBlock();
			}
		});
	});
}

- (void)setCustomNavigationBarView:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	RNNStackController* stack = self.boundViewController;
	if (![options.topBar.component.waitForRender getWithDefaultValue:NO] && readyBlock) {
		readyBlock();
		readyBlock = nil;
	}
	if (options.topBar.component.name.hasValue) {
		NSString* currentChildComponentId = [stack getCurrentChild].layoutInfo.componentId;
		RCTRootView *reactView = [_componentRegistry createComponentIfNotExists:options.topBar.component parentComponentId:currentChildComponentId reactViewReadyBlock:readyBlock];
		
		if (_customTopBar) {
			[_customTopBar removeFromSuperview];
		}
		_customTopBar = [[RNNCustomTitleView alloc] initWithFrame:stack.navigationBar.bounds subView:reactView alignment:@"fill"];
		reactView.backgroundColor = UIColor.clearColor;
		_customTopBar.backgroundColor = UIColor.clearColor;
		[stack.navigationBar addSubview:_customTopBar];
	} else {
		[_customTopBar removeFromSuperview];
		_customTopBar = nil;
		if (readyBlock) {
			readyBlock();
		}
	}
}

- (void)setCustomNavigationComponentBackground:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	RNNStackController* stack = self.boundViewController;
	if (![options.topBar.background.component.waitForRender getWithDefaultValue:NO] && readyBlock) {
		readyBlock();
		readyBlock = nil;
	}
	if (options.topBar.background.component.name.hasValue) {
		NSString* currentChildComponentId = [stack getCurrentChild].layoutInfo.componentId;
		RNNReactView *reactView = [_componentRegistry createComponentIfNotExists:options.topBar.background.component parentComponentId:currentChildComponentId reactViewReadyBlock:readyBlock];
		_customTopBarBackgroundReactView = reactView;
		
	} else {
		[_customTopBarBackground removeFromSuperview];
		_customTopBarBackground = nil;
		if (readyBlock) {
			readyBlock();
		}
	}
}

- (void)presentBackgroundComponent {
	RNNStackController* stack = self.boundViewController;
	if (_customTopBarBackground) {
		[_customTopBarBackground removeFromSuperview];
	}
	RNNCustomTitleView* customTopBarBackground = [[RNNCustomTitleView alloc] initWithFrame:stack.navigationBar.bounds subView:_customTopBarBackgroundReactView alignment:@"fill"];
	_customTopBarBackground = customTopBarBackground;
	
	[stack.navigationBar insertSubview:_customTopBarBackground atIndex:1];
}

- (void)dealloc {
	[_componentRegistry removeComponent:self.boundComponentId];
}

@end
