#import "BoolParser.h"
#import "ColorParser.h"
#import "DictionaryParser.h"
#import "DoubleParser.h"
#import "EnumParser.h"
#import "ImageParser.h"
#import "IntNumberParser.h"
#import "NumberParser.h"
#import "TextParser.h"
#import "TimeIntervalParser.h"
#import <React/RCTConvert.h>
#import <UIKit/UIKit.h>

@interface RNNOptions : NSObject

- (instancetype)initWithDict:(NSDictionary *)dict;

- (RNNOptions *)overrideOptions:(RNNOptions *)otherOptions;
- (RNNOptions *)mergeOptions:(RNNOptions *)otherOptions;
- (RNNOptions *)mergeInOptions:(RNNOptions *)otherOptions;

- (RNNOptions *)withDefault:(RNNOptions *)defaultOptions;

@end
