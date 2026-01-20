#import "Enum.h"

typedef NS_ENUM(NSInteger, SearchBarPlacement) {
    SearchBarPlacementStacked = 0,
    SearchBarPlacementIntegrated
};

@interface RNNSearchBarPlacement: Enum

- (SearchBarPlacement)get;

- (SearchBarPlacement)withDefault:(SearchBarPlacement)defaultValue;

@end
