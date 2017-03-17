#import "RCCAppStyler.h"

@interface RCCAppStyler ()

@property (nonatomic, strong, readwrite) NSDictionary *appStyle;

@end

@implementation RCCAppStyler

-(instancetype)initWithAppStyleProps:(NSDictionary*)appStyleProps {
    self = [super init];
    if (self) {
        self.appStyle = appStyleProps;
    }
    return self;
}


-(NSString*)styleForKey:(NSString*)key {
    if (key) {
        return self.appStyle[key];
    }
    return nil;
}

@end
