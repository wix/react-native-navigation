#import "RNNButtonOptions.h"
#import "RNNComponentViewCreator.h"
#import "RNNReactComponentRegistry.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNButtonsPresenter : NSObject

- (instancetype)initWithViewController:(UIViewController *)viewController
                     componentRegistry:(RNNReactComponentRegistry *)componentRegistry;

- (void)applyLeftButtons:(NSArray<RNNButtonOptions *> *)leftButtons
      defaultButtonStyle:(RNNButtonOptions *)defaultButtonStyle;

- (void)applyRightButtons:(NSArray<RNNButtonOptions *> *)rightButtons
       defaultButtonStyle:(RNNButtonOptions *)defaultButtonStyle;

- (void)componentDidAppear;

- (void)componentDidDisappear;

@end
