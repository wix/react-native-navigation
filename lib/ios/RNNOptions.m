
#import "RNNOptions.h"
#import <objc/runtime.h>

@implementation RNNOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	return self;
}

- (RNNOptions *)mergeOptions:(RNNOptions *)otherOptions overrideOptions:(BOOL)override {
	for (id prop in [self objectProperties:otherOptions]) {
		id value = [otherOptions valueForKey:prop];
		if ([value isKindOfClass:[RNNOptions class]]) {
			[[self valueForKey:prop] mergeOptions:value overrideOptions:override];
		} else if ([value isKindOfClass:[Param class]]) {
			if ((((Param *)value).hasValue) && (override || !((Param *)[self valueForKey:prop]).hasValue)) {
				[self setValue:value forKey:prop];
			}
		} else if (value && (override || ![self valueForKey:prop])) {
			[self setValue:value forKey:prop];
		}
	}
	
	return self;
}

- (RNNOptions *)overrideOptions:(RNNOptions *)otherOptions {
	return [self mergeOptions:otherOptions overrideOptions:YES];
}

- (RNNOptions *)mergeOptions:(RNNOptions *)otherOptions {
	return [self mergeOptions:otherOptions overrideOptions:NO];
}

- (RNNOptions *)mergeInOptions:(RNNOptions *)otherOptions {
	if (!otherOptions) {
		return self;
	}
	
	return [otherOptions mergeOptions:self overrideOptions:NO];
}

- (RNNOptions *)withDefault:(RNNOptions *)defaultOptions {
	RNNOptions* newOptions = [[[self class] alloc] initWithDict:@{}];
	[newOptions mergeOptions:defaultOptions overrideOptions:YES];
	[newOptions mergeOptions:self overrideOptions:YES];

	return newOptions;
}

- (NSArray *)objectProperties:(NSObject *)object {
	NSMutableArray* properties = [NSMutableArray new];
	unsigned int count;
	objc_property_t* props = class_copyPropertyList([object class], &count);
	for (int i = 0; i < count; i++) {
		objc_property_t property = props[i];
		NSString *propertyName = [NSString stringWithCString:property_getName(property) encoding:NSUTF8StringEncoding];
		[properties addObject:propertyName];
	}
	
	free(props);
	return properties;
}

@end
