import { LayoutStore } from '../Stores/LayoutStore';
import LayoutNodeFactory from '../Layouts/LayoutNodeFactory';
import { LayoutNode } from '../../src/commands/LayoutTreeCrawler';
import { events } from '../Stores/EventsStore';
import _ from 'lodash';
import ComponentNode from '../Layouts/ComponentNode';
import { Constants } from '../../src/adapters/Constants';
import { CommandName } from '../../src/interfaces/CommandName';

export class NativeCommandsSender {
  constructor() {}

  setRoot(commandId: string, layout: { root: any; modals: any[]; overlays: any[] }) {
    return new Promise((resolve) => {
      if (LayoutStore.getVisibleLayout()) {
        LayoutStore.getVisibleLayout().componentDidDisappear();
        LayoutStore.setRoot({});
      }

      const layoutNode = LayoutNodeFactory.create(layout.root);
      LayoutStore.setRoot(layoutNode);
      layoutNode.getVisibleLayout().componentDidAppear();
      resolve(layout.root.nodeId);
      this.reportCommandCompletion(CommandName.SetRoot, commandId);
    });
  }

  setDefaultOptions(options: object) {
    LayoutStore.setDefaultOptions(options);
  }

  mergeOptions(componentId: string, options: object) {
    LayoutStore.mergeOptions(componentId, options);
  }

  push(commandId: string, onComponentId: string, layout: LayoutNode) {
    return new Promise((resolve) => {
      const stack = LayoutStore.getLayoutById(onComponentId).getStack();
      const layoutNode = LayoutNodeFactory.create(layout, stack);
      stack.getVisibleLayout().componentDidDisappear();
      LayoutStore.push(layoutNode, stack);
      LayoutStore.applyOptions(layoutNode.nodeId, layoutNode.data.options);
      stack.getVisibleLayout().componentDidAppear();
      resolve(stack.getVisibleLayout().nodeId);
      this.reportCommandCompletion(CommandName.Push, commandId);
    });
  }

  pop(commandId: string, componentId: string, _options?: object) {
    return new Promise((resolve, reject) => {
      if (!LayoutStore.getLayoutById(componentId)) {
        reject(`Popping component failed - componentId '${componentId}' not found`);
        return;
      }
      const poppedChild = _.last(
        LayoutStore.getLayoutById(componentId).getStack().children
      ) as ComponentNode;
      LayoutStore.pop(componentId);
      events.invokeScreenPopped({
        componentId,
      });
      resolve(poppedChild.nodeId);
      this.reportCommandCompletion(CommandName.Pop, commandId);
    });
  }

  popTo(commandId: string, componentId: string, _options?: object) {
    return new Promise((resolve) => {
      LayoutStore.popTo(componentId);
      resolve(componentId);
      this.reportCommandCompletion(CommandName.PopTo, commandId);
    });
  }

  popToRoot(commandId: string, componentId: string, _options?: object) {
    LayoutStore.popToRoot(componentId);
    this.reportCommandCompletion(CommandName.PopToRoot, commandId);
  }

  setStackRoot(commandId: string, onComponentId: string, layout: object) {
    LayoutStore.setStackRoot(onComponentId, layout);
    this.reportCommandCompletion(CommandName.SetStackRoot, commandId);
  }

  showModal(commandId: string, layout: object) {
    return new Promise((resolve) => {
      const layoutNode = LayoutNodeFactory.create(layout);
      LayoutStore.getVisibleLayout().componentDidDisappear();
      LayoutStore.showModal(layoutNode);
      layoutNode.componentDidAppear();
      resolve(layoutNode.nodeId);
      this.reportCommandCompletion(CommandName.ShowModal, commandId);
    });
  }

  dismissModal(commandId: string, componentId: string, _options?: object) {
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
        this.reportCommandCompletion(CommandName.DismissModal, commandId);
      } else {
        reject(`component with id: ${componentId} is not a modal`);
      }
    });
  }

  dismissAllModals(commandId: string, _options?: object) {
    LayoutStore.dismissAllModals();
    this.reportCommandCompletion(CommandName.DismissAllModals, commandId);
  }

  showOverlay(commandId: string, layout: object) {
    const layoutNode = LayoutNodeFactory.create(layout);
    LayoutStore.showOverlay(layoutNode);
    layoutNode.componentDidAppear();
    this.reportCommandCompletion(CommandName.ShowOverlay, commandId);
  }

  dismissOverlay(commandId: string, componentId: string) {
    LayoutStore.dismissOverlay(componentId);
    this.reportCommandCompletion(CommandName.DismissOverlay, commandId);
  }

  dismissAllOverlays(commandId: string) {
    LayoutStore.dismissAllOverlays();
    this.reportCommandCompletion(CommandName.DismissAllOverlays, commandId);
  }

  getLaunchArgs(commandId: string) {
    this.reportCommandCompletion(CommandName.GetLaunchArgs, commandId);
  }

  getNavigationConstants(): Promise<Constants> {
    return Promise.resolve({
      topBarHeight: 0,
      backButtonId: 'RNN.back',
      bottomTabsHeight: 0,
      statusBarHeight: 0,
    });
  }

  getNavigationConstantsSync(): Constants {
    return {
      topBarHeight: 0,
      backButtonId: 'RNN.back',
      bottomTabsHeight: 0,
      statusBarHeight: 0,
    };
  }

  private reportCommandCompletion(commandName: string, commandId: string) {
    events.invokeCommandCompleted({
      commandName,
      commandId,
      completionTime: 0,
    });
  }
}
