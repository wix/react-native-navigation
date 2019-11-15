import { ComponentProvider } from 'react-native';
import { IWrappedComponent } from './ComponentWrapper';

export class Store {
  private componentsByName: Record<string, ComponentProvider> = {};
  private propsById: Record<string, any> = {};
  private componentsInstancesById: Record<string, IWrappedComponent> = {};

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
    return this.componentsByName[componentName.toString()];
  }

  setComponentInstance(id: string, component: IWrappedComponent): void {
    this.componentsInstancesById[id] = component;
  }

  getComponentInstance(id: string): IWrappedComponent {
    return this.componentsInstancesById[id];
  }
}
