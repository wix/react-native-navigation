#import "ImageParser.h"
#import "NullImage.h"
#import <React/RCTConvert.h>

@implementation ImageParser

+ (Image *)parse:(NSDictionary *)json key:(NSString *)key {
    id data = json[key];
    if (!data) {
        return [NullImage new];
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

    return [[Image alloc] initWithValue:image];
}

@end
