#import "ImageParser.h"
#import "NullImage.h"
#import <React/RCTConvert.h>

@implementation ImageParser

+ (Image *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] ? [[Image alloc] initWithValue:[RCTConvert UIImage:json[key]]] : [NullImage new];
}

@end
