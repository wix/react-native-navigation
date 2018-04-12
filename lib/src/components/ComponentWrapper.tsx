import * as React from 'react';
import * as  _ from 'lodash';

export class ComponentWrapper {

  static wrap(componentName: string, OriginalComponentClass: React.ComponentType<any>, store): React.ComponentType<any> {

    class WrappedComponent extends React.Component<any, { componentId: string; allProps: {}; }> {

      private originalComponentRef;

      constructor(props) {
        super(props);
        this._assertComponentId();
        this._saveComponentRef = this._saveComponentRef.bind(this);
        this.state = {
          componentId: props.componentId,
          allProps: _.merge({}, props, store.getPropsForId(props.componentId))
        };
      }

      componentWillMount() {
        store.setRefForId(this.state.componentId, this);
      }

      componentWillUnmount() {
        store.cleanId(this.state.componentId);
      }

      componentDidAppear() {
        if (this.originalComponentRef.componentDidAppear) {
          this.originalComponentRef.componentDidAppear();
        }
      }

      componentDidDisappear() {
        if (this.originalComponentRef.componentDidDisappear) {
          this.originalComponentRef.componentDidDisappear();
        }
      }

      onNavigationButtonPressed(buttonId) {
        if (this.originalComponentRef.onNavigationButtonPressed) {
          this.originalComponentRef.onNavigationButtonPressed(buttonId);
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          allProps: _.merge({}, nextProps, store.getPropsForId(this.state.componentId))
        });
      }

      render() {
        return (
          <OriginalComponentClass
            ref={this._saveComponentRef}
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

      private _saveComponentRef(r) {
        this.originalComponentRef = r;
      }
    }

    return WrappedComponent;
  }
}
