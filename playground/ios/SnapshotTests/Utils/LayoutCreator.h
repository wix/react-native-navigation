#import <Foundation/Foundation.h>

@interface LayoutCreator : NSObject

+ (NSDictionary *)component:(NSString *)componentId options:(NSDictionary *)options;

+ (NSDictionary *)parentWithID:(NSString *)componentId
                          type:(NSString *)type
                       options:(NSDictionary *)options
                      children:(NSArray<NSDictionary *> *)children;

+ (NSDictionary *)stack:(NSDictionary *)options children:(NSArray<NSDictionary *> *)children;

@end
