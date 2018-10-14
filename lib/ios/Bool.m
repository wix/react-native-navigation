#import "Bool.h"

@interface Bool()

@property (nonatomic, retain) NSNumber* value;

@end

@implementation Bool

- (BOOL)get {
	return [self.value boolValue];
}

- (NSNumber *)getValue {
	return self.value;
}

- (BOOL)getWithDefaultValue:(id)value {
	if (self.value) {
		return [self.value boolValue];
	} else {
		return [value boolValue];
	}
}

@end
