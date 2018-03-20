#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef UIViewController * (^RNNExternalViewCreator)(void);

@interface ReactNativeNavigation : NSObject

+(void)bootstrap:(NSURL*)jsCodeLocation launchOptions:(NSDictionary *)launchOptions;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

@end
