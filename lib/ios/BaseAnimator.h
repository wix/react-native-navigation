#import <Foundation/Foundation.h>
#import "DisplayLinkAnimatorDelegate.h"

@interface BaseAnimator : NSObject<DisplayLinkAnimatorDelegate>

@property (nonatomic, strong) NSArray<id<DisplayLinkAnimation>>* animations;
@property (nonatomic, strong) UIView* view;

@end
