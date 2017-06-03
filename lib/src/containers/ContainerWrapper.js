import _ from 'lodash';
import React, { Component } from 'react';

export default class ContainerWrapper {
  static wrap(containerName, OriginalContainer, store) {
    return class extends Component {
      constructor(props) {
        super(props);
        this._saveContainerRef = this._saveContainerRef.bind(this);
        this._assertId(props);
        this._createState(props);
      }

      _assertId(props) {
        if (!props.id) {
          throw new Error(`Container ${containerName} does not have an id!`);
        }
      }

      _createState(props) {
        this.state = {
          id: props.id,
          allProps: _.merge({}, props, store.getPropsForContainerId(props.id))
        };
      }

      _saveContainerRef(r) {
        this.originalContainerRef = r;
      }

      componentWillMount() {
        store.setRefForId(this.state.id, this);
      }

      componentWillUnmount() {
        store.cleanId(this.state.id);
      }

      onStart() {
        if (this.originalContainerRef.onStart) {
          this.originalContainerRef.onStart();
        }
      }

      onStop() {
        if (this.originalContainerRef.onStop) {
          this.originalContainerRef.onStop();
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          allProps: _.merge({}, nextProps, store.getPropsForContainerId(this.state.id))
        });
      }

      render() {
        return (
          <OriginalContainer
            ref={this._saveContainerRef}
            {...this.state.allProps}
            id={this.state.id}
            key={this.state.id}
          />
        );
      }
    };
  }
}
