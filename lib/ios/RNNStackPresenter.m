#import "RNNStackPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "RNNStackController.h"
#import "RNNCustomTitleView.h"
#import "TopBarPresenterCreator.h"
#import "RNNReactBackgroundView.h"
#import "InteractivePopGestureDelegate.h"

@interface RNNStackPresenter() {
    RNNReactComponentRegistry* _componentRegistry;
    UIView* _customTopBarBackground;
    RNNReactView* _topBarBackgroundReactView;
    TopBarPresenter* _topBarPresenter;
    InteractivePopGestureDelegate *_interactivePopGestureDelegate;
}

@property (nonatomic, weak) RNNStackController* stackController;

@end
@implementation RNNStackPresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry defaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super initWithDefaultOptions:defaultOptions];
    _componentRegistry = componentRegistry;
    _interactivePopGestureDelegate = [InteractivePopGestureDelegate new];
    return self;
}

- (void)bindViewController:(UINavigationController *)boundViewController {
    [super bindViewController:boundViewController];
    _topBarPresenter = [TopBarPresenterCreator createWithBoundedNavigationController:self.stackController];
    _interactivePopGestureDelegate.navigationController = boundViewController;
    _interactivePopGestureDelegate.originalDelegate = boundViewController.interactivePopGestureRecognizer.delegate;
}

- (void)componentDidAppear {
    [_topBarBackgroundReactView componentDidAppear];
    [_topBarPresenter componentDidAppear];
}

- (void)componentDidDisappear {
    [_topBarBackgroundReactView componentDidDisappear];
}

- (RNNStackController *)stackController {
    return (RNNStackController *)self.boundViewController;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    [super applyOptions:options];
    RNNStackController* stack = self.stackController;
    RNNNavigationOptions * withDefault = [options withDefault:[self defaultOptions]];
    
    [_interactivePopGestureDelegate setEnabled:[withDefault.popGesture getWithDefaultValue:YES]];
    stack.interactivePopGestureRecognizer.delegate = _interactivePopGestureDelegate;

    [stack setBarStyle:[RCTConvert UIBarStyle:[withDefault.topBar.barStyle getWithDefaultValue:@"default"]]];
    [stack setRootBackgroundImage:[withDefault.rootBackgroundImage getWithDefaultValue:nil]];
    [stack setNavigationBarTestId:[withDefault.topBar.testID getWithDefaultValue:nil]];
    [stack setNavigationBarVisible:[withDefault.topBar.visible getWithDefaultValue:YES] animated:[withDefault.topBar.animate getWithDefaultValue:YES]];
    [stack hideBarsOnScroll:[withDefault.topBar.hideOnScroll getWithDefaultValue:NO]];
    
    [_topBarPresenter applyOptions:withDefault.topBar];
    
    [stack setNavigationBarBlur:[withDefault.topBar.background.blur getWithDefaultValue:NO]];
    [stack setNavigationBarClipsToBounds:[withDefault.topBar.background.clipToBounds getWithDefaultValue:NO]];
	
	[stack.view setBackgroundColor:[withDefault.layout.backgroundColor getWithDefaultValue:nil]];
}

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options {
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    if (withDefault.topBar.background.component.name.hasValue) {
        [self presentBackgroundComponent];
    }
}

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options {
    [_topBarPresenter applyOptionsBeforePopping:options.topBar];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    [super mergeOptions:options resolvedOptions:resolvedOptions];
    RNNStackController* stack = self.stackController;
    
    if (options.popGesture.hasValue) {
        [_interactivePopGestureDelegate setEnabled:options.popGesture.get];
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
    
    if (options.topBar.barStyle.hasValue) {
        [stack setBarStyle:[RCTConvert UIBarStyle:options.topBar.barStyle.get]];
    }
    
    if (options.topBar.background.clipToBounds.hasValue) {
        [stack setNavigationBarClipsToBounds:[options.topBar.background.clipToBounds get]];
    }
    
    if (options.topBar.background.blur.hasValue) {
        [stack setNavigationBarBlur:[options.topBar.background.blur get]];
    }

    if (options.topBar.background.component.name.hasValue) {
        [self setCustomNavigationComponentBackground:options perform:nil];
    }
	
	if (options.layout.backgroundColor.hasValue) {
		[stack.view setBackgroundColor:options.layout.backgroundColor.get];
	}
    
    RNNNavigationOptions * withDefault = (RNNNavigationOptions *) [[options mergeInOptions:resolvedOptions] withDefault:[self defaultOptions]];
    [_topBarPresenter mergeOptions:options.topBar withDefault:withDefault.topBar];
}

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
        dispatch_group_t group = dispatch_group_create();
        
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

- (void)setCustomNavigationComponentBackground:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    RNNStackController* stack = self.stackController;
    if (![withDefault.topBar.background.component.waitForRender getWithDefaultValue:NO] && readyBlock) {
        readyBlock();
        readyBlock = nil;
    }
    
    if (withDefault.topBar.background.component.name.hasValue) {
        NSString* currentChildComponentId = [stack getCurrentChild].layoutInfo.componentId;
        _topBarBackgroundReactView = [_componentRegistry createComponentIfNotExists:withDefault.topBar.background.component parentComponentId:currentChildComponentId componentType:RNNComponentTypeTopBarBackground reactViewReadyBlock:readyBlock];
        
    } else {
        [_topBarBackgroundReactView componentDidDisappear];
        [_customTopBarBackground removeFromSuperview];
        _customTopBarBackground = nil;
        if (readyBlock) {
            readyBlock();
        }
    }
}

- (void)presentBackgroundComponent {
    RNNStackController* stack = self.stackController;
    if (_customTopBarBackground) {
        [_customTopBarBackground removeFromSuperview];
    }
    _customTopBarBackground = [[RNNCustomTitleView alloc] initWithFrame:stack.navigationBar.bounds subView:_topBarBackgroundReactView alignment:@"fill"];
    
    [stack.navigationBar insertSubview:_customTopBarBackground atIndex:1];
    [_topBarBackgroundReactView componentDidAppear];
}

- (void)dealloc {
    [_componentRegistry removeComponent:self.boundComponentId];
}

@end
