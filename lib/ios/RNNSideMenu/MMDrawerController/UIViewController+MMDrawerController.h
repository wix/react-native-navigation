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

#import "MMDrawerController.h"

/**
 A helper category on `UIViewController` that exposes the parent drawer controller, the visible side drawer frame, and a `mm_drawerWillAppear` method that is called when the drawer is about to appear.
 */

@interface UIViewController (MMDrawerController)

///---------------------------------------
/// @name Accessing Drawer View Controller Properties
///---------------------------------------

/**
 The `MMDrawerController` that the view controller is contained within. If the view controller is not contained within a `MMDrawerController`, this property is nil. Note that if the view controller is contained within a `UINavigationController`, that navigation controller is contained within a `MMDrawerController`, this property will return a refernce to the `MMDrawerController`, despite the fact that it is not the direct parent of the view controller.
 */
@property(nonatomic, strong, readonly) MMDrawerController *mm_drawerController;

/**
 The visible rect of the side drawer view controller in the drawer controller coordinate space. If the view controller is not a drawer in a `MMDrawerController`, then this property returns `CGRectNull`
 */
@property(nonatomic, assign, readonly) CGRect mm_visibleDrawerFrame;

@end
