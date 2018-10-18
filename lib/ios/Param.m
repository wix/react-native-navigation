#import "Param.h"

@interface Param()

@property (nonatomic, retain) id value;
@property (nonatomic) BOOL consumed;

@end

@implementation Param

- (instancetype)initWithValue:(id)value {
	self = [super init];
	
	self.value = value;
	
	return self;
}

- (id)get {
	if (!self.value) {
		@throw [NSException exceptionWithName:@"Param get" reason:@"value does not exists" userInfo:nil];
	}
	
	return self.value;
}

- (id)getWithDefaultValue:(id)defaultValue {
	if (self.value) {
		return self.value;
	} else if (defaultValue) {
		return defaultValue;
	}
	
	return nil;
}

- (void)consume {
	self.consumed = true;
}

- (BOOL)hasValue {
	return self.value && !self.consumed;
}

@end
