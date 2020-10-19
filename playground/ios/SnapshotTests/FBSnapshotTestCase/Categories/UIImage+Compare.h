//
//  Created by Gabriel Handford on 3/1/09.
//  Copyright 2009-2013. All rights reserved.
//  Created by John Boiles on 10/20/11.
//  Copyright (c) 2011. All rights reserved
//  Modified by Felix Schulze on 2/11/13.
//  Copyright 2013. All rights reserved.
//
//  Permission is hereby granted, free of charge, to any person
//  obtaining a copy of this software and associated documentation
//  files (the "Software"), to deal in the Software without
//  restriction, including without limitation the rights to use,
//  copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the
//  Software is furnished to do so, subject to the following
//  conditions:
//
//  The above copyright notice and this permission notice shall be
//  included in all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//  OTHER DEALINGS IN THE SOFTWARE.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImage (Compare)

/**
 Compares the image against another given image.

 @param image The other image to compare against.
 @param perPixelTolerance How much (in percentage) any given pixel's colors are allowed to change from the pixel in the reference image.
 @param overallTolerance The overall percentage of pixels that are allowed to change from the pixels in the reference image.
 @return A BOOL which represents if the image is the same or not.
 */
- (BOOL)fb_compareWithImage:(UIImage *)image perPixelTolerance:(CGFloat)perPixelTolerance overallTolerance:(CGFloat)overallTolerance;

@end

NS_ASSUME_NONNULL_END
