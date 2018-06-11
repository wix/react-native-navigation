@protocol RNNBridgeManagerDelegate <NSObject>

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge;

@end
