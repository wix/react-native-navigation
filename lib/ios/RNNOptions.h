#import "RNNBoolParser.h"
#import "RNNColorParser.h"
#import "RNNDictionaryParser.h"
#import "RNNDoubleParser.h"
#import "RNNEnumParser.h"
#import "RNNImageParser.h"
#import "RNNIntNumberParser.h"
#import "RNNNumberParser.h"
#import "RNNTextParser.h"
#import "RNNTimeIntervalParser.h"
#import <React/RCTConvert.h>
#import <UIKit/UIKit.h>

@interface RNNOptions : NSObject

- (instancetype)initWithDict:(NSDictionary *)dict;

- (void)mergeOptions:(RNNOptions *)otherOptions;

@end
