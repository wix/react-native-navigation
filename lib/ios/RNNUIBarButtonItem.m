#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNUIBarButtonItem.h"
#import "RCTConvert+UIBarButtonSystemItem.h"

@implementation RNNUIBarButtonItem

-(instancetype)init:(NSString*)buttonId withIcon:(UIImage*)iconImage {
	UIButton* button = [[UIButton alloc] init];
	[button addTarget:self action:@selector(onButtonPressed) forControlEvents:UIControlEventTouchUpInside];
	[button setImage:iconImage forState:UIControlStateNormal];
	self = [super initWithCustomView:button];
	self.buttonId = buttonId;
	return self;
}

-(instancetype)init:(NSString*)buttonId withTitle:(NSString*)title {
	self = [super initWithTitle:title style:UIBarButtonItemStylePlain target:nil action:nil];
	self.buttonId = buttonId;
	return self;
}

-(instancetype)init:(NSString*)buttonId withCustomView:(RCTRootView *)reactView {
	self = [super initWithCustomView:reactView];
	
	reactView.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
	reactView.delegate = self;
	reactView.backgroundColor = [UIColor clearColor];
	self.buttonId = buttonId;
	return self;
}

-(instancetype)init:(NSString*)buttonId withSystemItem:(NSString *)systemItemName {
	UIBarButtonSystemItem systemItem = [RCTConvert UIBarButtonSystemItem:systemItemName];
	self = [super initWithBarButtonSystemItem:systemItem target:nil action:nil];
	self.buttonId = buttonId;
	return self;
}

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
	CGSize size = rootView.intrinsicContentSize;
	rootView.frame = CGRectMake(0, 0, size.width, size.height);
	self.width = size.width;
}

- (void)onButtonPressed {
	[self.target performSelector:self.action
					  withObject:self
					  afterDelay:0];
}

@end
