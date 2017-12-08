//
//  RCCTitleView.m
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 26/04/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import "RCCCustomTitleView.h"

@interface RCCCustomTitleView ()
@property (nonatomic, strong) UIView *subView;
@property (nonatomic, strong) NSString *subViewAlign;
@property BOOL correctedTheNavBarWidth;
@property CGRect correctFrame;
@end

@implementation RCCCustomTitleView


-(instancetype)initWithFrame:(CGRect)frame subView:(UIView*)subView alignment:(NSString*)alignment {
    _correctFrame = frame;
    self = [super initWithFrame:_correctFrame];
    
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.subView = subView;
        self.subViewAlign = alignment;
        
        subView.frame = self.bounds;
        [self addSubview:subView];
    }
    
    return self;
}


-(void)layoutSubviews {
    [super layoutSubviews];
    
    if ([self.subViewAlign isEqualToString:@"fill"]) {
        self.subView.frame = self.bounds;
    }
    else {
        
        CGFloat superViewWidth = self.superview.frame.size.width;
        CGFloat paddingLeftFromCenter = (superViewWidth/2) - self.frame.origin.x;
        CGFloat paddingRightFromCenter = self.frame.size.width - paddingLeftFromCenter;;
        CGRect reactViewFrame = self.subView.bounds;
        CGFloat minPadding = MIN(paddingLeftFromCenter, paddingRightFromCenter);
        
        reactViewFrame.size.width = minPadding*2;
        reactViewFrame.origin.x = paddingLeftFromCenter - minPadding;
        self.subView.frame = reactViewFrame;
    }
}

- (void)setFrame:(CGRect) newFrame {
    float correctNavBarWidth = _correctFrame.size.width;
    float newNavBarWidth = newFrame.size.width;
    BOOL frameNeedsToBeCorrected = newNavBarWidth < correctNavBarWidth;

    if (_correctedTheNavBarWidth == NO) {
        if (frameNeedsToBeCorrected) {
            // first we need to find out the total horizontal margin
            float navBarHorizontalMargin = correctNavBarWidth - newNavBarWidth;
            
            // then we need to place the nav bar half times the horizontal margin to the left
            newFrame.origin.x -= navBarHorizontalMargin / 2;
            
            // and finally set the width to whatever we initially intended the width to be
            newFrame.size.width = correctNavBarWidth;
            
            [super setFrame:newFrame];
            _correctedTheNavBarWidth = YES;
        } else { // if frame needs no correction
            [super setFrame:newFrame];
        }
    }
}


- (void) viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(id<UIViewControllerTransitionCoordinator>)coordinator {
    // whenever the orientation changes this runs
    // and sets the nav bar item width to the new size width
    CGRect newFrame = self.frame;

    if (newFrame.size.width < size.width) {
        newFrame.size.width = size.width;
        _correctFrame = newFrame;
    }
    [super setFrame:newFrame];
    [self setNeedsLayout];
}

@end
