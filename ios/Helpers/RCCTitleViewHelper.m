//
//  RCCTitleViewHelper.m
//  ReactNativeControllers
//
//  Created by Ran Greenberg on 06/09/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import "RCCTitleViewHelper.h"
#import <React/RCTConvert.h>

@interface RCCTitleViewHelper ()

@property (nonatomic, weak) UIViewController *viewController;
@property (nonatomic, weak) UINavigationController *navigationController;

@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSString *subtitle;
@property (nonatomic, strong) id titleImageData;

@property (nonatomic, strong) UIView *titleView;

@end


@implementation RCCTitleViewHelper

- (instancetype)init:(UIViewController*)viewController
navigationController:(UINavigationController*)navigationController
               title:(NSString*)title subtitle:(NSString*)subtitle
      titleImageData:(id)titleImageData
{
    self = [super init];
    if (self) {
        self.viewController = viewController;
        self.navigationController = navigationController;
        self.title = title;
        self.subtitle = subtitle;
        self.titleImageData = titleImageData;
    }
    return self;
}

-(void)setup:(NSDictionary*)style
{
    if (!self.navigationController)
    {
        return;
    }
    
    CGRect navigationBarBounds = self.navigationController.navigationBar.bounds;
    
    UILabel *titleLabel;
    UILabel *subtitleLabel;
    
    self.titleView = [[UIView alloc] initWithFrame:navigationBarBounds];
    self.titleView.backgroundColor = [UIColor clearColor];
    self.titleView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
    self.titleView.clipsToBounds = YES;
    
    
    self.viewController.title = self.title;
    
    if ([self isTitleOnly]) {
        self.viewController.navigationItem.titleView = nil;
        return;
    }
    
    if ([self isTitleImage])
    {
        [self setupTitleImage];
        return;
    }
    
    if (self.subtitle)
    {
        subtitleLabel = [self setupSubtitle:style];
    }
    
    if (self.title)
    {
        titleLabel = [self setupTitle:style];
    }
    
    [self centerTitleView:navigationBarBounds titleLabel:titleLabel subtitleLabel:subtitleLabel];
    
    self.viewController.navigationItem.titleView = self.titleView;
}


-(BOOL)isTitleOnly
{
    return self.title && !self.subtitle && !self.titleImageData;
}


-(BOOL)isTitleImage
{
    return self.titleImageData && ![self.titleImageData isEqual:[NSNull null]];
}


-(void)setupTitleImage
{
    UIImage *titleImage = [RCTConvert UIImage:self.titleImageData];
    UIImageView *imageView = [[UIImageView alloc] initWithImage:titleImage];
    imageView.contentMode = UIViewContentModeScaleAspectFit;
    imageView.autoresizingMask = self.titleView.autoresizingMask;
    
    self.viewController.navigationItem.titleView = imageView;
}


-(void)centerTitleView:(CGRect)navigationBarBounds titleLabel:(UILabel*)titleLabel subtitleLabel:(UILabel*)subtitleLabel
{
    CGRect titleViewFrame = navigationBarBounds;
    titleViewFrame.size.width = MAX(titleLabel.frame.size.width, subtitleLabel.frame.size.width);;
    self.titleView.frame = titleViewFrame;
    
    for (UIView *view in self.titleView.subviews)
    {
        CGRect viewFrame = view.frame;
        viewFrame.size.width = self.titleView.frame.size.width;
        viewFrame.origin.x = (self.titleView.frame.size.width - viewFrame.size.width)/2;
        view.frame = viewFrame;
    }
    
}


-(UILabel*)setupSubtitle:(NSDictionary*)style
{
    CGRect subtitleFrame = self.titleView.frame;
    subtitleFrame.size.height /= 2;
    subtitleFrame.origin.y = subtitleFrame.size.height;
    
    UILabel *subtitleLabel = [[UILabel alloc] initWithFrame:subtitleFrame];
    subtitleLabel.text = self.subtitle;
    subtitleLabel.textAlignment = NSTextAlignmentCenter;
    subtitleLabel.backgroundColor = [UIColor clearColor];
    subtitleLabel.autoresizingMask = self.titleView.autoresizingMask;
    UIFont *subtitleFont = [UIFont systemFontOfSize:14.f];
    
    id fontSize = style[@"navBarSubtitleFontSize"];
    if (fontSize) {
        CGFloat fontSizeFloat = [RCTConvert CGFloat:fontSize];
        subtitleFont = [UIFont boldSystemFontOfSize:fontSizeFloat];
    }
    
    subtitleLabel.font = subtitleFont;
    
    id navBarSubtitleTextColor = style[@"navBarSubtitleTextColor"];
    if (navBarSubtitleTextColor)
    {
        UIColor *color = navBarSubtitleTextColor != (id)[NSNull null] ? [RCTConvert UIColor:navBarSubtitleTextColor] : nil;
        subtitleLabel.textColor = color;
    }
    
    CGSize labelSize = [subtitleLabel.text sizeWithAttributes:@{NSFontAttributeName:subtitleFont}];
    CGRect labelframe = subtitleLabel.frame;
    labelframe.size = labelSize;
    subtitleLabel.frame = labelframe;
    
    [self.titleView addSubview:subtitleLabel];
    
    return subtitleLabel;
}


-(UILabel*)setupTitle:(NSDictionary*)style
{
    CGRect titleFrame = self.titleView.frame;
    if (self.subtitle)
    {
        titleFrame.size.height /= 2;
    }
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:titleFrame];
    titleLabel.text = self.title;
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.backgroundColor = [UIColor clearColor];
    
    titleLabel.autoresizingMask = self.titleView.autoresizingMask;
    
    UIFont *titleFont = [UIFont boldSystemFontOfSize:17.f];
    
    id fontSize = style[@"navBarTitleFontSize"];
    if (fontSize) {
        CGFloat fontSizeFloat = [RCTConvert CGFloat:fontSize];
        titleFont = [UIFont boldSystemFontOfSize:fontSizeFloat];
    }
    
    titleLabel.font = titleFont;
    
    CGSize labelSize = [titleLabel.text sizeWithAttributes:@{NSFontAttributeName:titleFont}];
    CGRect labelframe = titleLabel.frame;
    labelframe.size = labelSize;
    titleLabel.frame = labelframe;
    
    if (!self.subtitle)
    {
        titleLabel.center = self.titleView.center;
    }
    
    id navBarTextColor = style[@"navBarTextColor"];
    if (navBarTextColor)
    {
        UIColor *color = navBarTextColor != (id)[NSNull null] ? [RCTConvert UIColor:navBarTextColor] : nil;
        titleLabel.textColor = color;
    }
    
    [self.titleView addSubview:titleLabel];
    
    return titleLabel;
}


@end
