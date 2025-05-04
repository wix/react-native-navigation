#import <Foundation/Foundation.h>

@class RNNReactView;

@interface RNNElementFinder : NSObject

+ (UIView *)findElementForId:(NSString *)elementId inView:(UIView *)view;

@end
