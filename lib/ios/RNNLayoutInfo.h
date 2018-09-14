#import <Foundation/Foundation.h>
#import "RNNOptionsManager.h"
#import "RNNLayoutNode.h"

@interface RNNLayoutInfo : NSObject

- (instancetype)initWithNode:(RNNLayoutNode *)node optionsManager:(RNNOptionsManager *)optionsManager;

@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, strong) NSString* name;
@property (nonatomic, strong) NSDictionary* props;
@property (nonatomic, strong) RNNNavigationOptions* options;

@end
