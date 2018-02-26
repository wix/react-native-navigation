#import "RNNCustomViewController.h"

@implementation RNNCustomViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	[self addTestLabel];
}

- (void)addTestLabel {
	UILabel* label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 100)];
	label.center = self.view.center;
	label.text = @"Test label";
	label.accessibilityIdentifier = @"TestLabel";
	[self.view addSubview:label];
}

@end
