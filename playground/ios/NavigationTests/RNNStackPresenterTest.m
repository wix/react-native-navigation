#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNStackPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "RNNStackController.h"

@interface RNNStackPresenterTest : XCTestCase

@property (nonatomic, strong) RNNStackPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) id boundViewController;

@end

@implementation RNNStackPresenterTest

- (void)setUp {
	[super setUp];
	self.uut = [[RNNStackPresenter alloc] init];
	self.boundViewController = [OCMockObject partialMockForObject:[RNNStackController new]];
    [self.uut boundViewController:self.boundViewController];
	self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptions_shouldSetBackButtonColor_withDefaultValues {
	[[_boundViewController expect] setBackButtonColor:nil];
	[self.uut applyOptions:self.options];
	[_boundViewController verify];
}

- (void)testApplyOptions_shouldSetBackButtonColor_withColor {
	self.options.topBar.backButton.color = [[Color alloc] initWithValue:[UIColor redColor]];
	[[_boundViewController expect] setBackButtonColor:[UIColor redColor]];
	[self.uut applyOptions:self.options];
	[_boundViewController verify];
}

- (void)testApplyOptionsBeforePoppingShouldSetTopBarBackgroundForPoppingViewController {
	_options.topBar.background.color = [[Color alloc] initWithValue:[UIColor redColor]];
	
	[[_boundViewController expect] setTopBarBackgroundColor:_options.topBar.background.color.get];
	[self.uut applyOptionsBeforePopping:self.options];
	[_boundViewController verify];
}

- (void)testApplyOptionsBeforePoppingShouldSetLargeTitleForPoppingViewController {
	_options.topBar.largeTitle.visible = [[Bool alloc] initWithBOOL:YES];
	
	[self.uut applyOptionsBeforePopping:self.options];
	XCTAssertTrue([[self.uut.boundViewController navigationBar] prefersLargeTitles]);
}

- (void)testApplyOptionsBeforePoppingShouldSetDefaultLargeTitleFalseForPoppingViewController {
	_options.topBar.largeTitle.visible = nil;
	
	[self.uut applyOptionsBeforePopping:self.options];
	XCTAssertFalse([[self.uut.boundViewController navigationBar] prefersLargeTitles]);
}

- (void)testApplyOptions_shouldSetBackButtonOnBoundViewController_withTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	[[_boundViewController expect] setBackButtonIcon:nil withColor:nil title:title.get showTitle:YES];
	[self.uut applyOptions:self.options];
	[_boundViewController verify];
}

- (void)testApplyOptions_shouldSetBackButtonOnBoundViewController_withHideTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	self.options.topBar.backButton.showTitle = [[Bool alloc] initWithValue:@(0)];
	[[(id) self.boundViewController expect] setBackButtonIcon:nil withColor:nil title:title.get showTitle:self.options.topBar.backButton.showTitle.get];
	[self.uut applyOptions:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptions_shouldSetBackButtonOnBoundViewController_withIcon {
	Image* image = [[Image alloc] initWithValue:[UIImage new]];
	self.options.topBar.backButton.icon = image;
	[[(id) self.boundViewController expect] setBackButtonIcon:image.get withColor:nil title:nil showTitle:YES];
	[self.uut applyOptions:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptions_shouldSetBackButtonOnBoundViewController_withDefaultValues {
	[[(id) self.boundViewController expect] setBackButtonIcon:nil withColor:nil title:nil showTitle:YES];
	[self.uut applyOptions:self.options];
	[(id)self.boundViewController verify];
}

@end
