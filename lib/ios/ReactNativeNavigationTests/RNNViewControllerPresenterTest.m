#import <XCTest/XCTest.h>
#import "RNNViewControllerPresenter.h"

@interface RNNViewControllerPresenterTest : XCTestCase

@property (nonatomic, strong) RNNViewControllerPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) UITabBarController* bindedTabController;
@end

@implementation RNNViewControllerPresenterTest

- (void)setUp {
    [super setUp];
	self.uut = [[RNNViewControllerPresenter alloc] init];
	self.bindedTabController = [[UITabBarController alloc] init];
	[self.uut bindViewController:self.bindedTabController];
	self.options = [[RNNNavigationOptions alloc] initWithDict:@{}];
}


@end
