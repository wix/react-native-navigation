#import "NoColor.h"

@implementation NoColor

- (BOOL)hasValue {
    return YES;
}

- (UIColor *)get {
    return nil;
}

- (UIColor *)getWithDefaultValue:(id)defaultValue {
    return nil;
}

@end