#import "RNNButtonOptions.h"
#import <Foundation/Foundation.h>

@interface RNNButtonsParser : NSObject

+ (NSArray<RNNButtonOptions *> *)parse:(NSDictionary *)buttonsDictionary;

@end
