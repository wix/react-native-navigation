#import "Number.h"

@interface Number ()

@property(nonatomic, retain) NSNumber *value;

@end

@implementation Number

- (NSNumber *)get {
    return [super get];
}

- (NSNumber *)withDefault:(NSNumber *)defaultValue {
    return [super withDefault:defaultValue];
}

@end
