//
//  RCTHelpers.m
//  ReactNativeControllers
//
//  Created by Artal Druk on 25/05/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import "RCTHelpers.h"
#import "RCCViewController.h"
#import "RCCNavigationController.h"
#import <objc/runtime.h>

#if __has_include(<React/RCTView.h>)
#import <React/RCTView.h>
#elif __has_include("RCTView.h")
#import "RCTView.h"
#elif __has_include("React/RCTView.h")
#import "React/RCTView.h"   // Required when used as a Pod in a Swift project
#endif

#if __has_include(<React/RCTScrollView.h>)
#import <React/RCTScrollView.h>
#elif __has_include("RCTScrollView.h")
#import "RCTScrollView.h"
#elif __has_include("React/RCTScrollView.h")
#import "React/RCTScrollView.h"   // Required when used as a Pod in a Swift project
#endif

#if __has_include(<React/RCTFont.h>)
#import <React/RCTFont.h>
#elif __has_include("RCTFont.h")
#import "RCTFont.h"
#elif __has_include("React/RCTFont.h")
#import "React/RCTFont.h"   // Required when used as a Pod in a Swift project
#endif

@implementation RCTHelpers

+(NSArray*)getAllSubviewsForView:(UIView*)view
{
    NSMutableArray *allSubviews = [NSMutableArray new];
    for (UIView *subview in view.subviews)
    {
        [allSubviews addObject:subview];
        [allSubviews addObjectsFromArray:[self getAllSubviewsForView:subview]];
    }
    return allSubviews;
}

/*
 The YellowBox is added to each RCTRootView. Regardless if there are warnings or not, if there's a warning anywhere in the app - it is added
 Since it is always appears on the top, it blocks interactions with other components.
 It is most noticeable in RCCLightBox and RCCNotification where button (for example) are not clickable if placed at the bottom part of the view
 */

+(BOOL)removeYellowBox:(RCTRootView*)reactRootView
{
#ifndef DEBUG
    return YES;
#endif
    
    BOOL removed = NO;
    
    NSArray* subviews = [self getAllSubviewsForView:reactRootView];
    for (UIView *view in subviews)
    {
        if ([view isKindOfClass:[RCTView class]])
        {
            CGFloat r, g, b, a;
            [view.backgroundColor getRed:&r green:&g blue:&b alpha:&a];
            
            //identify the yellow view by its hard-coded color and height
            if((lrint(r * 255) == 250) && (lrint(g * 255) == 186) && (lrint(b * 255) == 48) && (lrint(a * 100) == 95) && (view.frame.size.height == 46))
            {
                UIView *yelloboxParentView = view;
                while (view.superview != nil)
                {
                    yelloboxParentView = yelloboxParentView.superview;
                    if ([yelloboxParentView isKindOfClass:[RCTScrollView class]])
                    {
                        yelloboxParentView = yelloboxParentView.superview;
                        break;
                    }
                }
                
                [yelloboxParentView removeFromSuperview];
                removed = YES;
                break;
            }
        }
        
        if (removed)
        {
            break;
        }
    }
    
    return removed;
}

+ (NSArray *)textAttributeKeys
{
    return @[
       @"color",
       @"fontFamily",
       @"fontWeight",
       @"fontSize",
       @"fontStyle",
       @"shadowColor",
       @"shadowOffset",
       @"shadowBlurRadius",
       @"showShadow"
    ];
}

