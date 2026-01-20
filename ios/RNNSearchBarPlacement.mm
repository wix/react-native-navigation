#import "RNNSearchBarPlacement.h"
#import <React/RCTConvert.h>

@implementation RNNSearchBarPlacement

- (SearchBarPlacement)convertString:(NSString *)string {
    return [self.class SearchBarPlacement:string];
}

RCT_ENUM_CONVERTER(SearchBarPlacement, (@{
                       @"stacked" : @(SearchBarPlacementStacked),
                       @"integrated" : @(SearchBarPlacementIntegrated)
                   }),
                   SearchBarPlacementStacked, integerValue)

@end

