#import <Foundation/Foundation.h>
#import "RNNButtonOptions.h"
#import "RNNIconDrawer.h"
#import "UIImage+utils.h"

@interface RNNBaseIconCreator : NSObject

- (instancetype)initWithIconDrawer:(RNNIconDrawer *)iconDrawer;

- (UIImage *)create:(RNNButtonOptions *)buttonOptions;

@property (nonatomic, retain) RNNIconDrawer* iconDrawer;

@end

@interface RNNBaseIconCreator (Private)

- (UIImage *)createIcon:(RNNButtonOptions *)buttonOptions
              tintColor:(UIColor *)tintColor
        backgroundColor:(UIColor *)backgroundColor;

- (CGSize)resolveIconSize:(RNNButtonOptions *)buttonOptions;

@end
