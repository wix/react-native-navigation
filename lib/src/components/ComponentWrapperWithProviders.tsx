import * as React from 'react';
import {ComponentWrapper} from './ComponentWrapper';
import { ComponentProvider } from 'react-native';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { Store } from './Store';

export class ComponentWrapperWithProviders extends ComponentWrapper {
  private readonly componentWrapper: any;

  constructor(componentWrapper: any) {
    super();
    this.componentWrapper = componentWrapper;
    console.log('guyca', `ctor ${this.constructor.name}`);
  }

  do() {
    super.do();
    console.log('guyca', 'do ComponentWrapperWithProviders');
  }

  wrap(
    componentName: string | number,
    OriginalComponentGenerator: ComponentProvider,
    store: Store,
    componentEventsObserver: ComponentEventsObserver,
  ): React.ComponentClass<any> {
    console.error('err');
    const component = super.wrap(componentName, OriginalComponentGenerator, store, componentEventsObserver);
    console.log('guyca', 'in super');
    return this.componentWrapper(component);
  }

  // wrapComponent(WrappedComponent: React.ComponentClass<any>): React.ComponentClass<any> {
    // const wrap = (props) => {
    //   let wrapped = <WrappedComponent {...props}/>;
    //   this.providers.reverse();
    //   this.providers.forEach((provider) => {
    //     const Provider = provider[0];
    //     console.log('guyca', `value: ${provider}`);
    //     const args = provider[1] || {};
    //     wrapped = <Provider {...args}>
    //       {wrapped}
    //     </Provider>;
    //   });
    //   return wrapped;
    // };

    // class Wrapper extends React.Component<any, any> {
    //   render() {
    //     return (
    //       wrap(this.props)
    //     );
    //   }
    // }
    // require('hoist-non-react-statics')(Wrapper, WrappedComponent);
    // return Wrapper;
  // }
}
