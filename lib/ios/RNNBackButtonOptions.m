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
	
	return self;
}

- (BOOL)hasValue {
	return self.icon.hasValue ||
			self.showTitle.hasValue ||
			self.color.hasValue ||
			self.title.hasValue;
}


@end
