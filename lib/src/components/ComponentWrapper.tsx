import * as React from 'react';
import * as  _ from 'lodash';

interface State {
  componentId: string;
  allProps: {};
}

export class ComponentWrapper {
  static wrap(componentName: string, OriginalComponentClass: React.ComponentType<any>, store): React.ComponentType<any> {

    class WrappedComponent extends React.Component<any, State> {

      private originalComponentRef;

      constructor(props) {
        super(props);
        this._saveComponentRef = this._saveComponentRef.bind(this);
        this._assertComponentId(props);
        this.state = {
          componentId: props.componentId,
          allProps: _.merge({}, props, store.getPropsForComponentId(props.componentId))
        };
      }

      _assertComponentId(props) {
        if (!props.componentId) {
          throw new Error(`Component ${componentName} does not have a componentId!`);
        }
      }

      _saveComponentRef(r) {
        this.originalComponentRef = r;
      }

      componentWillMount() {
        store.setRefForComponentId(this.state.componentId, this);
      }

      componentWillUnmount() {
        store.cleanId(this.state.componentId);
      }

      didAppear() {
        if (this.originalComponentRef.didAppear) {
          this.originalComponentRef.didAppear();
        }
      }

      didDisappear() {
        if (this.originalComponentRef.didDisappear) {
          this.originalComponentRef.didDisappear();
        }
      }

      onNavigationButtonPressed(buttonId) {
        if (this.originalComponentRef.onNavigationButtonPressed) {
          this.originalComponentRef.onNavigationButtonPressed(buttonId);
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          allProps: _.merge({}, nextProps, store.getPropsForComponentId(this.state.componentId))
        });
      }

      render() {
        return (
          <OriginalComponentClass
            ref={this._saveComponentRef}
            { ...this.state.allProps }
            componentId={this.state.componentId}
            key={this.state.componentId}
          />
        );
      }
    }

    return WrappedComponent;
  }
}
