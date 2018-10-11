#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>
#import "BoolParser.h"
#import "StringParser.h"
#import "NumberParser.h"
#import "DictionaryParser.h"

@class RNNOptions;

@protocol RNNOptionsDelegate <NSObject>

@optional
- (void)optionsDidUpdatedWithOptions:(RNNOptions *)otherOptions;

@end

@interface RNNOptions : NSObject

@property (nonatomic, weak) id<RNNOptionsDelegate> delegate;

- (instancetype)initWithDict:(NSDictionary*)dict;

- (void)mergeOptions:(RNNOptions *)otherOptions;
- (void)mergeOptions:(RNNOptions *)otherOptions overrideOptions:(BOOL)override;

@end
