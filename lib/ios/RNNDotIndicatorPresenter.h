#import <Foundation/Foundation.h>

@class UIViewController;
@class DotIndicatorOptions;


@interface RNNDotIndicatorPresenter : NSObject
- (void)apply:(UIViewController *)child :(DotIndicatorOptions *)options;
@end