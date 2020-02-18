#import "TimeInterval.h"

@implementation TimeInterval

- (NSTimeInterval)getWithDefaultValue:(double)defaultValue {
    return [super getWithDefaultValue:defaultValue] / 1000;
}

- (NSTimeInterval)get {
    return [super get] / 1000;
}

@end
