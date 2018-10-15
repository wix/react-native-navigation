#import "RNNSideMenuSideOptions.h"

@implementation RNNSideMenuSideOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.visible = [BoolParser parse:dict key:@"visible"];
	self.enabled = [BoolParser parse:dict key:@"enabled"];
	
	self.width = [DoubleParser parse:dict key:@"width"];
	
	self.animationType = [StringParser parse:dict key:@"animationType"];
	
	return self;
}

- (void)resetOptions {
	self.visible = nil;
	self.enabled = nil;
}

@end
