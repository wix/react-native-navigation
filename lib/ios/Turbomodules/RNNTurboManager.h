#ifndef RNNTurboManager_h
#define RNNTurboManager_h

#import <Foundation/Foundation.h>

@class RCTHost;

typedef UIViewController * (^RNNExternalHostViewCreator)(NSDictionary *props, RCTHost *host);

@interface RNNTurboManager : NSObject

- (instancetype)initWithHost:(RCTHost *)host mainWindow:(UIWindow *)mainWindow;

- (UIViewController *)findComponentForId:(NSString *)componentId;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalHostViewCreator)callback;

@end

#endif
