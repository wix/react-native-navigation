#import <Foundation/Foundation.h>

#define kPush              @"push"
#define kPop               @"pop"
#define kPopTo             @"popTo"
#define kPopToRoot         @"popToRoot"
#define kShowModal         @"showModal"
#define kDismissModal      @"dismissModal"
#define kDismissAllModals  @"dismissAllModals"
#define kShowOverlay       @"showOverlay"
#define kDismissOverlay    @"dismissOverlay"
typedef NSString*               NavigationCommand;

@interface RNNNavigationEvent : NSObject

@property (nonatomic, strong) NSDictionary* body;

+ (instancetype)create:(NavigationCommand)commandType fromComponent:(NSString *)fromComponentId toComponent:(NSString *)toComponentId;

@end
