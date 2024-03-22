#import "RNNTimeInterval.h"

@implementation RNNTimeInterval

- (NSTimeInterval)withDefault:(double)defaultValue {
    return [super withDefault:defaultValue] / 1000;
}

- (NSTimeInterval)get {
    return [super get] / 1000;
}

@end
