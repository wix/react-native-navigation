#import "TimeInterval.h"

@implementation TimeInterval

- (NSTimeInterval)withDefault:(double)defaultValue {
    return [super withDefault:defaultValue] / 1000;
}

- (NSTimeInterval)get {
    return [super get] / 1000;
}

@end
