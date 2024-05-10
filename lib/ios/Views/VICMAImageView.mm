//
//  VIAnimatableImageView.m
//  VIAnimatableImageViewDemo
//
//  Created by Vito on 12/30/14.
//  Copyright (c) 2014 Vito. All rights reserved.
// 	project can be found at: https://github.com/vitoziv/VICMAImageView

#import "VICMAImageView.h"

@interface VICMAImageView ()

@property(nonatomic, strong) UIImageView *imageView;

@end

@implementation VICMAImageView

- (void)commonInit {
    self.clipsToBounds = YES;
    self.contentMode = UIViewContentModeScaleAspectFit;

    if (!_imageView) {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:self.frame];
        imageView.contentMode = UIViewContentModeScaleToFill;
        [self addSubview:imageView];
        _imageView = imageView;
    }
}

- (instancetype)initWithImage:(UIImage *)image {
    self = [super init];
    if (self) {
        [self commonInit];
        _imageView.image = image;
    }

    return self;
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self commonInit];
    }

    return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
        [self commonInit];
    }

    return self;
}

- (CGSize)presentationImageSize {
    return self.imageView.bounds.size;
}

- (void)setImage:(UIImage *)image {
    self.imageView.image = image;
    [self updateView];
}

- (UIImage *)image {
    return self.imageView.image;
}

- (void)setFrame:(CGRect)frame {
    [super setFrame:frame];
    [self updateView];
}

- (void)setContentMode:(UIViewContentMode)contentMode {
    [super setContentMode:contentMode];
    [self updateView];
}

- (void)updateView {
    if (self.bounds.size.width == 0 || self.bounds.size.height == 0 || self.image.size.width == 0 ||
        self.image.size.height == 0) {
        return;
    }

    switch (self.contentMode) {
    case UIViewContentModeScaleAspectFit:
        [self updateViewToAspectFit];
        break;

    case UIViewContentModeScaleAspectFill:
        [self updateViewToAspectFill];
        break;

    case UIViewContentModeScaleToFill:
        [self updateViewToScaleToFill];
        break;

    case UIViewContentModeCenter:
        [self updateViewToCenter];
        break;

    case UIViewContentModeBottom:
        [self updateViewToBottom];
        break;

    case UIViewContentModeBottomLeft:
        [self updateViewToBottomLeft];
        break;

    case UIViewContentModeBottomRight:
        [self updateViewToBottomRight];
        break;

    case UIViewContentModeLeft:
        [self updateViewToLeft];
        break;

    case UIViewContentModeRight:
        [self updateViewToRight];
        break;

    case UIViewContentModeTop:
        [self updateViewToTop];
        break;

    case UIViewContentModeTopLeft:
        [self updateViewToTopLeft];
        break;

    case UIViewContentModeTopRight:
        [self updateViewToTopRight];
        break;

    case UIViewContentModeRedraw:
        [self updateViewToScaleToFill];
        break;

    default:
        break;
    }
}

- (void)updateViewToAspectFit {
    CGSize imageSize = CGSizeMake(self.imageView.image.size.width / self.imageView.image.scale,
                                  self.imageView.image.size.height / self.imageView.image.scale);

    CGFloat widthRatio = imageSize.width / self.bounds.size.width;
    CGFloat heightRatio = imageSize.height / self.bounds.size.height;

    if (widthRatio > heightRatio) {
        imageSize = CGSizeMake(imageSize.width / widthRatio, imageSize.height / widthRatio);
    } else {
        imageSize = CGSizeMake(imageSize.width / heightRatio, imageSize.height / heightRatio);
    }

    self.imageView.bounds = CGRectMake(0, 0, imageSize.width, imageSize.height);
    self.imageView.center = CGPointMake(self.bounds.size.width / 2, self.bounds.size.height / 2);
}

- (void)updateViewToAspectFill {
    CGSize imageSize = CGSizeMake(self.imageView.image.size.width / self.imageView.image.scale,
                                  self.imageView.image.size.height / self.imageView.image.scale);

    CGFloat widthRatio = imageSize.width / self.bounds.size.width;
    CGFloat heightRatio = imageSize.height / self.bounds.size.height;

    if (widthRatio > heightRatio) {
        imageSize = CGSizeMake(imageSize.width / heightRatio, imageSize.height / heightRatio);
    } else {
        imageSize = CGSizeMake(imageSize.width / widthRatio, imageSize.height / widthRatio);
    }

    self.imageView.bounds = CGRectMake(0, 0, imageSize.width, imageSize.height);
    [self centerImageViewToSuperView];
}

- (void)updateViewToScaleToFill {
    self.imageView.bounds = CGRectMake(0, 0, self.bounds.size.width, self.bounds.size.height);
    [self centerImageViewToSuperView];
}

- (void)updateViewToCenter {
    [self fitImageViewSizeToImageSize];
    [self centerImageViewToSuperView];
}

- (void)updateViewToBottom {
    [self fitImageViewSizeToImageSize];
    self.imageView.center = CGPointMake(self.bounds.size.width / 2,
                                        self.bounds.size.height - self.image.size.height / 2);
}

- (void)updateViewToBottomLeft {
    [self fitImageViewSizeToImageSize];

    self.imageView.center = CGPointMake(self.image.size.width / 2,
                                        self.bounds.size.height - self.image.size.height / 2);
}

- (void)updateViewToBottomRight {
    [self fitImageViewSizeToImageSize];
    self.imageView.center = CGPointMake(self.bounds.size.width - self.image.size.width / 2,
                                        self.bounds.size.height - self.image.size.height / 2);
}

- (void)updateViewToLeft {
    [self fitImageViewSizeToImageSize];

    self.imageView.center = CGPointMake(self.image.size.width / 2, self.bounds.size.height / 2);
}

- (void)updateViewToRight {
    [self fitImageViewSizeToImageSize];

    self.imageView.center = CGPointMake(self.bounds.size.width - self.image.size.width / 2,
                                        self.bounds.size.height / 2);
}

- (void)updateViewToTop {
    [self fitImageViewSizeToImageSize];

    self.imageView.center = CGPointMake(self.bounds.size.width / 2, self.image.size.height / 2);
}

- (void)updateViewToTopLeft {
    [self fitImageViewSizeToImageSize];

    self.imageView.center = CGPointMake(self.image.size.width / 2, self.image.size.height / 2);
}

- (void)updateViewToTopRight {
    [self fitImageViewSizeToImageSize];
    self.imageView.center =
        CGPointMake(self.bounds.size.width - self.image.size.width / 2, self.image.size.height / 2);
}

#pragma mark - Helper

- (void)fitImageViewSizeToImageSize {
    CGSize imageSize = CGSizeMake(self.imageView.image.size.width / self.imageView.image.scale,
                                  self.imageView.image.size.height / self.imageView.image.scale);

    self.imageView.bounds = CGRectMake(0, 0, imageSize.width, imageSize.height);
}

- (void)centerImageViewToSuperView {
    self.imageView.center = CGPointMake(self.bounds.size.width / 2, self.bounds.size.height / 2);
}

@end
