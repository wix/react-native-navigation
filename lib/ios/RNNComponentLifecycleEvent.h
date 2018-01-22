#import "RNNNavigationEvent.h"

#define kDidAppear         @"didAppear"
#define kDidDisappear      @"didDisappear"
#define kWillUnmount       @"willUnmount"
#define kDidMount         @"didMount"
typedef NSString*               LifecycleEvent;

@interface RNNComponentLifecycleEvent : NSObject

+ (instancetype)create:(LifecycleEvent)event componentName:(NSString*)componentName componentId:(NSString*)componentId;

- (NSDictionary*)body;

@end
