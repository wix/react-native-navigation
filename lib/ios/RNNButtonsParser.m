#import "RNNButtonsParser.h"

@implementation RNNButtonsParser

+ (NSArray<RNNButtonOptions *> *)parse:(NSDictionary *)buttons {
    if (!buttons)
        return nil;
    if ([buttons isKindOfClass:[NSArray class]]) {
        NSMutableArray *buttonsArray = NSMutableArray.new;
        for (NSDictionary *buttonDict in buttons) {
            [buttonsArray addObject:[self parseButton:buttonDict]];
        }
        return [NSArray arrayWithArray:buttonsArray];
    } else {
        return @[ [self parseButton:buttons] ];
    }
}

+ (RNNButtonOptions *)parseButton:(NSDictionary *)button {
    return [[RNNButtonOptions alloc] initWithDict:button];
}

@end
