#import "TopBarPresenter.h"
#import <UIKit/UIKit.h>

API_AVAILABLE(ios(13.0))
@interface TopBarAppearancePresenter : TopBarPresenter

@property(nonatomic, strong) UIColor *borderColor;

@property(nonatomic) BOOL showBorder;

@end
