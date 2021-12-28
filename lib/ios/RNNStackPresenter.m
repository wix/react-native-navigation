#import "RNNStackPresenter.h"
#import "InteractivePopGestureDelegate.h"
#import "RNNCustomTitleView.h"
#import "RNNReactBackgroundView.h"
#import "RNNStackController.h"
#import "TopBarPresenterCreator.h"
#import "UINavigationController+RNNOptions.h"

@interface RNNStackPresenter () {
    RNNReactComponentRegistry *_componentRegistry;
    UIView *_customTopBarBackground;
    RNNReactView *_topBarBackgroundReactView;
    TopBarPresenter *_topBarPresenter;
    InteractivePopGestureDelegate *_interactivePopGestureDelegate;
}

@property(nonatomic, weak) RNNStackController *stackController;

@end
@implementation RNNStackPresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super initWithDefaultOptions:defaultOptions];
    _componentRegistry = componentRegistry;
    _interactivePopGestureDelegate = [InteractivePopGestureDelegate new];
    return self;
}

- (void)bindViewController:(UINavigationController *)boundViewController {
    [super bindViewController:boundViewController];
    _topBarPresenter =
        [TopBarPresenterCreator createWithBoundedNavigationController:self.stackController];
    _interactivePopGestureDelegate.navigationController = boundViewController;
    _interactivePopGestureDelegate.originalDelegate =
        boundViewController.interactivePopGestureRecognizer.delegate;
}

- (void)componentWillAppear {
    [_topBarBackgroundReactView componentWillAppear];
    [_topBarPresenter componentWillAppear];
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

- (BOOL)shouldPopItem:(UINavigationItem *)item options:(RNNNavigationOptions *)options {
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    return [withDefault.topBar.backButton.popStackOnPress withDefault:YES];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    [super applyOptions:options];
    RNNStackController *stack = self.stackController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];

    [_interactivePopGestureDelegate setEnabled:[withDefault.popGesture withDefault:YES]];
    stack.interactivePopGestureRecognizer.delegate = _interactivePopGestureDelegate;

    [stack
        setBarStyle:[RCTConvert UIBarStyle:[withDefault.topBar.barStyle withDefault:@"default"]]];
    [stack setRootBackgroundImage:[withDefault.rootBackgroundImage withDefault:nil]];
    [stack setNavigationBarTestId:[withDefault.topBar.testID withDefault:nil]];
    [stack setNavigationBarVisible:[withDefault.topBar.visible withDefault:YES]
                          animated:[withDefault.topBar.animate withDefault:YES]];
    [stack hideBarsOnScroll:[withDefault.topBar.hideOnScroll withDefault:NO]];

    [_topBarPresenter applyOptions:withDefault.topBar];

    [stack setNavigationBarBlur:[withDefault.topBar.background.blur withDefault:NO]];
    [stack
        setNavigationBarClipsToBounds:[withDefault.topBar.background.clipToBounds withDefault:NO]];

    [stack.view setBackgroundColor:[withDefault.layout.backgroundColor withDefault:nil]];
    if (options.topBar.background.component.name.hasValue) {
        [self setCustomNavigationComponentBackground:options perform:nil];
    }
}

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options {
    [super applyOptionsOnViewDidLayoutSubviews:options];
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    if (withDefault.topBar.background.component.name.hasValue) {
        [self presentBackgroundComponent];
    }
}

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options {
    [_topBarPresenter applyOptionsBeforePopping:options.topBar];
}

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    [super mergeOptions:mergeOptions resolvedOptions:resolvedOptions];
    RNNStackController *stack = self.stackController;

    if (mergeOptions.popGesture.hasValue) {
        [_interactivePopGestureDelegate setEnabled:mergeOptions.popGesture.get];
    }

    if (mergeOptions.rootBackgroundImage.hasValue) {
        [stack setRootBackgroundImage:mergeOptions.rootBackgroundImage.get];
    }

    if (mergeOptions.topBar.testID.hasValue) {
        [stack setNavigationBarTestId:mergeOptions.topBar.testID.get];
    }

    if (mergeOptions.topBar.visible.hasValue) {
        [stack setNavigationBarVisible:mergeOptions.topBar.visible.get
                              animated:[mergeOptions.topBar.animate withDefault:YES]];
    }

    if (mergeOptions.topBar.hideOnScroll.hasValue) {
        [stack hideBarsOnScroll:[mergeOptions.topBar.hideOnScroll get]];
    }

    if (mergeOptions.topBar.barStyle.hasValue) {
        [stack setBarStyle:[RCTConvert UIBarStyle:mergeOptions.topBar.barStyle.get]];
    }

    if (mergeOptions.topBar.background.clipToBounds.hasValue) {
        [stack setNavigationBarClipsToBounds:[mergeOptions.topBar.background.clipToBounds get]];
    }

    if (mergeOptions.topBar.background.blur.hasValue) {
        [stack setNavigationBarBlur:[mergeOptions.topBar.background.blur get]];
    }

    if (mergeOptions.topBar.background.component.name.hasValue) {
        [self setCustomNavigationComponentBackground:mergeOptions perform:nil];
    }

    if (mergeOptions.layout.backgroundColor.hasValue) {
        [stack.view setBackgroundColor:mergeOptions.layout.backgroundColor.get];
    }

    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[resolvedOptions
        mergeOptions:mergeOptions] withDefault:[self defaultOptions]];
    [_topBarPresenter mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];

    if (mergeOptions.topBar.backButton.visible.hasValue &&
        withDefault.topBar.backButton.testID.hasValue) {
        [stack setBackButtonTestID:withDefault.topBar.backButton.testID.get];
    }
}

- (void)renderComponents:(RNNNavigationOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
      dispatch_group_t group = dispatch_group_create();

      dispatch_group_enter(group);
      dispatch_async(dispatch_get_main_queue(), ^{
        [self setCustomNavigationComponentBackground:options
                                             perform:^{
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

- (void)setCustomNavigationComponentBackground:(RNNNavigationOptions *)options
                                       perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    RNNStackController *stack = self.stackController;
    if (![withDefault.topBar.background.component.waitForRender withDefault:NO] && readyBlock) {
        readyBlock();
        readyBlock = nil;
    }

    if (withDefault.topBar.background.component.name.hasValue) {
        NSString *currentChildComponentId = [stack getCurrentChild].layoutInfo.componentId;
        _topBarBackgroundReactView =
            [_componentRegistry createComponentIfNotExists:withDefault.topBar.background.component
                                         parentComponentId:currentChildComponentId
                                             componentType:RNNComponentTypeTopBarBackground
                                       reactViewReadyBlock:readyBlock];

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
    RNNStackController *stack = self.stackController;
    if (_customTopBarBackground) {
        [_customTopBarBackground removeFromSuperview];
    }
    _customTopBarBackground = [[RNNCustomTitleView alloc] initWithFrame:stack.navigationBar.bounds
                                                                subView:_topBarBackgroundReactView
                                                              alignment:@"fill"];

    [stack.navigationBar insertSubview:_customTopBarBackground atIndex:1];
    [_topBarBackgroundReactView componentDidAppear];
}

- (void)dealloc {
    [_componentRegistry removeComponent:self.boundComponentId];
}

@end
