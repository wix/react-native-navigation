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


#import "UIViewController+MMDrawerController.h"

@implementation UIViewController (MMDrawerController)


-(MMDrawerController*)mm_drawerController{
    UIViewController *parentViewController = self.parentViewController;
    while (parentViewController != nil) {
        if([parentViewController isKindOfClass:[MMDrawerController class]]){
            return (MMDrawerController *)parentViewController;
        }
        parentViewController = parentViewController.parentViewController;
    }
    return nil;
}

-(CGRect)mm_visibleDrawerFrame{
    if([self isEqual:self.mm_drawerController.leftDrawerViewController] ||
       [self.navigationController isEqual:self.mm_drawerController.leftDrawerViewController]){
        CGRect rect = self.mm_drawerController.view.bounds;
        rect.size.width = self.mm_drawerController.maximumLeftDrawerWidth;
        if (self.mm_drawerController.showsStatusBarBackgroundView) {
            rect.size.height -= 20.0f;
        }
        return rect;
        
    }
    else if([self isEqual:self.mm_drawerController.rightDrawerViewController] ||
             [self.navigationController isEqual:self.mm_drawerController.rightDrawerViewController]){
        CGRect rect = self.mm_drawerController.view.bounds;
        rect.size.width = self.mm_drawerController.maximumRightDrawerWidth;
        rect.origin.x = CGRectGetWidth(self.mm_drawerController.view.bounds)-rect.size.width;
        if (self.mm_drawerController.showsStatusBarBackgroundView) {
            rect.size.height -= 20.0f;
        }
        return rect;
    }
    else {
        return CGRectNull;
    }
}

@end
