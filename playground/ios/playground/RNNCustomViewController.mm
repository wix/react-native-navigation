#import "RNNCustomViewController.h"
#import "RNNNativeViewController.h"

@implementation RNNCustomViewController {
    NSString *_text;
}

- (instancetype)initWithProps:(NSDictionary *)props {
    self = [super init];
    _text = props[@"text"];

    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self addTestLabel];
    [self addDismissModalButton];
    [self addNavigationBarButtons];
    [self addPushButton];

    [[self view] setBackgroundColor:UIColor.whiteColor];
}

- (void)addDismissModalButton {
    UIButton *dismissModalButton =
        [[UIButton alloc] initWithFrame:CGRectMake(self.view.center.x - 70, 300, 140, 50)];
    dismissModalButton.backgroundColor = UIColor.systemBlueColor;
    dismissModalButton.accessibilityIdentifier = @"EXTERNAL_DISMISS_MODAL_BTN";
    [dismissModalButton setTitle:@"Dismiss modal" forState:UIControlStateNormal];
    [dismissModalButton addTarget:self
                           action:@selector(dismissModal)
                 forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:dismissModalButton];
}

- (void)addPushButton {
    UIButton *pushNativeVCButton =
        [[UIButton alloc] initWithFrame:CGRectMake(self.view.center.x - 70, 370, 140, 50)];
    pushNativeVCButton.backgroundColor = UIColor.systemBlueColor;
    pushNativeVCButton.accessibilityIdentifier = @"PUSH_NATIVE_BTN";
    [pushNativeVCButton setTitle:@"Push Native VC" forState:UIControlStateNormal];
    [pushNativeVCButton addTarget:self
                           action:@selector(pushNativeVC)
                 forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:pushNativeVCButton];
}

- (void)addTestLabel {
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 200)];
    label.textAlignment = NSTextAlignmentCenter;
    label.numberOfLines = 2;
    label.center = self.view.center;
    label.text = _text;
    label.accessibilityIdentifier = @"TestLabel";
    [self.view addSubview:label];
}

- (void)addNavigationBarButtons {
    UIBarButtonItem *rightButton =
        [[UIBarButtonItem alloc] initWithTitle:@"Right button"
                                         style:UIBarButtonItemStylePlain
                                        target:self
                                        action:@selector(rightButtonPressed)];
    rightButton.accessibilityIdentifier = @"EXTERNAL_TOP_BAR_RIGHT_BTN";
    self.navigationItem.rightBarButtonItem = rightButton;
}

- (void)dismissModal {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)pushNativeVC {
    RNNNativeViewController *nativeVC = [[RNNNativeViewController alloc] init];
    [self.navigationController pushViewController:nativeVC animated:NO];
}

- (void)rightButtonPressed {
}

@end
