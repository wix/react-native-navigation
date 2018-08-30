#import "RNNReactRootView.h"
#import "RCTHelpers.h"
#import <React/RCTUIManager.h>

@implementation RNNReactRootView {
	RCTBridge* _bridge;
};

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties {
	self = [super initWithBridge:bridge moduleName:moduleName initialProperties:initialProperties];
	_bridge = bridge;
	
#ifdef DEBUG
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(contentDidAppear:) name:RCTContentDidAppearNotification object:nil];
#endif
	
	return self;
}

- (void)contentDidAppear:(NSNotification *)notification {
	if ([((RNNReactRootView *)notification.object).moduleName isEqualToString:self.moduleName]) {
		[RCTHelpers removeYellowBox:self];
		[[NSNotificationCenter defaultCenter] removeObserver:self];
	}
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

- (void)setAlignment:(NSString *)alignment {
	if ([alignment isEqualToString:@"fill"]) {
		self.sizeFlexibility = RCTRootViewSizeFlexibilityNone;
	} else {
		self.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
	}
}

- (void)layoutSubviews {
	[super layoutSubviews];
	[_bridge.uiManager setSize:self.bounds.size forView:self];
}

@end
