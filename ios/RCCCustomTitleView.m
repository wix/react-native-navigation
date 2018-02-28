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
@property float initialWidth;
@end

@implementation RCCCustomTitleView


-(instancetype)initWithFrame:(CGRect)frame subView:(UIView*)subView alignment:(NSString*)alignment {
    _initialWidth = frame.size.width;
    self = [super initWithFrame:frame];
    
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

- (void)setFrame:(CGRect) frame {
    float referenceWidth = [self statusBarWidth];
    if (referenceWidth == 0) {
        referenceWidth = _initialWidth;
    }
    float newNavBarWidth = frame.size.width;
    BOOL frameNeedsToBeCorrected = newNavBarWidth < referenceWidth || CGRectEqualToRect(self.frame, CGRectZero);

    if (frameNeedsToBeCorrected) {
        // first we need to find out the total point diff of the status bar and the nav bar
        float navBarHorizontalMargin = referenceWidth - newNavBarWidth;
        
        CGRect correctedFrame = frame;

        // then we need to place the nav bar half times the horizontal margin to the left
        correctedFrame.origin.x = -(navBarHorizontalMargin / 2);
        
        // and finally set the width so that it's equal to the status bar width
        correctedFrame.size.width = referenceWidth;
        
        [super setFrame:correctedFrame];
    } else if (frame.size.height != self.frame.size.height) { // otherwise
        // if only the height has changed
        CGRect newHeightFrame = self.frame;
        // make sure we update just the height
        newHeightFrame.size.height = frame.size.height;
        [super setFrame:newHeightFrame];
    }
    
    // keep a ref to the last frame, so that we avoid setting the frame twice for no reason
//    _lastFrame = frame;
}


- (void) viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(id<UIViewControllerTransitionCoordinator>)coordinator {
    // whenever the orientation changes this runs
    // and sets the nav bar item width to the new size width
    CGRect newFrame = self.frame;

    if (newFrame.size.width < size.width) {
        newFrame.size.width = size.width;
        newFrame.origin.x = 0;
    }
    [super setFrame:newFrame];
}

-(float) statusBarWidth {
    CGSize statusBarSize = [[UIApplication sharedApplication] statusBarFrame].size;
    return MAX(statusBarSize.width, statusBarSize.height);
}


@end
