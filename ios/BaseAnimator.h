#import "DisplayLinkAnimatorDelegate.h"
#import <Foundation/Foundation.h>

@interface BaseAnimator : NSObject <DisplayLinkAnimatorDelegate>

@property(nonatomic, strong) UIView *view;

@property(nonatomic, strong) NSArray<id<DisplayLinkAnimation>> *animations;

@end
