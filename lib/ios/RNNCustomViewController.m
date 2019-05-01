#import "RNNCustomViewController.h"

@implementation RNNCustomViewController {
	NSString* _text;
}

- (instancetype)initWithProps:(NSDictionary *)props {
	self = [super init];
	_text = props[@"text"];
	
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self addTestLabel];
    [[self view] setBackgroundColor:UIColor.whiteColor];
}

- (void)addTestLabel {
	UILabel* label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 200)];
	label.textAlignment = NSTextAlignmentCenter;
	label.numberOfLines = 2;
	label.center = self.view.center;
	label.text = _text;
	label.accessibilityIdentifier = @"TestLabel";
	[self.view addSubview:label];
}

@end
