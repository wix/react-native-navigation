export interface LifecycleEvent {
  type: LifecycleEventType;
  componentId: string;
  componentName: string;
}

export const enum LifecycleEventType {
  ComponentDidMount = 'ComponentDidMount',
  ComponentDidAppear = 'ComponentDidAppear',
  ComponentDidDisappear = 'ComponentDidDisappear',
  ComponentWillUnmount = 'ComponentWillUnmount'
}
