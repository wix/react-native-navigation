#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNNavigationControllerPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "RNNNavigationController.h"

@interface RNNNavigationControllerPresenterTest : XCTestCase

@property (nonatomic, strong) RNNNavigationControllerPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) id bindedViewController;

@end

@implementation RNNNavigationControllerPresenterTest

- (void)setUp {
	[super setUp];
	self.uut = [[RNNNavigationControllerPresenter alloc] init];
	self.bindedViewController = [OCMockObject partialMockForObject:[RNNNavigationController new]];
	[self.uut bindViewController:self.bindedViewController];
	self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptions_shouldSetBackButtonColor_withDefaultValues {
	[[_bindedViewController expect] rnn_setBackButtonColor:nil];
	[self.uut applyOptions:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptions_shouldSetBackButtonColor_withColor {
	self.options.topBar.backButton.color = [[Color alloc] initWithValue:[UIColor redColor]];
	[[_bindedViewController expect] rnn_setBackButtonColor:[UIColor redColor]];
	[self.uut applyOptions:self.options];
	[_bindedViewController verify];
}


- (void)testApplyOptions_shouldSetBackButtonOnBindedViewController_withTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	[[_bindedViewController expect] rnn_setBackButtonIcon:nil withColor:nil title:title.get];
	[self.uut applyOptions:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptions_shouldSetBackButtonOnBindedViewController_withHideTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	self.options.topBar.backButton.showTitle = [[Bool alloc] initWithValue:@(0)];
	[[_bindedViewController expect] rnn_setBackButtonIcon:nil withColor:nil title:@""];
	[self.uut applyOptions:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptions_shouldSetBackButtonOnBindedViewController_withIcon {
	Image* image = [[Image alloc] initWithValue:[UIImage new]];
	self.options.topBar.backButton.icon = image;
	[[_bindedViewController expect] rnn_setBackButtonIcon:image.get withColor:nil title:nil];
	[self.uut applyOptions:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBindedViewController_withTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	[[_bindedViewController expect] rnn_setBackButtonIcon:nil withColor:nil title:title.get];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBindedViewController_withHideTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	self.options.topBar.backButton.showTitle = [[Bool alloc] initWithValue:@(0)];
	[[_bindedViewController expect] rnn_setBackButtonIcon:nil withColor:nil title:@""];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBindedViewController_withIcon {
	Image* image = [[Image alloc] initWithValue:[UIImage new]];
	self.options.topBar.backButton.icon = image;
	[[_bindedViewController expect] rnn_setBackButtonIcon:image.get withColor:nil title:nil];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBindedViewController_withDefaultValues {
	[[_bindedViewController expect] rnn_setBackButtonIcon:nil withColor:nil title:nil];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptionsBeforePoppingShouldSetTopBarBackgroundForPoppingViewController {
	_options.topBar.background.color = [[Color alloc] initWithValue:[UIColor redColor]];
	
	[[_bindedViewController expect] setTopBarBackgroundColor:_options.topBar.background.color.get];
	[self.uut applyOptionsBeforePopping:self.options];
	[_bindedViewController verify];
}

- (void)testApplyOptionsBeforePoppingShouldSetLargeTitleForPoppingViewController {
	_options.topBar.largeTitle.visible = [[Bool alloc] initWithBOOL:YES];
	
	[self.uut applyOptionsBeforePopping:self.options];
	XCTAssertTrue([[self.uut.bindedViewController navigationBar] prefersLargeTitles]);
}

- (void)testApplyOptionsBeforePoppingShouldSetDefaultLargeTitleFalseForPoppingViewController {
	_options.topBar.largeTitle.visible = nil;
	
	[self.uut applyOptionsBeforePopping:self.options];
	XCTAssertFalse([[self.uut.bindedViewController navigationBar] prefersLargeTitles]);
}

@end
