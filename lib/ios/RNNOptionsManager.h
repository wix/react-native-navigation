#import <Foundation/Foundation.h>
#import "RNNNavigationOptions.h"

@interface RNNOptionsManager : NSObject

- (RNNNavigationOptions *)createOptions:(NSDictionary *)optionsDict;

@property (nonatomic, strong) NSDictionary* defaultOptionsDict;

@end
