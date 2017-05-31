//
// Created by Hank Brekke on 5/19/17.
// Copyright (c) 2017 hnryjms. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface RCCCustomButtonView : UIView

@property (nonatomic, strong) NSString *callbackId;
@property (nonatomic, strong) NSString *buttonId;

- (instancetype)initWithFrame:(CGRect)frame subView:(UIView*)subView;

@end