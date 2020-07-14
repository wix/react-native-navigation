//
//  Color+Interpolation.m
//
//  Created by Leo Natan on 01/10/2016.
//  Copyright © 2016 Leo Natan. All rights reserved.
//

#if __has_include(<UIKit/UIKit.h>) || __has_include(<AppKit/AppKit.h>)

#import "Color+Interpolation.h"

#if __has_include(<UIKit/UIKit.h>)
#define Color UIColor
#else
#define Color NSColor
#endif

#define SWAP(x, y) do { __typeof(x) __ZZZZ__SWAP = x; x = y; y = __ZZZZ__SWAP; } while(0)

//Same value as LNInterpolationBehaviorUseDefault
LNInterpolationBehavior const RNNInterpolationBehaviorUseLABColorSpace = @"LNInterpolationBehaviorUseDefault";
LNInterpolationBehavior const RNNInterpolationBehaviorUseRGBColorSpace = @"LNInterpolationBehaviorUseRGB";

extern double LNLinearInterpolate(double from, double to, double p);

static NSArray<NSNumber*>* LNRGBComponentsFromColor(Color* color)
{
	size_t numberOfComponents = CGColorGetNumberOfComponents(color.CGColor);
	const CGFloat* components = CGColorGetComponents(color.CGColor);
	
	return numberOfComponents == 2 ? @[@(components[0]), @(components[0]), @(components[0]), @(components[1])] : @[@(components[0]), @(components[1]), @(components[2]), @(components[3])];
}

static Color* LNColorFromRGBComponents(NSArray<NSNumber*>* components)
{
	return [Color colorWithRed:components[0].doubleValue green:components[1].doubleValue blue:components[2].doubleValue alpha:components[3].doubleValue];
}

static NSArray<NSNumber*>* LNLabComponentsFromColor(Color* color)
{
	NSArray<NSNumber*>* rgbComponents = LNRGBComponentsFromColor(color);
	CGFloat r = rgbComponents[0].doubleValue;
	CGFloat g = rgbComponents[1].doubleValue;
	CGFloat b = rgbComponents[2].doubleValue;
	
	//RGB -> XYZ
	//http://www.brucelindbloom.com/index.html?Eqn_RGB_to_XYZ.html

	r = (r > 0.04045) ? pow((r + 0.055) / 1.055, 2.4) : (r / 12.92);
	g = (g > 0.04045) ? pow((g + 0.055) / 1.055, 2.4) : (g / 12.92);
	b = (b > 0.04045) ? pow((b + 0.055) / 1.055, 2.4) : (b / 12.92);
	
	//http://www.brucelindbloom.com/index.html?Eqn_RGB_XYZ_Matrix.html (sRGB -> XYZ)
	CGFloat x = r * 41.24564 + g * 35.75761 + b * 18.04375;
	CGFloat y = r * 21.26729 + g * 71.51522 + b * 07.21750;
	CGFloat z = r * 01.93339 + g * 11.91920 + b * 95.03041;
	
	//XYZ -> Lab
	//http://www.brucelindbloom.com/index.html?Eqn_XYZ_to_Lab.html
	
	static const CGFloat eps = 216.0 / 24389.0;
	static const CGFloat k = 24389.0 / 27.0;
	
	x = x / 100.0;//95.047;
	y = y / 100.0;//100.000;
	z = z / 100.0;//108.883;
	
	x = x > eps ? pow(x, 1.0 / 3.0) : (k * x + 16.0) / 116.0;
	y = y > eps ? pow(y, 1.0 / 3.0) : (k * y + 16.0) / 116.0;
	z = z > eps ? pow(z, 1.0 / 3.0) : (k * z + 16.0) / 116.0;
	
	CGFloat l = 116 * y - 16;
	CGFloat a = 500 * (x - y);
	        b = 200 * (y - z);
	
	return @[@(l), @(a), @(b), rgbComponents[3]];
}

static Color* LNColorFromLabComponents(NSArray<NSNumber*>* components)
{
	CGFloat l = components[0].doubleValue;
	CGFloat a = components[1].doubleValue;
	CGFloat b = components[2].doubleValue;
	
	//Lab -> XYZ
	//http://www.brucelindbloom.com/index.html?Eqn_Lab_to_XYZ.html
	
	static const CGFloat eps = 216.0 / 24389.0;
	static const CGFloat k = 24389.0 / 27.0;
	
	CGFloat y = (l + 16.0) / 116.0;
	CGFloat x = a / 500 + y;
	CGFloat z = y - b / 200;

	x = pow(x, 3.0) > eps ? pow(x, 3.0) : (116 * x - 16) / k;
	y = l > k * eps ? pow((l + 16) / 116, 3.0) : l / k;
	z = pow(z, 3.0) > eps ? pow(z, 3.0) : (116 * z - 16) / k;
	
	x = x * 1.0;//.95047;
	y = y * 1.0;//1.00000;
	z = z * 1.0;//1.08883;
	
	//XYZ -> RGB
	
	//http://www.brucelindbloom.com/index.html?Eqn_RGB_XYZ_Matrix.html (XYZ -> sRGB)
	CGFloat r = x *  3.2404542 + y * -1.5371385 + z * -0.4985314;
	CGFloat g = x * -0.9692660 + y *  1.8760108 + z *  0.0415560;
	        b = x *  0.0556434 + y * -0.2040259 + z *  1.0572252;
	
	r = r <= 0.0031308 ? 12.92 * r : 1.055 * pow(r, 1.0 / 2.4) - 0.055;
	g = g <= 0.0031308 ? 12.92 * g : 1.055 * pow(g, 1.0 / 2.4) - 0.055;
	b = b <= 0.0031308 ? 12.92 * b : 1.055 * pow(b, 1.0 / 2.4) - 0.055;
	
	// return Color
	return LNColorFromRGBComponents(@[@(r), @(g), @(b), components[3]]);
}

static inline __attribute__((__always_inline__)) Color* LNInterpolateColor(Color* fromValue, Color* toValue, CGFloat p, NSArray* (*compConverter)(Color*), Color* (*colorConverter)(NSArray*))
{
	NSArray<NSNumber*>* arrayC1 = compConverter(fromValue);
	NSArray<NSNumber*>* arrayC2 = compConverter(toValue);
	
	NSMutableArray<NSNumber*>* arrayOutput = [NSMutableArray new];
	[arrayC1 enumerateObjectsUsingBlock:^(NSNumber * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		arrayOutput[idx] = @(LNLinearInterpolate(obj.doubleValue, arrayC2[idx].doubleValue, p));
	}];
	
	return colorConverter(arrayOutput);
}

@implementation Color (Interpolation)

- (instancetype)interpolateToValue:(Color*)toValue progress:(double)p
{
	return [self interpolateToValue:toValue progress:p behavior:LNInterpolationBehaviorUseDefault];
}

- (instancetype)interpolateToValue:(id)toValue progress:(double)p behavior:(LNInterpolationBehavior)behavior
{
	NSParameterAssert([toValue isKindOfClass:[Color class]]);
	
	if(p <= 0)
	{
		return self;
	}
	
	if(p >= 1)
	{
		return toValue;
	}
	
	return LNInterpolateColor(self, toValue, p, behavior == RNNInterpolationBehaviorUseRGBColorSpace ? LNRGBComponentsFromColor : LNLabComponentsFromColor, behavior == RNNInterpolationBehaviorUseRGBColorSpace ? LNColorFromRGBComponents : LNColorFromLabComponents);
}

@end

#endif
