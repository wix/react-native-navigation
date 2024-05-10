#import "DisplayLinkAnimation.h"
#import <Foundation/Foundation.h>

@protocol DisplayLinkAnimatorDelegate <NSObject>

@required

- (void)updateAnimations:(NSTimeInterval)elapsed;

- (NSTimeInterval)maxDuration;

@optional
- (void)end;

@end
