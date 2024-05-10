#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface UIViewController (Utils)

- (void)forEachChild:(void (^)(UIViewController *child))perform;
- (BOOL)isLastInStack;

@end
