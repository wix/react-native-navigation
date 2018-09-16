#import "RNNOptionsManager.h"

@implementation RNNOptionsManager

- (RNNNavigationOptions *)createOptions:(NSDictionary *)optionsDict {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:optionsDict];
	
	[options mergeOptions:[[RNNNavigationOptions alloc] initWithDict:_defaultOptionsDict] overrideOptions:NO];
	
	return options;
}
@end
