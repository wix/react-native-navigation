#import "TopBarTitlePresenter.h"
#import "RNNReactTitleView.h"
#import "RNNTitleViewHelper.h"
#import "UIViewController+RNNOptions.h"
#import "RNNUtils.h"

@implementation TopBarTitlePresenter {
    RNNReactTitleView *_customTitleView;
    RNNTitleViewHelper *_titleViewHelper;
    BOOL _deferComponentTitleUntilAfterButtons;
}

- (void)applyOptionsOnInit:(RNNTopBarOptions *)initialOptions {
    if (initialOptions.title.component.hasValue) {
        if (@available(iOS 26.0, *)) {
            _deferComponentTitleUntilAfterButtons = YES;
        } else {
            [self setCustomNavigationTitleView:initialOptions navigationBar:nil perform:nil];
        }
    } else if (initialOptions.title.text.hasValue) {
        [self removeTitleComponents];
        self.boundViewController.navigationItem.title = initialOptions.title.text.get;
    }
}

- (void)applyOptions:(RNNTopBarOptions *)options {
    if (options.subtitle.text.hasValue) {
        [self setTitleViewWithSubtitle:options];
    }
}

- (void)mergeOptions:(RNNTopBarOptions *)options
     resolvedOptions:(RNNTopBarOptions *)resolvedOptions {
    if (options.title.component.hasValue) {
        [self setCustomNavigationTitleView:resolvedOptions navigationBar:nil perform:nil];
    } else if (options.subtitle.text.hasValue) {
        [self setTitleViewWithSubtitle:resolvedOptions];
    } else if (options.title.text.hasValue) {
        [self removeTitleComponents];
        self.boundViewController.navigationItem.title = resolvedOptions.title.text.get;
    }

    if (options.title.color.hasValue) {
        [_titleViewHelper setTitleColor:options.title.color.get];
    }
    if (options.subtitle.color.hasValue) {
        [_titleViewHelper setSubtitleColor:options.subtitle.color.get];
    }
}

- (void)setTitleViewWithSubtitle:(RNNTopBarOptions *)options {
    if (!_customTitleView && ![options.largeTitle.visible withDefault:NO]) {
        _titleViewHelper =
            [[RNNTitleViewHelper alloc] initWithTitleViewOptions:options.title
                                                 subTitleOptions:options.subtitle
                                                    parentTestID:options.testID
                                                  viewController:self.boundViewController];

        if (options.title.text.hasValue) {
            [_titleViewHelper setTitleOptions:options.title];
        }
        if (options.subtitle.text.hasValue) {
            [_titleViewHelper setSubtitleOptions:options.subtitle];
        }

        [_titleViewHelper setup];
    }
}

- (void)preCreateDeferredComponentTitleIfNeeded:(RNNTopBarOptions *)options {
    if (!_deferComponentTitleUntilAfterButtons || !options.title.component.hasValue) {
        return;
    }
    [self.componentRegistry createComponentIfNotExists:options.title.component
                                     parentComponentId:self.boundViewController.layoutInfo.componentId
                                         componentType:RNNComponentTypeTopBarTitle
                                   reactViewReadyBlock:nil];
}

- (void)attachDeferredComponentTitleIfNeeded:(RNNTopBarOptions *)options
                                navigationBar:(UINavigationBar *)navigationBar {
    if (!_deferComponentTitleUntilAfterButtons) {
        return;
    }
    [self setCustomNavigationTitleView:options navigationBar:navigationBar perform:nil];
}

- (void)renderComponents:(RNNTopBarOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    if (_deferComponentTitleUntilAfterButtons) {
        if (readyBlock) {
            readyBlock();
        }
        return;
    }
    [self setCustomNavigationTitleView:options navigationBar:nil perform:readyBlock];
}

- (NSString *)resolvedTitleAlignmentForOptions:(RNNTopBarOptions *)options {
    NSString *alignment = [options.title.component.alignment withDefault:@""];
    if (@available(iOS 26.0, *)) {
        if ([alignment isEqualToString:@"center"] || alignment.length == 0) {
            return @"fill";
        }
    }
    return alignment;
}

- (void)setCustomNavigationTitleView:(RNNTopBarOptions *)options
                      navigationBar:(UINavigationBar *)navigationBar
                             perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    UIViewController<RNNLayoutProtocol> *viewController = self.boundViewController;
    if (![options.title.component.waitForRender withDefault: [RNNUtils getDefaultWaitForRender]] && readyBlock) {
        readyBlock();
        readyBlock = nil;
    }

    if (options.title.component.name.hasValue) {
        NSString *alignment = [self resolvedTitleAlignmentForOptions:options];
        if (!navigationBar) {
            navigationBar = viewController.navigationController.navigationBar;
        }
        if (_customTitleView && viewController.navigationItem.titleView == _customTitleView) {
            if (navigationBar && !CGRectIsEmpty(navigationBar.bounds)) {
                [_customTitleView setAlignment:alignment inFrame:navigationBar.bounds];
            }
            if (readyBlock) {
                readyBlock();
            }
            return;
        }

        _customTitleView = (RNNReactTitleView *)[self.componentRegistry
            createComponentIfNotExists:options.title.component
                     parentComponentId:viewController.layoutInfo.componentId
                         componentType:RNNComponentTypeTopBarTitle
                   reactViewReadyBlock:readyBlock];
        _customTitleView.backgroundColor = UIColor.clearColor;
        CGRect barBounds = navigationBar.bounds;
        [_customTitleView setAlignment:alignment inFrame:barBounds];
        [_customTitleView layoutIfNeeded];

        __weak RNNReactTitleView *weakTitleView = _customTitleView;
        __weak UIViewController<RNNLayoutProtocol> *weakViewController = viewController;
        __weak UINavigationBar *weakNavigationBar = navigationBar;
        __weak TopBarTitlePresenter *weakSelf = self;
        void (^attachTitleView)(void) = ^{
            if (!weakTitleView || !weakViewController) {
                return;
            }
            UINavigationBar *bar = weakNavigationBar ?: weakViewController.navigationController.navigationBar;
            if (bar && !CGRectIsEmpty(bar.bounds)) {
                [weakTitleView setAlignment:alignment inFrame:bar.bounds];
            }
            if (weakViewController.navigationItem.titleView != weakTitleView) {
                weakViewController.navigationItem.titleView = weakTitleView;
            }
            [weakTitleView setNeedsLayout];
            [weakTitleView layoutIfNeeded];
            TopBarTitlePresenter *strongSelf = weakSelf;
            if (strongSelf && weakViewController.navigationItem.titleView == weakTitleView) {
                strongSelf->_deferComponentTitleUntilAfterButtons = NO;
            }
        };

        if (navigationBar && !CGRectIsEmpty(barBounds)) {
            attachTitleView();
        } else {
            dispatch_async(dispatch_get_main_queue(), attachTitleView);
        }
        [_customTitleView componentWillAppear];
        [_customTitleView componentDidAppear];
    } else {
        [_customTitleView removeFromSuperview];
        if (readyBlock) {
            readyBlock();
        }
    }
}

- (void)removeTitleComponents {
    [_customTitleView componentDidDisappear];
    [_customTitleView removeFromSuperview];
    _customTitleView = nil;
    self.boundViewController.navigationItem.titleView = nil;
}

- (void)componentWillAppear {
    [_customTitleView componentWillAppear];
}

- (void)componentDidAppear {
    [_customTitleView componentDidAppear];
}

- (void)componentDidDisappear {
    [_customTitleView componentDidDisappear];
}

@end
