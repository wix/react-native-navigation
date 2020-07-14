#import "Double.h"

@interface Double()

@property (nonatomic, retain) NSNumber* value;

@end

@implementation Double

+ (instancetype)withValue:(double)value {
    return [[self.class alloc] initWithValue:@(value)];
}

- (double)get {
	return [[super get] doubleValue];
}

- (double)getWithDefaultValue:(double)defaultValue {
	if (self.value) {
		return [self.value doubleValue];
	} else if (defaultValue) {
		return defaultValue;
	}
	
	return 0;
}

@end
