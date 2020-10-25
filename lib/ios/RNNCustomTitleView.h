#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>
#import <UIKit/UIKit.h>

@interface RNNCustomTitleView : UIView <RCTRootViewDelegate>

- (instancetype)initWithFrame:(CGRect)frame
                      subView:(RCTRootView *)subView
                    alignment:(NSString *)alignment;

@end
