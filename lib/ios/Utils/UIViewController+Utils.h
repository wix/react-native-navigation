#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface UIViewController (Utils)

+ (instancetype)snapshotKeyWindow;
- (void)forEachChild:(void (^)(UIViewController *child))perform;
- (BOOL)isLastInStack;

@end
