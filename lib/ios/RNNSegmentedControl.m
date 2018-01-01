#import "RNNSegmentedControl.h"

@implementation RNNSegmentedControl

- (void)setTitle:(NSString*)title atIndex:(NSUInteger)index {
	NSMutableArray* mutableTitles = [[NSMutableArray alloc] initWithArray:self.sectionTitles];
	[mutableTitles setObject:title atIndexedSubscript:index];
	[self setSectionTitles:mutableTitles];
}

@end