+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix baseFont:(UIFont *)baseFont
{
    NSMutableDictionary *textAttributes = [NSMutableDictionary new];
    
    NSString *colorKey = @"color";
    NSString *familyKey = @"fontFamily";
    NSString *weightKey = @"fontWeight";
    NSString *sizeKey = @"fontSize";
    NSString *styleKey = @"fontStyle";
    NSString *shadowColourKey = @"shadowColor";
    NSString *shadowOffsetKey = @"shadowOffset";
    NSString *shadowBlurRadiusKey = @"shadowBlurRadius";
    NSString *showShadowKey = @"showShadow";
    
    if (prefix) {
        
        colorKey = [colorKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[colorKey substringToIndex:1].capitalizedString];
        colorKey = [NSString stringWithFormat:@"%@%@", prefix, colorKey];
        
        familyKey = [familyKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[familyKey substringToIndex:1].capitalizedString];
        familyKey = [NSString stringWithFormat:@"%@%@", prefix, familyKey];
        
        weightKey = [weightKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[weightKey substringToIndex:1].capitalizedString];
        weightKey = [NSString stringWithFormat:@"%@%@", prefix, weightKey];
        
        sizeKey = [sizeKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[sizeKey substringToIndex:1].capitalizedString];
        sizeKey = [NSString stringWithFormat:@"%@%@", prefix, sizeKey];
        
        styleKey = [styleKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[styleKey substringToIndex:1].capitalizedString];
        styleKey = [NSString stringWithFormat:@"%@%@", prefix, styleKey];
        
        shadowColourKey = [shadowColourKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[shadowColourKey substringToIndex:1].capitalizedString];
        shadowColourKey = [NSString stringWithFormat:@"%@%@", prefix, shadowColourKey];
        
        shadowOffsetKey = [shadowOffsetKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[shadowOffsetKey substringToIndex:1].capitalizedString];
        shadowOffsetKey = [NSString stringWithFormat:@"%@%@", prefix, shadowOffsetKey];
        
        shadowBlurRadiusKey = [shadowBlurRadiusKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[shadowBlurRadiusKey substringToIndex:1].capitalizedString];
        shadowBlurRadiusKey = [NSString stringWithFormat:@"%@%@", prefix, shadowBlurRadiusKey];
        
        showShadowKey = [showShadowKey stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[showShadowKey substringToIndex:1].capitalizedString];
        showShadowKey = [NSString stringWithFormat:@"%@%@", prefix, showShadowKey];
    }
    
    NSShadow *shadow;
    
    NSNumber *shadowColor = dictionary[shadowColourKey];
    if (shadowColor && [shadowColor isKindOfClass:[NSNumber class]]) {
        if (!shadow) {
            shadow = [NSShadow new];
        }
        shadow.shadowColor = [RCTConvert UIColor:shadowColor];
    }
    
    NSDictionary *shadowOffsetDict = dictionary[shadowOffsetKey];
    if (shadowOffsetDict && [shadowOffsetDict isKindOfClass:[NSDictionary class]]) {
        CGSize shadowOffset = [RCTConvert CGSize:shadowOffsetDict];
        if (!shadow) {
            shadow = [NSShadow new];
        }
        shadow.shadowOffset = shadowOffset;
    }
    
    NSNumber *shadowRadius = dictionary[shadowBlurRadiusKey];
    if (shadowRadius) {
        CGFloat radius = [RCTConvert CGFloat:shadowRadius];
        if (!shadow) {
            shadow = [NSShadow new];
        }
        shadow.shadowBlurRadius = radius;
    }
    
    NSNumber *showShadow = dictionary[showShadowKey];
    if (showShadow) {
        BOOL show = [RCTConvert BOOL:showShadow];
        if (!show) {
            shadow = nil;
        }
    }
    
    if (shadow) {
        [textAttributes setObject:shadow forKey:NSShadowAttributeName];
    }
    
    NSNumber *textColor = dictionary[colorKey];
    if (textColor && [textColor isKindOfClass:[NSNumber class]])
    {
        UIColor *color = [RCTConvert UIColor:textColor];
        [textAttributes setObject:color forKey:NSForegroundColorAttributeName];
    }
    
    NSString *fontFamily = dictionary[familyKey];
    if (![fontFamily isKindOfClass:[NSString class]]) {
        fontFamily = nil;
    }
    
    NSString *fontWeight = dictionary[weightKey];
    if (![fontWeight isKindOfClass:[NSString class]]) {
        fontWeight = nil;
    }
    
    NSNumber *fontSize = dictionary[sizeKey];
    if (![fontSize isKindOfClass:[NSNumber class]]) {
        fontSize = nil;
    }
    
    NSNumber *fontStyle = dictionary[styleKey];
    if (![fontStyle isKindOfClass:[NSString class]]) {
        fontStyle = nil;
    }
    
    UIFont *font = [RCTFont updateFont:baseFont withFamily:fontFamily size:fontSize weight:fontWeight style:fontStyle variant:nil scaleMultiplier:1];
    
    if (font && (fontStyle || fontWeight || fontSize || fontFamily)) {
        [textAttributes setObject:font forKey:NSFontAttributeName];
    }
    
    return textAttributes;
}

+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix
{
    return [self textAttributesFromDictionary:dictionary withPrefix:prefix baseFont:[UIFont systemFontOfSize:[UIFont systemFontSize]]];
}

