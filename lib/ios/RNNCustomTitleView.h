#import <React/RCTRootViewDelegate.h>
#import <UIKit/UIKit.h>
#import "RNNReactView.h"

#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTSurfaceDelegate.h>
#import <React/RCTSurface.h>
#endif

@interface RNNCustomTitleView : UIView <
#ifdef RCT_NEW_ARCH_ENABLED
RCTSurfaceDelegate
#else
RCTRootViewDelegate
#endif
>

- (instancetype)initWithFrame:(CGRect)frame
					  subView:(RNNReactView *)subView
					alignment:(NSString *)alignment;

@end
