#import <Foundation/Foundation.h>
#import "RNNElementView.h"

@interface RNNElementFinder : NSObject

- (instancetype)initWithToVC:(UIViewController *)toVC andfromVC:(UIViewController *)fromVC;
- (instancetype)initWithFromVC:(UIViewController *)fromVC;

- (RNNElementView *)findElementForId:(NSString *)elementId;

@end
