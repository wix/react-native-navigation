import * as React from 'react';
import * as  _ from 'lodash';
import * as ReactLifecyclesCompat from 'react-lifecycles-compat';

export class ComponentWrapper {

  static wrap(
    componentName: string,
    OriginalComponentClass: React.ComponentType<any>,
    store,
    componentEventsObserver,
    ReduxProvider?,
    reduxStore?): React.ComponentType<any> {

    class WrappedComponent extends React.Component<any, { componentId: string; allProps: {}; }> {

      static getDerivedStateFromProps(nextProps, prevState) {
        return {
          allProps: _.merge({}, nextProps, store.getPropsForId(prevState.componentId))
        };
      }

      constructor(props) {
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
          <OriginalComponentClass
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
    require('hoist-non-react-statics')(WrappedComponent, OriginalComponentClass);

    if (reduxStore) {
      return ComponentWrapper.wrapWithRedux(WrappedComponent, ReduxProvider, reduxStore);
    } else {
      return WrappedComponent;
    }
  }

  static wrapWithRedux(WrappedComponent, ReduxProvider, reduxStore): React.ComponentType<any> {
    class ReduxWrapper extends React.Component<any, any> {
      render() {
        return (
          <ReduxProvider store={reduxStore}>
            <WrappedComponent {...this.props} />
          </ReduxProvider>
        );
      }
    }
    require('hoist-non-react-statics')(ReduxWrapper, WrappedComponent);
    return ReduxWrapper;
  }
}
