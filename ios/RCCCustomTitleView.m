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

@end

@implementation RCCCustomTitleView

- (instancetype)initWithFrame:(CGRect)frame subView:(RCTRootView*)subView alignment:(NSString*)alignment {
    self = [super init];
    
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

@end
