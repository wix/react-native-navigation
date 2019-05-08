#import "RNNUtils.h"

@implementation RNNUtils
+(double)getDoubleOrKey:(NSDictionary*)dict withKey:(NSString*)key withDefault:(double)defaultResult {
	if ([dict objectForKey:key]) {
		return [dict[key] doubleValue];
	} else {
		return defaultResult;
	}
}

+(BOOL)getBoolOrKey:(NSDictionary*)dict withKey:(NSString*)key withDefault:(BOOL)defaultResult {
	if ([dict objectForKey:key]) {
		return [dict[key] boolValue];
	} else {
		return defaultResult;
	}
}

+(id)getObjectOrKey:(NSDictionary*)dict withKey:(NSString*)key withDefault:(id)defaultResult {
	if ([dict objectForKey:key]) {
		return dict[key];
	} else {
		return defaultResult;
	}
}

+ (NSNumber *)getCurrentTimestamp {
	return [NSNumber numberWithLong:[[NSDate date] timeIntervalSince1970] * 1000];
}

@end
