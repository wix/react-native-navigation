#import "RNNBackButtonOptions.h"

@implementation RNNBackButtonOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.icon = [ImageParser parse:dict key:@"icon"];
	self.title = [TextParser parse:dict key:@"title"];
	self.transition = [TextParser parse:dict key:@"transition"];
	self.color = [ColorParser parse:dict key:@"color"];
	self.showTitle = [BoolParser parse:dict key:@"showTitle"];
	self.visible = [BoolParser parse:dict key:@"visible"];

    self.textColor = [ColorParser parse:dict key:@"textColor"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];

	return self;
}

@end
