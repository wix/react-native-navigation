#import "RNNNavigationEvent.h"

@interface RNNNavigationEvent ()
	@property (nonatomic, strong) NavigationCommand command;
	@property (nonatomic, strong) NSString* toComponentId;
	@property (nonatomic, strong) NSString* fromComponentId;
@end

@implementation RNNNavigationEvent

+ (instancetype)create:(NavigationCommand)commandType fromComponent:(NSString *)fromComponentId toComponent:(NSString *)toComponentId {
	RNNNavigationEvent* navigationCommand = [[RNNNavigationEvent alloc] init];
	navigationCommand.command = commandType;
	navigationCommand.fromComponentId = fromComponentId;
	navigationCommand.toComponentId = toComponentId;
	return navigationCommand;
}

- (NSDictionary *)body {
	NSMutableDictionary* mutableParams = [NSMutableDictionary new];
	self.fromComponentId ? [mutableParams setObject:self.fromComponentId forKey:@"fromComponentId"] : nil;
	self.toComponentId ? [mutableParams setObject:self.toComponentId forKey:@"toComponentId"] : nil;
	return @{@"commandName": self.command, @"params": mutableParams};
}

@end
