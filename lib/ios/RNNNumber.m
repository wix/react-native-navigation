#import "RNNNumber.h"

@interface RNNNumber ()

@property(nonatomic, retain) NSNumber *value;

@end

@implementation RNNNumber

- (NSNumber *)get {
    return [super get];
}

- (NSNumber *)withDefault:(NSNumber *)defaultValue {
    return [super withDefault:defaultValue];
}

@end
