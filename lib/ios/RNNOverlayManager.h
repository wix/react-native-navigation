#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"
#import "RNNOverlayWindow.h"

@interface RNNOverlayManager : NSObject

- (void)showOverlayWindow:(UIWindow*)window withOptions:(NSDictionary *)options;
- (void)dismissOverlay:(UIViewController*)viewController;

@property (nonatomic, retain) NSMutableArray* overlayWindows;
@property (nonatomic, retain) NSMutableArray* keyOverlayWindows;

@end
