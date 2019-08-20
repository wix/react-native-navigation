#import "NoColor.h"
#import "Color.h"


@implementation NoColor

- (BOOL)hasValue {
    return YES;
}

- (UIColor *)get {
//    return nil;
    return UIColor.cyanColor;
}

- (UIColor *)getWithDefaultValue:(id)defaultValue {
    return UIColor.cyanColor;
//    return nil;
}


@end