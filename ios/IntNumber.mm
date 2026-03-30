#import "IntNumber.h"

@interface IntNumber ()

@property(nonatomic, retain) NSNumber *value;

@end

@implementation IntNumber

- (NSUInteger)get {
    return [[super get] unsignedIntegerValue];
}

- (NSUInteger)withDefault:(NSUInteger)defaultValue {
    if (self.value) {
        return [self.value unsignedIntegerValue];
    } else if (defaultValue) {
        return defaultValue;
    }

    return 0;
}

@end
