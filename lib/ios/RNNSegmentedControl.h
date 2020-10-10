#import <Foundation/Foundation.h>
#import "HMSegmentedControl.h"

@interface RNNSegmentedControl : HMSegmentedControl

- (void)setTitle:(NSString*)title atIndex:(NSUInteger)index;
- (void)setBadge:(NSString*)badge atIndex:(NSUInteger)index;

@end
