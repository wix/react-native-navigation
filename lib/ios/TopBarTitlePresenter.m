#import "TopBarTitlePresenter.h"
#import "RNNReactTitleView.h"
#import "RNNTitleViewHelper.h"
#import "UIViewController+RNNOptions.h"

@implementation TopBarTitlePresenter {
    RNNReactTitleView *_customTitleView;
    RNNTitleViewHelper *_titleViewHelper;
}

- (void)applyOptionsOnInit:(RNNTopBarOptions *)initialOptions {
    if (initialOptions.title.component.hasValue) {
        [self setCustomNavigationTitleView:initialOptions perform:nil];
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
        [self setCustomNavigationTitleView:resolvedOptions perform:nil];
    } else if (options.subtitle.text.hasValue) {
        [self setTitleViewWithSubtitle:resolvedOptions];
    } else if (options.title.text.hasValue) {
        [self removeTitleComponents];
        self.boundViewController.navigationItem.title = resolvedOptions.title.text.get;
    }
}

- (void)setTitleViewWithSubtitle:(RNNTopBarOptions *)options {
    if (!_customTitleView && ![options.largeTitle.visible withDefault:NO]) {
        _titleViewHelper =
            [[RNNTitleViewHelper alloc] initWithTitleViewOptions:options.title
                                                 subTitleOptions:options.subtitle
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

- (void)renderComponents:(RNNTopBarOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    [self setCustomNavigationTitleView:options perform:readyBlock];
}

- (void)setCustomNavigationTitleView:(RNNTopBarOptions *)options
                             perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    UIViewController<RNNLayoutProtocol> *viewController = self.boundViewController;
    if (![options.title.component.waitForRender withDefault:NO] && readyBlock) {
        readyBlock();
        readyBlock = nil;
    }

    if (options.title.component.name.hasValue) {
        _customTitleView = (RNNReactTitleView *)[self.componentRegistry
            createComponentIfNotExists:options.title.component
                     parentComponentId:viewController.layoutInfo.componentId
                         componentType:RNNComponentTypeTopBarTitle
                   reactViewReadyBlock:readyBlock];
        _customTitleView.backgroundColor = UIColor.clearColor;
        NSString *alignment = [options.title.component.alignment withDefault:@""];
        [_customTitleView setAlignment:alignment
                               inFrame:viewController.navigationController.navigationBar.frame];
        [_customTitleView layoutIfNeeded];

        viewController.navigationItem.titleView = nil;
        viewController.navigationItem.titleView = _customTitleView;
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
