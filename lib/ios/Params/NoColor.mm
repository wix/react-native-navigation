#import "NoColor.h"

@implementation NoColor

- (BOOL)hasValue {
    return YES;
}

- (UIColor *)get {
    return nil;
}

- (UIColor *)withDefault:(id)defaultValue {
    return nil;
}

@end
