
#import "RNNOptions.h"
#import <objc/runtime.h>

@implementation RNNOptions

-(instancetype)	initWithDict:(NSDictionary *)dict {
	self = [super init];
	[self initializeOptionsPropertiesWithDict:dict];
	
	[self mergeWith:dict];
	return self;
}

- (void)applyOn:(UIViewController *)viewController defaultOptions:(RNNOptions *)defaultOptions {
	[defaultOptions applyOn:viewController];
	[self applyOn:viewController];
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([self hasProperty:key]) {
			if ([[self valueForKey:key] isKindOfClass:[RNNOptions class]]) {
				RNNOptions* options = [self valueForKey:key];
				[options mergeWith:[otherOptions objectForKey:key]];
			} else {
				[self setValue:[otherOptions objectForKey:key] forKey:key];
			}
		}
	}
}

-(void)mergeIfEmptyWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([self hasProperty:key]) {
			if ([[self valueForKey:key] isKindOfClass:[RNNOptions class]]) {
				RNNOptions* options = [self valueForKey:key];
				[options mergeIfEmptyWith:[otherOptions objectForKey:key]];
			} else if (![self valueForKey:key]) {
				[self setValue:[otherOptions objectForKey:key] forKey:key];
			}
		}
	}
}

- (BOOL)hasProperty:(NSString*)propName {
	return [self respondsToSelector:NSSelectorFromString(propName)];
}

- (void)initializeOptionsPropertiesWithDict:(NSDictionary*)dict {
	unsigned int count;
	objc_property_t* props = class_copyPropertyList([self class], &count);
	for (int i = 0; i < count; i++) {
		objc_property_t property = props[i];
		NSString *propertyName = [NSString stringWithCString:property_getName(property) encoding:NSUTF8StringEncoding];
		const char * type = property_getAttributes(property);
		NSString * typeString = [NSString stringWithUTF8String:type];
		NSArray * attributes = [typeString componentsSeparatedByString:@","];
		NSString * typeAttribute = [attributes objectAtIndex:0];
		
		if ([typeAttribute hasPrefix:@"T@"] && [typeAttribute length] > 3) {
			NSString * typeClassName = [typeAttribute substringWithRange:NSMakeRange(3, [typeAttribute length]-4)];
			Class typeClass = NSClassFromString(typeClassName);
			if ([typeClass isSubclassOfClass:[RNNOptions class]]) {
				RNNOptions* value = [[typeClass alloc] initWithDict:dict[propertyName]];
				[self setValue:value forKey:propertyName];
			}
		}
		
	}
	free(props);
}

@end
