#import "RNNSplitViewOptions.h"
#import "RNNParentProtocol.h"

@implementation RNNSplitViewOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.displayMode = dict[@"displayMode"];
	self.primaryEdge = dict[@"primaryEdge"];
	NSNumberFormatter *f = [[NSNumberFormatter alloc] init];
	f.numberStyle = NSNumberFormatterDecimalStyle;
	self.minWidth = [f numberFromString:dict[@"minWidth"]];
	self.maxWidth = [f numberFromString:dict[@"maxWidth"]];
	
	return self;	
}

@end
