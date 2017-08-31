#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, RCCPanDirection) {
    RCCPanDirectionRight,
    RCCPanDirectionDown,
    RCCPanDirectionLeft,
    RCCPanDirectionUp
};

@interface RCCDirectionalPanGestureRecognizer : UIPanGestureRecognizer

@property (nonatomic) RCCPanDirection direction;

@end
