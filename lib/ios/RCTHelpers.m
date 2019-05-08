#import "RCTHelpers.h"
#import <React/RCTView.h>
#import <React/RCTScrollView.h>
#import <React/RCTFont.h>

@implementation RCTHelpers

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
	
	NSString *fontStyle = dictionary[styleKey];
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

+ (NSString *)getTimestampString {
	long long milliseconds = (long long)([[NSDate date] timeIntervalSince1970] * 1000.0);
	return [NSString stringWithFormat:@"%lld", milliseconds];
}

+ (NSArray *)getAllSubviewsForView:(UIView *)view {
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

+ (BOOL)removeYellowBox:(RCTRootView *)reactRootView {
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

@end
