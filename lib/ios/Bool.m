#import "Bool.h"

@interface Bool()

@property (nonatomic, retain) NSNumber* value;

@end

@implementation Bool

- (BOOL)boolValue {
	if (self.hasValue) {
		return [self.value boolValue];
	} else
		return false;
}

@end