+ (void)styleNavigationItem:(UIBarButtonItem *)barButtonItem inViewController:(UIViewController *)viewController side:(NSString *)side
{
    if ([viewController isKindOfClass:[RCCViewController class]]) {
        
        RCCViewController *rccViewController = (RCCViewController *)viewController;
        NSDictionary *navigatorStyle = rccViewController.navigatorStyle;
        
        NSString *capitalSide = [side capitalizedString];
        
        BOOL needsButton = !!navigatorStyle[@"navBarButtonBorderRadius"] || !!navigatorStyle[@"navBarButtonBorderWidth"] || !!navigatorStyle[@"navBarButtonBorderColor"] || !!navigatorStyle[[NSString stringWithFormat:@"navBarButton%@BorderRadius", capitalSide]] || !!navigatorStyle[[NSString stringWithFormat:@"navBarButton%@BorderWidth", capitalSide]] || !!navigatorStyle[[NSString stringWithFormat:@"navBarButton%@BorderColor", capitalSide]] || !!navigatorStyle[@"navBarButtonPadding"] || !!navigatorStyle[[NSString stringWithFormat:@"navBarButton%@Padding", capitalSide]];
        
        UIButton *button;
        
        if (needsButton && !barButtonItem.customView) {
            
            button = [UIButton buttonWithType:UIButtonTypeCustom];
            if (barButtonItem.image) {
                [button setImage:[barButtonItem.image imageWithRenderingMode:barButtonItem.image.renderingMode] forState:UIControlStateNormal];
            }
            if (barButtonItem.title) {
                [button setTitle:barButtonItem.title forState:UIControlStateNormal];
            }
            
            [button addTarget:barButtonItem.target action:barButtonItem.action forControlEvents:UIControlEventTouchUpInside];
            
            NSString *callbackId = objc_getAssociatedObject(barButtonItem, &NAVIGATION_ITEM_CALLBACK_ID_ASSOCIATED_KEY);
            NSString *buttonId = objc_getAssociatedObject(barButtonItem, &NAVIGATION_ITEM_BUTTON_ID_ASSOCIATED_KEY);
            
            objc_setAssociatedObject(button, &NAVIGATION_ITEM_BUTTON_ID_ASSOCIATED_KEY, buttonId, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
            objc_setAssociatedObject(button, &NAVIGATION_ITEM_CALLBACK_ID_ASSOCIATED_KEY, callbackId, OBJC_ASSOCIATION_RETAIN_NONATOMIC);

        } else if (barButtonItem.customView && [barButtonItem.customView isKindOfClass:[UIButton class]]) {
            button = (UIButton *)barButtonItem.customView;
        }
        
        NSMutableDictionary *navButtonTextAttributes = [RCTHelpers textAttributesFromDictionary:rccViewController.navigatorStyle withPrefix:@"navBarButton"];
        NSMutableDictionary *navButtonSideTextAttributes = [RCTHelpers textAttributesFromDictionary:rccViewController.navigatorStyle withPrefix:[NSString stringWithFormat:@"navBarButton%@", capitalSide]];
        if (navButtonSideTextAttributes.allKeys.count > 0) {
            [navButtonTextAttributes setValuesForKeysWithDictionary:navButtonSideTextAttributes];
        }
        
        if (button) {
        
            button.tintColor = [viewController.navigationController.navigationBar tintColor];
            
            id borderRadius = navigatorStyle[[NSString stringWithFormat:@"navBarButton%@BorderRadius", capitalSide]] ? : navigatorStyle[@"navBarButtonBorderRadius"];
            if (borderRadius) {
                button.layer.cornerRadius = [RCTConvert CGFloat:borderRadius];
            }
            
            id borderWidth = navigatorStyle[[NSString stringWithFormat:@"navBarButton%@BorderWidth", capitalSide]] ? : navigatorStyle[@"navBarButtonBorderWidth"];
            if (borderWidth) {
                button.layer.borderWidth = [RCTConvert CGFloat:borderWidth];
            }
            
            id borderColor = navigatorStyle[[NSString stringWithFormat:@"navBarButton%@BorderColor", capitalSide]] ? : navigatorStyle[@"navBarButtonBorderColor"];
            if (borderColor) {
                button.layer.borderColor = [RCTConvert CGColor:borderColor];
            }
            
            id padding = navigatorStyle[[NSString stringWithFormat:@"navBarButton%@Padding", capitalSide]] ? : navigatorStyle[@"navBarButtonPadding"];
            if (padding) {
                button.contentEdgeInsets = [RCTConvert UIEdgeInsets:padding];
            }
            
            if (navButtonTextAttributes.allKeys.count > 0) {
                [button setAttributedTitle:[[NSAttributedString alloc] initWithString:[button titleForState:UIControlStateNormal] ? : @"" attributes:navButtonTextAttributes] forState:UIControlStateNormal];
            } else if ([[UIBarButtonItem appearance] titleTextAttributesForState:UIControlStateNormal]) {
                [button setAttributedTitle:[[NSAttributedString alloc] initWithString:[button titleForState:UIControlStateNormal] ? : @"" attributes:[[UIBarButtonItem appearance] titleTextAttributesForState:UIControlStateNormal]] forState:UIControlStateNormal];
            }
            
            if (!barButtonItem.customView) {
                barButtonItem.customView = button;
            }
            
            [button sizeToFit];
            
        } else if (navButtonTextAttributes.allKeys.count > 0) {
            
            [barButtonItem setTitleTextAttributes:navButtonTextAttributes forState:UIControlStateNormal];
        }
    }
}

@end
