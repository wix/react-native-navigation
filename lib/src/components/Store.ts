import * as React from 'react';
import { ComponentProvider } from 'react-native';
import { IWrappedComponent } from './ComponentWrapper';

export class Store {
  private componentsByName: Record<string, ComponentProvider> = {};
  private propsById: Record<string, any> = {};
  private componentsInstancesById: Record<string, IWrappedComponent> = {};
  private wrappedComponents: Record<string, React.ComponentClass<any>> = {};
  private lazyRegistratorFn: ((lazyComponentRequest: string | number) => void) | undefined;

  updateProps(componentId: string, props: any) {
    this.propsById[componentId] = props;
    const component = this.componentsInstancesById[componentId];
    if (component) {
      this.componentsInstancesById[componentId].setProps(props);
    }
  }

  getPropsForId(componentId: string) {
    return this.propsById[componentId] || {};
  }

  clearComponent(componentId: string) {
    delete this.propsById[componentId];
    delete this.componentsInstancesById[componentId];
  }

  setComponentClassForName(componentName: string | number, ComponentClass: ComponentProvider) {
    this.componentsByName[componentName.toString()] = ComponentClass;
  }

  getComponentClassForName(componentName: string | number): ComponentProvider | undefined {
    this.ensureClassForName(componentName);
    return this.componentsByName[componentName.toString()];
  }

  ensureClassForName(componentName: string | number): void {
    if (!this.componentsByName[componentName.toString()] && this.lazyRegistratorFn) {
      this.lazyRegistratorFn(componentName);
    }
  }

  setComponentInstance(id: string, component: IWrappedComponent): void {
    this.componentsInstancesById[id] = component;
  }

  getComponentInstance(id: string): IWrappedComponent {
    return this.componentsInstancesById[id];
  }

  setWrappedComponent(
    componentName: string | number,
    wrappedComponent: React.ComponentClass<any>
  ): void {
    this.wrappedComponents[componentName] = wrappedComponent;
  }

  hasRegisteredWrappedComponent(componentName: string | number): boolean {
    return componentName in this.wrappedComponents;
  }

  getWrappedComponent(componentName: string | number): React.ComponentClass<any> {
    return this.wrappedComponents[componentName];
  }

  setLazyComponentRegistrator(lazyRegistratorFn: (lazyComponentRequest: string | number) => void) {
    this.lazyRegistratorFn = lazyRegistratorFn;
  }
}
