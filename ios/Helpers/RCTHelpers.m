//
//  RCTHelpers.m
//  ReactNativeControllers
//
//  Created by Artal Druk on 25/05/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import "RCTHelpers.h"
#import "RCTView.h"
#import "RCTScrollView.h"
#import "RCTFont.h"

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

+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix baseFontSize:(CGFloat)size
{
    NSMutableDictionary *textAttributes = [NSMutableDictionary new];
    
    NSString *colorKey = @"color";
    NSString *familyKey = @"fontFamily";
    NSString *weightKey = @"fontWeight";
    NSString *sizeKey = @"fontSize";
    NSString *styleKey = @"fontStyle";
    
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
    
    UIFont *font = [RCTFont updateFont:[UIFont systemFontOfSize:size] withFamily:fontFamily size:fontSize weight:fontWeight style:fontStyle variant:nil scaleMultiplier:1];
    
    if (font && (fontStyle || fontWeight || fontSize || fontFamily)) {
        [textAttributes setObject:font forKey:NSFontAttributeName];
    }
    
    return textAttributes;
}

+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix
{
    return [self textAttributesFromDictionary:dictionary withPrefix:prefix baseFontSize:[UIFont systemFontSize]];
}


@end
