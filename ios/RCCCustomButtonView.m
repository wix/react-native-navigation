//
// Created by Hank Brekke on 5/19/17.
// Copyright (c) 2017 hnryjms. All rights reserved.
//

#import "RCCCustomButtonView.h"

@interface RCCCustomButtonView ()

@property (nonatomic, strong) UIView *subView;

@end


@implementation RCCCustomButtonView

- (instancetype)initWithFrame:(CGRect)frame subView:(UIView*)subView {
    self = [super initWithFrame:frame];

    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.subView = subView;

        subView.frame = self.bounds;
        subView.autoresizingMask = (UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth);
        [self addSubview:subView];
    }

    return self;
}

@end