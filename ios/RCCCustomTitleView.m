//
//  RCCTitleView.m
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 26/04/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import "RCCCustomTitleView.h"

@interface RCCCustomTitleView ()

@property (nonatomic, strong) RCTRootView *subView;
@property (nonatomic, strong) NSString *alignment;
@property float initialWidth;
@end

@implementation RCCCustomTitleView

- (instancetype)initWithFrame:(CGRect)frame subView:(RCTRootView*)subView alignment:(NSString*)alignment {
    self = [super init];
    _initialWidth = frame.size.width;
    
    if (self) {
        self.subView = subView;
        self.alignment = alignment;
        
        self.backgroundColor = [UIColor clearColor];
        self.subView.backgroundColor = [UIColor clearColor];
                
        if ([alignment isEqualToString:@"fill"]) {
            self.frame = frame;
            subView.sizeFlexibility = RCTRootViewSizeFlexibilityNone;
        } else {
            self.subView.delegate = self;
            subView.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
        }
        
        [self addSubview:subView];
    }
    
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if ([self.alignment isEqualToString:@"fill"]) {
        [self.subView setFrame:self.bounds];
    }
}

- (NSString *)alignment {
    return _alignment ? _alignment : @"center";
}

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
    if ([self.alignment isEqualToString:@"center"]) {
        [self setFrame:CGRectMake(self.frame.origin.x - rootView.intrinsicContentSize.width / 2, self.frame.origin.y - rootView.intrinsicContentSize.height / 2, rootView.intrinsicContentSize.width, rootView.intrinsicContentSize.height)];
        [self.subView setFrame:CGRectMake(0, 0, rootView.intrinsicContentSize.width, rootView.intrinsicContentSize.height)];
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
