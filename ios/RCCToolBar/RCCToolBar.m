//
//  RCCToolBar.m
//  ReactNativeControllers
//
//  Created by Ran Greenberg on 09/05/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import "RCCToolBar.h"

@interface RCCToolBarView : UIToolbar

@property (nonatomic) BOOL toolBarTranslucent;

@end


@implementation RCCToolBarView

-(instancetype)init
{
    self = [super init];
    if (self)
    {
        self.toolBarTranslucent = self.translucent;
    }
    return self;
}


-(void)didMoveToWindow
{
    [super didMoveToWindow];
    self.translucent = self.toolBarTranslucent;
}

@end


@implementation RCCToolBar


RCT_EXPORT_MODULE()

- (UIView *)view
{
    return [[RCCToolBarView alloc] init];
}


RCT_CUSTOM_VIEW_PROPERTY(translucent, BOOL, RCCToolBarView)
{
    view.toolBarTranslucent = [RCTConvert BOOL:json];
}


@end