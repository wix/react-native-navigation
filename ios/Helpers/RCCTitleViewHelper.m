//
//  RCCTitleViewHelper.m
//  ReactNativeControllers
//
//  Created by Ran Greenberg on 06/09/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import "RCCTitleViewHelper.h"
#import <React/RCTConvert.h>
#import "RCCViewController.h"
#import "RCTHelpers.h"

@implementation RCCTitleView


@end

@interface RCCTitleViewHelper ()

@property (nonatomic, weak) UIViewController *viewController;
@property (nonatomic, weak) UINavigationController *navigationController;

@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSString *subtitle;
@property (nonatomic, strong) id titleImageData;
@property (nonatomic) BOOL isSetSubtitle;

@property (nonatomic, strong) RCCTitleView *titleView;

@end


@implementation RCCTitleViewHelper

- (instancetype)init:(UIViewController*)viewController
navigationController:(UINavigationController*)navigationController
               title:(NSString*)title subtitle:(NSString*)subtitle
      titleImageData:(id)titleImageData
       isSetSubtitle:(BOOL)isSetSubtitle

{
    self = [super init];
    if (self) {
        self.viewController = viewController;
        self.navigationController = navigationController;
        if(isSetSubtitle){
            self.title = viewController.navigationItem.title;
        } else {
            self.title = [RCCTitleViewHelper validateString:title];
        }
        self.subtitle = [RCCTitleViewHelper validateString:subtitle];
        self.titleImageData = titleImageData;
    }
    return self;
}

+(NSString*)validateString:(NSString*)string {
    if ([string isEqual:[NSNull null]]) {
        return nil;
    }
    
    return string;
}

-(void)setup:(NSDictionary*)style
{
    if (!self.navigationController)
    {
        return;
    }
    
    if ([self.viewController isKindOfClass:[RCCViewController class]]) {
        NSMutableDictionary *mergedStyle = [NSMutableDictionary dictionaryWithDictionary:((RCCViewController*)self.viewController).navigatorStyle];
        [mergedStyle addEntriesFromDictionary:style];
        
        style = mergedStyle;
    }
    
    CGRect navigationBarBounds = self.navigationController.navigationBar.bounds;
    
    self.titleView = [[RCCTitleView alloc] initWithFrame:navigationBarBounds];
    self.titleView.backgroundColor = [UIColor clearColor];
    self.titleView.clipsToBounds = YES;
    
    self.viewController.navigationItem.title = self.title;
    
    if ([self isTitleOnly]) {
        self.viewController.navigationItem.titleView = nil;
        return;
    }
    
    if ([self isTitleImage])
    {
        [self setupTitleImage];
        return;
    }

    if (self.title)
    {
        self.titleView.titleLabel = [self setupTitle:style];
    }

    if (self.subtitle)
    {
        self.titleView.subtitleLabel = [self setupSubtitle:style];
    }

    [self layoutTitleView:navigationBarBounds titleLabel:self.titleView.titleLabel subtitleLabel:self.titleView.subtitleLabel];
    
    self.viewController.navigationItem.titleView = self.titleView;
}


-(BOOL)isTitleOnly
{
    return self.title && !self.subtitle && ![self isTitleImage];
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
    
    self.viewController.navigationItem.titleView = imageView;
}


-(void)layoutTitleView:(CGRect)navigationBarBounds titleLabel:(UILabel*)titleLabel subtitleLabel:(UILabel*)subtitleLabel
{
    CGRect titleViewFrame = CGRectMake(
        navigationBarBounds.origin.x,
        navigationBarBounds.origin.y,
        MAX(
            titleLabel ? titleLabel.frame.size.width : 0,
            subtitleLabel ? subtitleLabel.frame.size.width : 0
        ),
        0
    );

    CGFloat height = 0;
    for (UIView *view in self.titleView.subviews) {
        CGRect viewFrame = view.frame;
        viewFrame.size.width = titleViewFrame.size.width;
        viewFrame.origin.x = (titleViewFrame.size.width - viewFrame.size.width) / 2;
        viewFrame.origin.y = height;
        height += viewFrame.size.height;
        view.frame = viewFrame;
    }

    titleViewFrame.size.height = height;
    self.titleView.frame = titleViewFrame;
}


-(UILabel*)setupSubtitle:(NSDictionary*)style
{
    CGRect subtitleFrame = self.titleView.frame;
    subtitleFrame.size.height /= 2;
    
    UILabel *subtitleLabel = [[UILabel alloc] initWithFrame:subtitleFrame];
    subtitleLabel.textAlignment = NSTextAlignmentCenter;
    subtitleLabel.backgroundColor = [UIColor clearColor];
    
    NSMutableDictionary *subtitleAttributes = [RCTHelpers textAttributesFromDictionary:style withPrefix:@"navBarSubtitle" baseFont:[UIFont systemFontOfSize:14.f]];
    [subtitleLabel setAttributedText:[[NSAttributedString alloc] initWithString:self.subtitle attributes:subtitleAttributes]];
    
    CGSize labelSize = [subtitleLabel.text sizeWithAttributes:subtitleAttributes];
    CGRect labelframe = subtitleLabel.frame;
    labelframe.size = labelSize;
    subtitleLabel.frame = labelframe;
    [subtitleLabel sizeToFit];
    
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
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.backgroundColor = [UIColor clearColor];
    
    UIFont *titleFont = [UIFont boldSystemFontOfSize:17.f];
    
    id fontSize = style[@"navBarTitleFontSize"];
    if (fontSize) {
        CGFloat fontSizeFloat = [RCTConvert CGFloat:fontSize];
        titleFont = [UIFont boldSystemFontOfSize:fontSizeFloat];
    }
    
    titleLabel.font = titleFont;
    
    NSMutableDictionary *titleAttributes = [RCTHelpers textAttributesFromDictionary:style withPrefix:@"navBarTitle" baseFont:[UIFont systemFontOfSize:14.f]];
    [titleLabel setAttributedText:[[NSAttributedString alloc] initWithString:self.title attributes:titleAttributes]];
    
    CGSize labelSize = [titleLabel.text sizeWithAttributes:@{NSFontAttributeName:titleFont}];
    CGRect labelframe = titleLabel.frame;
    labelframe.size = labelSize;
    titleLabel.frame = labelframe;

    id navBarTextColor = style[@"navBarTextColor"];
    if (navBarTextColor)
    {
        UIColor *color = navBarTextColor != (id)[NSNull null] ? [RCTConvert UIColor:navBarTextColor] : nil;
        titleLabel.textColor = color;
    }

    [titleLabel sizeToFit];
    [self.titleView addSubview:titleLabel];
    
    return titleLabel;
}


@end
