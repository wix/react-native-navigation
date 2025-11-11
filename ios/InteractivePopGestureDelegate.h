
#import <UIKit/UIKit.h>

@interface InteractivePopGestureDelegate : NSObject <UIGestureRecognizerDelegate>

@property(nonatomic, weak) UINavigationController *navigationController;
@property(nonatomic, weak) id<UIGestureRecognizerDelegate> originalDelegate;

@property(nonatomic) BOOL enabled;

@end
