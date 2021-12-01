#import "RNNButtonOptions.h"
#import "RNNComponentViewCreator.h"
#import "RNNReactComponentRegistry.h"
#import "UIViewController+LayoutProtocol.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNButtonsPresenter : NSObject

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                             eventEmitter:(RNNEventEmitter *)eventEmitter;

- (void)applyLeftButtons:(NSArray<RNNButtonOptions *> *)leftButtons
            defaultColor:(Color *)defaultColor
    defaultDisabledColor:(Color *)defaultDisabledColor
                animated:(BOOL)animated;

- (void)applyRightButtons:(NSArray<RNNButtonOptions *> *)rightButtons
             defaultColor:(Color *)defaultColor
     defaultDisabledColor:(Color *)defaultDisabledColor
                 animated:(BOOL)animated;

- (void)applyLeftButtonsColor:(UIColor *)color;

- (void)applyRightButtonsColor:(UIColor *)color;

- (void)componentWillAppear;

- (void)componentDidAppear;

- (void)componentDidDisappear;

- (void)bindViewController:(UIViewController<RNNLayoutProtocol> *)viewController;

@end
