#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNOverlayWindow.h"

@interface RNNOverlayManager : NSObject

- (void)showOverlayWindow:(UIWindow*)viewController;
- (void)showOverlayWindowAsKeyWindow:(UIWindow *)overlayWindow;
- (void)dismissOverlay:(UIViewController*)viewController;
- (void)dismissAllOverlays;

@property (nonatomic, retain) NSMutableArray* overlayWindows;

@end
