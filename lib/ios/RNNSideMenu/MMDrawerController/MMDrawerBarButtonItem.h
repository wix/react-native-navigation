// Copyright (c) 2013 Mutual Mobile (http://mutualmobile.com/)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#import <UIKit/UIKit.h>

/**
  `MMDrawerBarButtonItem` provides convenience methods to create `UIBarButtonItems` with a default
  hamburger-menu asset.
 */

@interface MMDrawerBarButtonItem : UIBarButtonItem

///---------------------------------------
/// @name Initializing a `MMDrawerBarButtonItem`
///---------------------------------------

/**
 Creates and initializes an `MMDrawerBarButtonItem` without a border.

 @param target The target to forward the `action` to when the button is pressed.
 @param action The action to call when the button is pressed.

 @return The newly-initialized bar button item.
 */
- (instancetype)initWithTarget:(id)target action:(SEL)action;

/**
 Returns the current color of the menu button for the state requested. This property is deprecated
 in iOS 7.0. Use `tintColor` instead.

 @param state The UIControl state that the color is being requested for.

 @return The menu button color for the requested state.
 */
- (UIColor *)menuButtonColorForState:(UIControlState)state
    __attribute__((deprecated("Use tintColor instead")));

/**
 Sets the color of the menu button for the specified state. For this control, only set colors for
 `UIControlStateNormal` and `UIControlStateHighlighted`. This property is deprecated in iOS 7.0. Use
 `tintColor` instead.

 @param color The color to set.
 @param state The state to set the color for.
 */
- (void)setMenuButtonColor:(UIColor *)color
                  forState:(UIControlState)state
    __attribute__((deprecated("Use tintColor instead")));

/**
 Returns the current color of the shadow for the state requested. This property is deprecated in
 iOS 7.0. The menu button no longer supports a shadow.

 @param state The UIControl state that the color is being requested for.

 @return The menu button color for the requested state.
 */
- (UIColor *)shadowColorForState:(UIControlState)state
    __attribute__((deprecated("Shadow is no longer supported")));

/**
 Sets the color of the shadow for the specified state. For this control, only set colors for
 `UIControlStateNormal` and `UIControlStateHighlighted`. This property is deprecated in iOS 7.0. The
 menu button no longer supports a shadow.

 @param color The color to set.
 @param state The state to set the color for.
 */
- (void)setShadowColor:(UIColor *)color
              forState:(UIControlState)state
    __attribute__((deprecated("Shadow is no longer supported")));

@end
