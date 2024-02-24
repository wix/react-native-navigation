#import "RNNDisplayLinkAnimation.h"
#import <Foundation/Foundation.h>

@protocol RNNDisplayLinkAnimatorDelegateProtocol <NSObject>

@required

- (void)updateAnimations:(NSTimeInterval)elapsed;

- (NSTimeInterval)maxDuration;

@optional
- (void)end;

@end
