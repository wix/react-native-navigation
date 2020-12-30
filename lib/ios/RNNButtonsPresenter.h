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
      defaultButtonStyle:(RNNButtonOptions *)defaultButtonStyle;

- (void)applyRightButtons:(NSArray<RNNButtonOptions *> *)rightButtons
       defaultButtonStyle:(RNNButtonOptions *)defaultButtonStyle;

- (void)componentDidAppear;

- (void)componentDidDisappear;

- (void)bindViewController:(UIViewController<RNNLayoutProtocol> *)viewController;

@end
