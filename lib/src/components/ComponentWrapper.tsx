import * as React from 'react';
import { ComponentProvider } from 'react-native';
import * as  _ from 'lodash';
import * as ReactLifecyclesCompat from 'react-lifecycles-compat';
import { Store } from './Store';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';

interface HocState { componentId: string; allProps: {}; }
interface HocProps { componentId: string; }

export class ComponentWrapper {
  static wrap(
    componentName: string | number,
    OriginalComponentGenerator: ComponentProvider,
    store: Store,
    componentEventsObserver: ComponentEventsObserver,
    Provider?: any,
    providerParams?: any
  ): React.ComponentClass<any> {
    const GeneratedComponentClass = OriginalComponentGenerator();
    class WrappedComponent extends React.Component<HocProps, HocState> {
      static getDerivedStateFromProps(nextProps: any, prevState: HocState) {
        return {
          allProps: _.merge({}, nextProps, store.getPropsForId(prevState.componentId))
        };
      }

      constructor(props: HocProps) {
        super(props);
        this._assertComponentId();
        this.state = {
          componentId: props.componentId,
          allProps: {}
        };
      }

      componentWillUnmount() {
        store.cleanId(this.state.componentId);
        componentEventsObserver.unmounted(this.state.componentId);
      }

      render() {
        return (
          <GeneratedComponentClass
            {...this.state.allProps}
            componentId={this.state.componentId}
            key={this.state.componentId}
          />
        );
      }

      private _assertComponentId() {
        if (!this.props.componentId) {
          throw new Error(`Component ${componentName} does not have a componentId!`);
        }
      }
    }

    ReactLifecyclesCompat.polyfill(WrappedComponent);
    require('hoist-non-react-statics')(WrappedComponent, GeneratedComponentClass);

    if (Provider) {
      return ComponentWrapper.wrapWithProvider(WrappedComponent, Provider, providerParams);
    } else {
      return WrappedComponent;
    }
  }

  static wrapWithProvider(WrappedComponent: React.ComponentClass<any>, Provider: any, providerParams: {}): React.ComponentClass<any> {
    class ProviderWrapper extends React.Component<any, any> {
      render () {
        return (
          <Provider {...providerParams}>
            <WrappedComponent {...this.props} />
          </Provider>
        );
      }
    }

    require('hoist-non-react-statics')(ProviderWrapper, WrappedComponent);
    return ProviderWrapper;
  }
}
