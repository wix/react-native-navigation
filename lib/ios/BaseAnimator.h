#import <Foundation/Foundation.h>
#import "DisplayLinkAnimatorDelegate.h"

@interface BaseAnimator : NSObject<DisplayLinkAnimatorDelegate>

@property (nonatomic, strong) UIView* view;

@property (nonatomic, strong) NSArray<id<DisplayLinkAnimation>> * animations;

@end
