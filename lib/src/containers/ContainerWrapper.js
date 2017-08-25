const _ = require('lodash');

const React = require('react');
const { Component } = require('react');

class ContainerWrapper {
  static wrap(containerName, OriginalContainer, store) {
    return class extends Component {
      constructor(props) {
        super(props);
        this._saveContainerRef = this._saveContainerRef.bind(this);
        this._assertContainerId(props);
        this.state = {
          containerId: props.containerId,
          allProps: _.merge({}, props, store.getPropsForContainerId(props.containerId))
        };
      }

      _assertContainerId(props) {
        if (!props.containerId) {
          throw new Error(`Container ${containerName} does not have a containerId!`);
        }
      }

      _saveContainerRef(r) {
        this.originalContainerRef = r;
      }

      componentWillMount() {
        store.setRefForContainerId(this.state.containerId, this);
      }

      componentWillUnmount() {
        store.cleanId(this.state.containerId);
      }

      didAppear() {
        if (this.originalContainerRef.didAppear) {
          this.originalContainerRef.didAppear();
        }
      }

      didDisappear() {
        if (this.originalContainerRef.didDisappear) {
          this.originalContainerRef.didDisappear();
        }
      }

      onNavigationButtonPressed(buttonId) {
        if (this.originalContainerRef.onNavigationButtonPressed) {
          this.originalContainerRef.onNavigationButtonPressed(buttonId);
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          allProps: _.merge({}, nextProps, store.getPropsForContainerId(this.state.containerId))
        });
      }

      render() {
        return (
          <OriginalContainer
            ref={this._saveContainerRef}
            {...this.state.allProps}
            containerId={this.state.containerId}
            key={this.state.containerId}
          />
        );
      }
    };
  }
}

module.exports = ContainerWrapper;
