#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>
#import "BoolParser.h"
#import "TextParser.h"
#import "NumberParser.h"
#import "DictionaryParser.h"
#import "ColorParser.h"
#import "ImageParser.h"
#import "IntNumberParser.h"
#import "DoubleParser.h"
#import "EnumParser.h"
#import "TimeIntervalParser.h"

@interface RNNOptions : NSObject

- (instancetype)initWithDict:(NSDictionary*)dict;

- (RNNOptions *)overrideOptions:(RNNOptions *)otherOptions;
- (RNNOptions *)mergeOptions:(RNNOptions *)otherOptions;
- (RNNOptions *)mergeInOptions:(RNNOptions *)otherOptions;

- (RNNOptions *)withDefault:(RNNOptions *)defaultOptions;

@end
