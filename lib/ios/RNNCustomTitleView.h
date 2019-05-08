#import <UIKit/UIKit.h>
#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>

@interface RNNCustomTitleView : UIView <RCTRootViewDelegate>

- (instancetype)initWithFrame:(CGRect)frame subView:(RCTRootView*)subView alignment:(NSString*)alignment;

@end

