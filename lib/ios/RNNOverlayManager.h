#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"

@interface RNNOverlayManager : NSObject

- (void)showOverlay:(UIViewController*)viewController completion:(RNNTransitionCompletionBlock)completion;
- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion;

@end
