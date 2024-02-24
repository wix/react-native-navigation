#import "RNNImageParser.h"
#import "RNNNullImage.h"
#import <React/RCTConvert.h>

@implementation RNNImageParser

+ (RNNImage *)parse:(NSDictionary *)json key:(NSString *)key {
    id data = json[key];
    if (!data) {
        return [RNNNullImage new];
    }

    UIImage *image;

    if ([data isKindOfClass:[NSDictionary class]] &&
        [data[@"system"] isKindOfClass:[NSString class]]) {
        if (@available(iOS 13.0, *)) {
            image = [UIImage systemImageNamed:data[@"system"]];
        }
        if (!image) {
            image = [RCTConvert UIImage:data[@"fallback"]];
        }
    } else {
        image = [RCTConvert UIImage:data];
    }

    return [[RNNImage alloc] initWithValue:image];
}

@end
