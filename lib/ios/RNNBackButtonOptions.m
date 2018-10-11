#import "RNNBackButtonOptions.h"

@implementation RNNBackButtonOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.icon = [DictionaryParser parse:dict key:@"icon"];
	self.title = [StringParser parse:dict key:@"title"];
	self.transition = [StringParser parse:dict key:@"transition"];
	self.color = [NumberParser parse:dict key:@"color"];
	self.showTitle = [BoolParser parse:dict key:@"showTitle"];
	self.visible = [BoolParser parse:dict key:@"visible"];
	
	return self;
}

@end
