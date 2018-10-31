#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"
#import "RNNOverlayWindow.h"

@interface RNNOverlayManager : NSObject

- (void)showOverlayWindow:(UIWindow*)viewController;
- (void)dismissOverlay:(UIViewController*)viewController;

@property (nonatomic, retain) NSMutableArray* overlayWindows;

@end
