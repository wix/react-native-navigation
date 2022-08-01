import { LayoutNode } from '../../src/commands/LayoutTreeCrawler';
import layoutActions from '../actions/layoutActions';
import optionActions from '../actions/optionActions';

export class NativeCommandsSender {
  constructor() {}

  setRoot(commandId: string, layout: { root: any; modals: any[]; overlays: any[] }) {
    return layoutActions.setRoot(commandId, layout);
  }

  setDefaultOptions(options: object) {
    return optionActions.setDefaultOptions(options);
  }

  mergeOptions(componentId: string, options: object) {
    return optionActions.mergeOptions(componentId, options);
  }

  push(commandId: string, onComponentId: string, layout: LayoutNode) {
    return layoutActions.push(commandId, onComponentId, layout);
  }

  pop(commandId: string, componentId: string, options?: object) {
    return layoutActions.pop(commandId, componentId, options);
  }

  popTo(commandId: string, componentId: string, options?: object) {
    return layoutActions.popTo(commandId, componentId, options);
  }

  popToRoot(commandId: string, componentId: string, options?: object) {
    return layoutActions.popToRoot(commandId, componentId, options);
  }

  setStackRoot(commandId: string, onComponentId: string, layout: object) {
    return layoutActions.setStackRoot(commandId, onComponentId, layout);
  }

  showModal(commandId: string, layout: object) {
    return layoutActions.showModal(commandId, layout);
  }

  dismissModal(commandId: string, componentId: string, options?: object) {
    return layoutActions.dismissModal(commandId, componentId, options);
  }

  dismissAllModals(commandId: string, options?: object) {
    return layoutActions.dismissAllModals(commandId, options);
  }

  showOverlay(commandId: string, layout: object) {
    return layoutActions.showOverlay(commandId, layout);
  }

  dismissOverlay(commandId: string, componentId: string) {
    return layoutActions.dismissOverlay(commandId, componentId);
  }

  dismissAllOverlays(commandId: string) {
    return layoutActions.dismissAllOverlays(commandId);
  }

  getLaunchArgs(_commandId: string) {}
}
