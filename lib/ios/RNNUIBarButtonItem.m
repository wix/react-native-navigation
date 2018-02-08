#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNUIBarButtonItem.h"

@implementation RNNUIBarButtonItem

-(instancetype)init:(NSString*)buttonId withIcon:(UIImage*)iconImage {
	self = [super initWithImage:iconImage style:UIBarButtonItemStylePlain target:nil action:nil];
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

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
	CGSize size = rootView.intrinsicContentSize;
	rootView.frame = CGRectMake(0, 0, size.width, size.height);
	self.width = size.width;
}

@end
