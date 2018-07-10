#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"
#import "RNNOverlayWindow.h"

@interface RNNOverlayManager : NSObject

- (instancetype)initWithStore:(RNNStore*)store;

- (void)showOverlay:(UIViewController*)viewController completion:(RNNTransitionCompletionBlock)completion;
- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject;

@property (nonatomic, retain) RNNOverlayWindow *overlayWindow;

@end
