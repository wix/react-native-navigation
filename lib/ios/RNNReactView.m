#import "RNNReactView.h"
#import "RCTHelpers.h"
#import <React/RCTUIManager.h>

@implementation RNNReactView

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	self = [super initWithBridge:bridge moduleName:moduleName initialProperties:initialProperties];
	_reactViewReadyBlock = reactViewReadyBlock;
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(contentDidAppear:) name:RCTContentDidAppearNotification object:nil];
	
	return self;
}

- (void)contentDidAppear:(NSNotification *)notification {
#ifdef DEBUG
	if ([((RNNReactView *)notification.object).moduleName isEqualToString:self.moduleName]) {
		[RCTHelpers removeYellowBox:self];
	}
#endif
	
	if (_reactViewReadyBlock) {
		_reactViewReadyBlock();
	}
	
	[[NSNotificationCenter defaultCenter] removeObserver:self];
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

@end
