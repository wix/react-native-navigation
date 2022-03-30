import { LayoutStore } from '../Stores/LayoutStore';
import LayoutNodeFactory from '../Layouts/LayoutNodeFactory';
import { LayoutNode } from '../../src/commands/LayoutTreeCrawler';
import { events } from '../Stores/EventsStore';
import _ from 'lodash';
import ComponentNode from '../Layouts/ComponentNode';

export class NativeCommandsSender {
  constructor() {}

  setRoot(_commandId: string, layout: { root: any; modals: any[]; overlays: any[] }) {
    return new Promise((resolve) => {
      if (LayoutStore.getVisibleLayout()) {
        LayoutStore.getVisibleLayout().componentDidDisappear();
        LayoutStore.setRoot({});
      }

      const layoutNode = LayoutNodeFactory.create(layout.root);
      LayoutStore.setRoot(layoutNode);
      layoutNode.getVisibleLayout().componentDidAppear();
      resolve(layout.root.nodeId);
    });
  }

  setDefaultOptions(options: object) {
    LayoutStore.setDefaultOptions(options);
  }

  mergeOptions(componentId: string, options: object) {
    LayoutStore.mergeOptions(componentId, options);
  }

  push(_commandId: string, onComponentId: string, layout: LayoutNode) {
    return new Promise((resolve) => {
      const stack = LayoutStore.getLayoutById(onComponentId).getStack();
      const layoutNode = LayoutNodeFactory.create(layout, stack);
      stack.getVisibleLayout().componentDidDisappear();
      LayoutStore.push(layoutNode, stack);
      stack.getVisibleLayout().componentDidAppear();
      resolve(stack.getVisibleLayout().nodeId);
    });
  }

  pop(_commandId: string, componentId: string, _options?: object) {
    return new Promise((resolve) => {
      const poppedChild = _.last(
        LayoutStore.getLayoutById(componentId).getStack().children
      ) as ComponentNode;
      LayoutStore.pop(componentId);
      resolve(poppedChild.nodeId);
    });
  }

  popTo(_commandId: string, componentId: string, _options?: object) {
    return new Promise((resolve) => {
      LayoutStore.popTo(componentId);
      resolve(componentId);
    });
  }

  popToRoot(_commandId: string, componentId: string, _options?: object) {
    LayoutStore.popToRoot(componentId);
  }

  setStackRoot(_commandId: string, onComponentId: string, layout: object) {
    LayoutStore.setStackRoot(onComponentId, layout);
  }

  showModal(_commandId: string, layout: object) {
    return new Promise((resolve) => {
      const layoutNode = LayoutNodeFactory.create(layout);
      LayoutStore.getVisibleLayout().componentDidDisappear();
      LayoutStore.showModal(layoutNode);
      layoutNode.componentDidAppear();
      resolve(layoutNode.nodeId);
    });
  }

  dismissModal(_commandId: string, componentId: string, _options?: object) {
    return new Promise((resolve, reject) => {
      const modal = LayoutStore.getModalById(componentId);
      if (modal) {
        const modalTopParent = modal.getTopParent();
        modalTopParent.componentDidDisappear();
        LayoutStore.dismissModal(componentId);
        events.invokeModalDismissed({
          componentName: modalTopParent.data.name,
          componentId: modalTopParent.nodeId,
          modalsDismissed: 1,
        });
        resolve(modalTopParent.nodeId);
        LayoutStore.getVisibleLayout().componentDidAppear();
      } else {
        reject(`component with id: ${componentId} is not a modal`);
      }
    });
  }

  dismissAllModals(_commandId: string, _options?: object) {
    LayoutStore.dismissAllModals();
  }

  showOverlay(_commandId: string, layout: object) {
    const layoutNode = LayoutNodeFactory.create(layout);
    LayoutStore.showOverlay(layoutNode);
    layoutNode.componentDidAppear();
  }

  dismissOverlay(_commandId: string, componentId: string) {
    LayoutStore.dismissOverlay(componentId);
  }

  dismissAllOverlays(_commandId: string) {
    LayoutStore.dismissAllOverlays();
  }

  getLaunchArgs(_commandId: string) {}
}
