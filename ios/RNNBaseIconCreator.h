#import "RNNButtonOptions.h"
#import "RNNIconDrawer.h"
#import "UIImage+utils.h"
#import <Foundation/Foundation.h>

@interface RNNBaseIconCreator : NSObject

- (instancetype)initWithIconDrawer:(RNNIconDrawer *)iconDrawer;

- (UIImage *)create:(RNNButtonOptions *)buttonOptions;

@property(nonatomic, retain) RNNIconDrawer *iconDrawer;

@end

@interface RNNBaseIconCreator (Private)

- (UIImage *)createIcon:(RNNButtonOptions *)buttonOptions
              tintColor:(UIColor *)tintColor
        backgroundColor:(UIColor *)backgroundColor;

- (CGSize)resolveIconSize:(RNNButtonOptions *)buttonOptions;

@end
