//
//  LNInterpolable.c
//
//  Created by Leo Natan on 04/10/2016.
//  Copyright © 2016 Leo Natan. All rights reserved.
//

#import "LNInterpolable.h"

LNInterpolationBehavior const LNInterpolationBehaviorUseDefault = @"LNInterpolationBehaviorUseDefault";

double LNLinearInterpolate(double from, double to, double p)
{
	return from + p * (to - from);
}
