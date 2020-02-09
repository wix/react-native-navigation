#import "TopBarTitlePresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNTitleViewHelper.h"
#import "RNNReactTitleView.h"

@implementation TopBarTitlePresenter {
    RNNReactTitleView* _customTitleView;
    RNNTitleViewHelper* _titleViewHelper;
}

- (void)applyOptions:(RNNTopBarOptions *)options {
	[self updateTitleWithOptions:options];
}

- (void)updateTitleWithOptions:(RNNTopBarOptions *)options {
    if (options.title.component.hasValue) {
        [self setCustomNavigationTitleView:options perform:nil];
    }else if (options.subtitle.text.hasValue) {
        [self setTitleViewWithSubtitle:options];
    }else if (options.title.text.hasValue) {
        [self removeTitleComponents];
        self.boundViewController.navigationItem.title = options.title.text.get;
    }
}

- (void)mergeOptions:(RNNTopBarOptions *)options resolvedOptions:(RNNTopBarOptions *)resolvedOptions {
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
	if (!_customTitleView && ![options.largeTitle.visible getWithDefaultValue:NO]) {
		_titleViewHelper = [[RNNTitleViewHelper alloc] initWithTitleViewOptions:options.title subTitleOptions:options.subtitle viewController:self.boundViewController];

		if (options.title.text.hasValue) {
			[_titleViewHelper setTitleOptions:options.title];
		}
		if (options.subtitle.text.hasValue) {
			[_titleViewHelper setSubtitleOptions:options.subtitle];
		}

		[_titleViewHelper setup];
	}
}

- (void)renderComponents:(RNNTopBarOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    [self setCustomNavigationTitleView:options perform:readyBlock];
}

- (void)setCustomNavigationTitleView:(RNNTopBarOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    UIViewController<RNNLayoutProtocol>* viewController = self.boundViewController;
    if (![options.title.component.waitForRender getWithDefaultValue:NO] && readyBlock) {
        readyBlock();
        readyBlock = nil;
    }
    
    if (options.title.component.name.hasValue) {
        _customTitleView = (RNNReactTitleView *)[self.componentRegistry createComponentIfNotExists:options.title.component parentComponentId:viewController.layoutInfo.componentId componentType:RNNComponentTypeTopBarTitle reactViewReadyBlock:readyBlock];
        _customTitleView.backgroundColor = UIColor.clearColor;
        NSString* alignment = [options.title.component.alignment getWithDefaultValue:@""];
        [_customTitleView setAlignment:alignment inFrame:viewController.navigationController.navigationBar.frame];
        [_customTitleView layoutIfNeeded];
        
        viewController.navigationItem.titleView = nil;
        viewController.navigationItem.titleView = _customTitleView;
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

- (void)componentDidAppear {
    [_customTitleView componentDidAppear];
}

- (void)componentDidDisappear {
    [_customTitleView componentDidDisappear];
}

@end
