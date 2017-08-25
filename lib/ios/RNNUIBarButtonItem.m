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

@end
