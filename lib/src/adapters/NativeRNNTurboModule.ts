import { TurboModule, TurboModuleRegistry, NativeModules } from 'react-native';
import { UnsafeObject, Double } from 'react-native/Libraries/Types/CodegenTypes';

export interface Spec extends TurboModule {
  readonly getConstants: () => {
    topBarHeight: Double;
    statusBarHeight: Double;
    bottomTabsHeight: Double;
  };

  setRoot(commandId: string, layout: UnsafeObject): Promise<string>;
  setDefaultOptions(options: UnsafeObject): void;
  mergeOptions(componentId: string, options: UnsafeObject): void;
  push(commandId: string, componentId: string, layout: UnsafeObject): Promise<string>;
  pop(commandId: string, componentId: string, options: UnsafeObject): Promise<string>;
  popTo(commandId: string, componentId: string, options: UnsafeObject): Promise<string>;
  popToRoot(commandId: string, componentId: string, options: UnsafeObject): Promise<string>;
  setStackRoot(
    commandId: string,
    componentId: string,
    layout: Array<UnsafeObject>
  ): Promise<string>;
  showModal(commandId: string, layout: UnsafeObject): Promise<string>;
  dismissModal(commandId: string, componentId: string, options: UnsafeObject): Promise<string>;
  dismissAllModals(commandId: string, options: UnsafeObject): Promise<void>;
  showOverlay(commandId: string, layout: UnsafeObject): Promise<string>;
  dismissOverlay(commandId: string, componentId: string): Promise<void>;
  dismissAllOverlays(commandId: string): Promise<void>;
  getLaunchArgs(commandId: string): Promise<Array<string>>;
}

const isTurboModuleEnabled = (globalThis as any).__turboModuleProxy != null;

const commands = isTurboModuleEnabled
  ? TurboModuleRegistry.get<Spec>('RNNTurboModule')
  : NativeModules.RNNBridgeModule;

const isNewArchWithBridgeless = isTurboModuleEnabled;

export const RCTAssertNewArchEnabled = () => {
  if (!isNewArchWithBridgeless)
    throw new Error('Allowed only in New Architecture with Bridgeless!');
};

export const isRNNTurboModuleAvailable = isNewArchWithBridgeless;
export default commands;
