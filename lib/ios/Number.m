#import "Number.h"

@interface Number()

@property (nonatomic, retain) NSNumber* value;

@end

@implementation Number

- (NSNumber *)get {
	return [super get];
}

- (NSNumber *)getWithDefaultValue:(NSNumber *)defaultValue {
	return [super getWithDefaultValue:defaultValue];
}

@end
