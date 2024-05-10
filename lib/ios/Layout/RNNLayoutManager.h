
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNLayoutManager : NSObject

- (void)addPendingViewController:(UIViewController *)vc;
- (void)removePendingViewController:(UIViewController *)vc;

- (UIViewController *)findComponentForId:(NSString *)componentId;

@end
