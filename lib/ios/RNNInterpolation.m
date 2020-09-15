#import "RNNInterpolation.h"
#import "RNNUtils.h"

@implementation RNNInterpolation

-(instancetype)initWithDict:(NSDictionary *)interpolation {
	self = [super init];
	
	self.overshootTension = [interpolation[@"tension"] floatValue];
	self.interpolation = [self animationOptionsFromString:interpolation[@"interpolation"]];
	
	return self;
}

- (void)mergeWith:(NSDictionary *)interpolation {
	self.overshootTension = [interpolation[@"tension"] floatValue];
	self.interpolation = [self animationOptionsFromString:interpolation[@"interpolation"]];
}

- (RNNInterpolationOptions)animationOptionsFromString:(NSString*)interpolationString {
	return [RCTConvert RNNInterpolationOptions:interpolationString];
}

@end
