#import <Foundation/Foundation.h>
#import "DisplayLinkAnimation.h"

@protocol DisplayLinkAnimatorDelegate <NSObject>

@required

- (void)updateAnimations:(NSTimeInterval)elapsed;

- (NSTimeInterval)maxDuration;

- (void)end;

@end
