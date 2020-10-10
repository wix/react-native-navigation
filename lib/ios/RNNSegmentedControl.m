#import "RNNSegmentedControl.h"

@implementation RNNSegmentedControl

- (void)setTitle:(NSString*)title atIndex:(NSUInteger)index {
	NSMutableArray* mutableTitles = [[NSMutableArray alloc] initWithArray:self.sectionTitles];
	[mutableTitles setObject:title atIndexedSubscript:index];
	[self setSectionTitles:mutableTitles];
}

- (void)setBadge:(NSString*)badge atIndex:(NSUInteger)index {
    NSMutableArray* mutableBadges = [[NSMutableArray alloc] initWithArray:self.sectionBadges];
    [mutableBadges setObject:badge atIndexedSubscript:index];
    [self setSectionBadges:mutableBadges];
}

@end
