#import "RNNIntNumber.h"

@interface RNNIntNumber ()

@property(nonatomic, retain) NSNumber *value;

@end

@implementation RNNIntNumber

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
