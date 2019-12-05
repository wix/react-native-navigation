#import "RNNReactView.h"
#import "RCTHelpers.h"
#import <React/RCTUIManager.h>

@implementation RNNReactView {
	BOOL _fillParent;
}

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	self = [super initWithBridge:bridge moduleName:moduleName initialProperties:initialProperties];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(contentDidAppear:) name:RCTContentDidAppearNotification object:nil];
	 _reactViewReadyBlock = reactViewReadyBlock;
	
	return self;
}

- (void)contentDidAppear:(NSNotification *)notification {
#ifdef DEBUG
	if ([((RNNReactView *)notification.object).moduleName isEqualToString:self.moduleName]) {
		[RCTHelpers removeYellowBox:self];
	}
#endif
	
	RNNReactView* appearedView = notification.object;
	
	 if (_reactViewReadyBlock && [appearedView.appProperties[@"componentId"] isEqual:self.componentId]) {
	 	_reactViewReadyBlock();
		 _reactViewReadyBlock = nil;
		 [[NSNotificationCenter defaultCenter] removeObserver:self];
	 }
}

- (NSString *)componentId {
	return self.appProperties[@"componentId"];
}

- (void)setRootViewDidChangeIntrinsicSize:(void (^)(CGSize))rootViewDidChangeIntrinsicSize {
		_rootViewDidChangeIntrinsicSize = rootViewDidChangeIntrinsicSize;
		self.delegate = self;
}

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
	if (_rootViewDidChangeIntrinsicSize) {
		_rootViewDidChangeIntrinsicSize(rootView.intrinsicContentSize);
	}
}

- (CGSize)intrinsicContentSize {
	if (_fillParent) {
		return UILayoutFittingExpandedSize;
	} else {
		return [super intrinsicContentSize];
	}
}

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame {
	if ([alignment isEqualToString:@"fill"]) {
		_fillParent = YES;
		self.translatesAutoresizingMaskIntoConstraints = NO;
		self.sizeFlexibility = RCTRootViewSizeFlexibilityNone;
	} else {
		self.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
		__weak RNNReactView *weakSelf = self;
		[self setRootViewDidChangeIntrinsicSize:^(CGSize intrinsicSize) {
			[weakSelf setFrame:CGRectMake(0, 0, intrinsicSize.width, intrinsicSize.height)];
		}];
	}
}

@end
