#import "NSObject+Utils.h"
#import "RNNOptions.h"
#import <objc/runtime.h>

@implementation NSObject (Utils)

- (NSArray *)classProperties {
    NSMutableArray *properties = [NSMutableArray new];
    unsigned int count;
    Class cls = [self class];
    do {
        objc_property_t *props = class_copyPropertyList(cls, &count);
        for (int i = 0; i < count; i++) {
            objc_property_t property = props[i];
            NSString *propertyName = [NSString stringWithCString:property_getName(property)
                                                        encoding:NSUTF8StringEncoding];
            [properties addObject:propertyName];
        }
        free(props);
        cls = [cls superclass];
    } while (![cls isEqual:[NSObject class]] && cls != nil);
    return properties;
}

@end
