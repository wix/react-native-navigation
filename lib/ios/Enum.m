#import "Enum.h"

@implementation Enum

- (int)get {
    return [self convertString:super.get];
}

- (int)getWithDefaultValue:(int)defaultValue {
    NSString* value = [super getWithDefaultValue:nil];
	if (value) {
        return [self convertString:value];
    } else {
        return defaultValue;
    }
}

- (int)convertString:(NSString *)string {
    return 0;
}

@end
