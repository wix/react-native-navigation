#import "RNNModalOptions.h"
#import "BoolParser.h"
#import "TextParser.h"

static UIViewController *RNNSheetPresentationHostForViewController(UIViewController *viewController) {
    UIViewController *current = viewController;
    while (current != nil) {
        if (current.sheetPresentationController != nil) {
            return current;
        }
        current = current.parentViewController;
    }
    return viewController;
}

static NSString *RNNSheetDetentIdentifierFromString(NSString *identifier) API_AVAILABLE(ios(15.0)) {
    NSString *normalized = [identifier lowercaseString];
    if (@available(iOS 16.0, *)) {
        if ([normalized isEqualToString:@"medium"]) {
            return UISheetPresentationControllerDetentIdentifierMedium;
        }
    }
    if ([normalized isEqualToString:@"large"]) {
        return UISheetPresentationControllerDetentIdentifierLarge;
    }
    return identifier;
}

@implementation RNNModalOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.swipeToDismiss = [BoolParser parse:dict key:@"swipeToDismiss"];
    self.detents = [RNNSheetDetentOptions parseDetents:dict[@"detents"]];
    self.selectedDetent = [TextParser parse:dict key:@"selectedDetent"];
    self.largestUndimmedDetent = [TextParser parse:dict key:@"largestUndimmedDetent"];
    self.prefersGrabberVisible = [BoolParser parse:dict key:@"prefersGrabberVisible"];
    return self;
}

- (void)mergeOptions:(RNNModalOptions *)options {
    if (options.swipeToDismiss.hasValue)
        self.swipeToDismiss = options.swipeToDismiss;
    if (options.detents)
        self.detents = options.detents;
    if (options.selectedDetent.hasValue)
        self.selectedDetent = options.selectedDetent;
    if (options.largestUndimmedDetent.hasValue)
        self.largestUndimmedDetent = options.largestUndimmedDetent;
    if (options.prefersGrabberVisible.hasValue)
        self.prefersGrabberVisible = options.prefersGrabberVisible;
}

- (BOOL)hasSheetPresentationOptions {
    return self.detents.count > 0 || self.selectedDetent.hasValue || self.largestUndimmedDetent.hasValue ||
           self.prefersGrabberVisible.hasValue;
}

- (void)applySheetPresentationToViewController:(UIViewController *)viewController {
    if (![self hasSheetPresentationOptions]) {
        return;
    }

    if (@available(iOS 15.0, *)) {
        UIViewController *host = RNNSheetPresentationHostForViewController(viewController);
        UISheetPresentationController *sheet = host.sheetPresentationController;
        if (sheet == nil) {
            return;
        }

        __weak RNNModalOptions *weakSelf = self;
        void (^applyChanges)(void) = ^{
          RNNModalOptions *strongSelf = weakSelf;
          if (strongSelf == nil) {
              return;
          }

          if (strongSelf.detents.count > 0) {
              NSArray<UISheetPresentationControllerDetent *> *resolvedDetents = [strongSelf resolveDetents];
              if (resolvedDetents.count > 0) {
                  sheet.detents = resolvedDetents;
              }
          }

          if (strongSelf.prefersGrabberVisible.hasValue) {
              sheet.prefersGrabberVisible = strongSelf.prefersGrabberVisible.get;
          }

          if (strongSelf.selectedDetent.hasValue) {
              NSString *detentId = RNNSheetDetentIdentifierFromString(strongSelf.selectedDetent.get);
              sheet.selectedDetentIdentifier = detentId;
          }

          if (strongSelf.largestUndimmedDetent.hasValue) {
              NSString *detentId =
                  RNNSheetDetentIdentifierFromString(strongSelf.largestUndimmedDetent.get);
              sheet.largestUndimmedDetentIdentifier = detentId;
          }
        };

        if (host.view.window != nil) {
            [sheet animateChanges:applyChanges];
        } else {
            applyChanges();
        }
    }
}

- (NSArray<UISheetPresentationControllerDetent *> *)resolveDetents API_AVAILABLE(ios(15.0)) {
    NSMutableArray<UISheetPresentationControllerDetent *> *resolved = [NSMutableArray new];

    for (RNNSheetDetentOptions *detent in self.detents) {
        UISheetPresentationControllerDetent *resolvedDetent = [self resolveDetent:detent];
        if (resolvedDetent != nil) {
            [resolved addObject:resolvedDetent];
        }
    }

    return resolved;
}

- (UISheetPresentationControllerDetent *)resolveDetent:(RNNSheetDetentOptions *)detent
    API_AVAILABLE(ios(15.0)) {
    if (detent.type == RNNSheetDetentTypeSystem) {
        NSString *identifier = [[detent.systemIdentifier get] lowercaseString];
        if (@available(iOS 16.0, *)) {
            if ([identifier isEqualToString:@"medium"]) {
                return [UISheetPresentationControllerDetent mediumDetent];
            }
            if ([identifier isEqualToString:@"large"]) {
                return [UISheetPresentationControllerDetent largeDetent];
            }
        } else if (@available(iOS 15.0, *)) {
            if ([identifier isEqualToString:@"large"]) {
                return [UISheetPresentationControllerDetent largeDetent];
            }
        }
        return nil;
    }

    if (@available(iOS 16.0, *)) {
        NSString *identifier = [detent.customIdentifier get];
        CGFloat height = [detent.height withDefault:0];
        if (identifier.length == 0 || height <= 0) {
            return nil;
        }

        return [UISheetPresentationControllerDetent customDetentWithIdentifier:identifier
                                                                    resolver:^CGFloat(
                                                                        id<UISheetPresentationControllerDetentResolutionContext>
                                                                            context) {
                                                                      return height;
                                                                    }];
    }

    return nil;
}

@end
