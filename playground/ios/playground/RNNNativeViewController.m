#import "RNNNativeViewController.h"

@interface RNNNativeViewController ()

@end

@implementation RNNNativeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UIColor.whiteColor;
    [self addPopButton];
}

- (void)addPopButton {
    UIButton *popNativeVCButton =
        [[UIButton alloc] initWithFrame:CGRectMake(self.view.center.x - 70, 370, 140, 50)];
    popNativeVCButton.backgroundColor = UIColor.systemBlueColor;
    popNativeVCButton.accessibilityIdentifier = @"POP_NATIVE_BTN";
    [popNativeVCButton setTitle:@"Pop" forState:UIControlStateNormal];
    [popNativeVCButton addTarget:self
                          action:@selector(pop)
                forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:popNativeVCButton];
}

- (void)pop {
    [self.navigationController popViewControllerAnimated:NO];
}

@end
