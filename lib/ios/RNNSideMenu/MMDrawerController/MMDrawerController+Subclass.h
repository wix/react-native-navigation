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

#import "MMDrawerController.h"

/**
 This class extension is designed for use by subclasses of `MMDrawerController` to customize the functionality to support a specific use-case by a developer. When importing this file, there is no need to also call `#import MMDrawerController.h`.
 
 None of these methods are meant to be called by non-subclasses of `MMDrawerController`.
 */

@interface MMDrawerController (Subclass)
///---------------------------------------
/// @name Gesture Interaction
///---------------------------------------
/** 
 `MMDrawerController`'s single-tap gesture recognizer callback. This method is called every time the `UITapGestureRecognizer` is triggered.
 
 @param tapGesture The single-tap gesture recognizer instance that triggered the callback
 */
-(void)tapGestureCallback:(UITapGestureRecognizer *)tapGesture __attribute((objc_requires_super));

/** 
 `MMDrawerController`'s pan gesture recognizer callback. This method is called every time the `UIPanGestureRecognizer` is updated.
 
 @warning This method do the minimal amount of work to keep the pan gesture responsive.
 
 @param panGesture The pan gesture recognizer instance that triggered the callback
 */
-(void)panGestureCallback:(UIPanGestureRecognizer *)panGesture __attribute((objc_requires_super));

/**
 A `UIGestureRecognizerDelegate` method that is queried by `MMDrawerController`'s gestures to determine if it should receive the touch.
 
 @param gestureRecognizer The gesture recognizer that is asking if it should recieve a touch
 @param touch The touch in question in gestureRecognizer.view's coordinate space
 */
-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch __attribute((objc_requires_super));

///---------------------------------------
/// @name Drawer Presentation
///---------------------------------------
/** 
 Sets the initial conditions for `MMDrawerController` and its child view controllers to prepare the drawer for a transition. If a drawer is open and the opposite drawer is being presented, it prepares that drawer to be hidden and vice-versa for the closing drawer.
 
 @param drawer The drawer side that will be presented
 @param animated A boolean that indicates whether the presentation is being animated or not
 */
-(void)prepareToPresentDrawer:(MMDrawerSide)drawer animated:(BOOL)animated __attribute((objc_requires_super));

///---------------------------------------
/// @name Opening/Closing Drawer
///---------------------------------------
/**
 The method that handles closing the drawer. You can subclass this method to get a callback every time the drawer is about to be closed. You can inspect the current open side to determine what side is about to be closed.
 
 @param animated A boolean that indicates whether the drawer should close with animation
 @param velocity A float indicating how fast the drawer should close
 @param animationOptions A mask defining the animation options of the animation
 @param completion A completion block to be called when the drawer is finished closing
 */
-(void)closeDrawerAnimated:(BOOL)animated velocity:(CGFloat)velocity animationOptions:(UIViewAnimationOptions)options completion:(void (^)(BOOL))completion __attribute((objc_requires_super));

/**
 The method that handles opening the drawer. You can subclass this method to get a callback every time the drawer is about to be opened.
 
 @param drawerSide The drawer side that will be opened
 @param animated A boolean that indicates whether the drawer should open with animation
 @param velocity A float indicating how fast the drawer should open
 @param animationOptions A mask defining the animation options of the animation
 @param completion A completion block to be called when the drawer is finished opening
 */
-(void)openDrawerSide:(MMDrawerSide)drawerSide animated:(BOOL)animated velocity:(CGFloat)velocity animationOptions:(UIViewAnimationOptions)options completion:(void (^)(BOOL))completion __attribute((objc_requires_super));

///---------------------------------------
/// @name `UIViewController` Subclass Methods
///---------------------------------------
/**
 Included here to ensure subclasses call `super`.
 
 @param toInterfaceOrientation The interface orientation that the interface is moving to
 @param duration The duration of the interface orientation animation
 */
-(void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration __attribute((objc_requires_super));

/** 
 Included here to ensure subclasses call `super`.
 
 @param toInterfaceOrientation The interface orientation that the interface is moving to
 @param duration The duration of the interface orientation animation
 */
-(void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration __attribute((objc_requires_super));

@end
