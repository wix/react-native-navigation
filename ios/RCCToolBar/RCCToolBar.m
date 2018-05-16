//
//  RCCToolBar.m
//  ReactNativeControllers
//
//  Created by Ran Greenberg on 09/05/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import "RCCToolBar.h"

@interface RCCToolBarView : UIView

@property (nonatomic) BOOL toolBarTranslucent;
@property (nonatomic, strong) UIToolbar *toolbar;

@end


@implementation RCCToolBarView

-(instancetype)init
{
    self = [super init];
    if (self)
    {
        self.toolBarTranslucent = self.toolbar.translucent;
        self.backgroundColor = [UIColor clearColor];
        self.toolbar = [[UIToolbar alloc] init];
        [self addSubview:self.toolbar];
    }
    return self;
}


-(void)didMoveToWindow
{
    [super didMoveToWindow];
    self.toolbar.translucent = self.toolBarTranslucent;
}

-(void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
    
    self.toolbar.frame = self.bounds;
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
