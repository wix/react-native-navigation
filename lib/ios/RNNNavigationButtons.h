#import "RNNButtonOptions.h"
#import "RNNComponentViewCreator.h"
#import "RNNReactComponentRegistry.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNNavigationButtons : NSObject

- (instancetype)initWithViewController:(UIViewController *)viewController
                     componentRegistry:(RNNReactComponentRegistry *)componentRegistry;

- (void)applyLeftButtons:(NSArray *)leftButtons
               rightButtons:(NSArray *)rightButtons
     defaultLeftButtonStyle:(RNNButtonOptions *)defaultLeftButtonStyle
    defaultRightButtonStyle:(RNNButtonOptions *)defaultRightButtonStyle;

- (void)componentDidAppear;

- (void)componentDidDisappear;

@end
