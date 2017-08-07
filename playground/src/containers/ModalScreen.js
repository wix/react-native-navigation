const _ = require('lodash');

const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const Navigation = require('react-native-navigation');

class ModalScreen extends Component {
  constructor(props) {
    super(props);
    this.onClickShowModal = this.onClickShowModal.bind(this);
    this.onClickDismissModal = this.onClickDismissModal.bind(this);
    this.onClickDismissPreviousModal = this.onClickDismissPreviousModal.bind(this);
    this.onClickDismissUnknownModal = this.onClickDismissUnknownModal.bind(this);
    this.onClickDismissAllPreviousModals = this.onClickDismissAllPreviousModals.bind(this);
    this.onClickDismissFirstInStack = this.onClickDismissFirstInStack.bind(this);
    this.onClickDismissAllModals = this.onClickDismissAllModals.bind(this);
  }
  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Modal Screen`}</Text>
        <Text style={styles.footer}>{`Modal Stack Position: ${this.getModalPosition()}`}</Text>
        <Button title="Show Modal" onPress={this.onClickShowModal} />
        <Button title="Dismiss Modal" onPress={this.onClickDismissModal} />
        <Button title="Dismiss Unknown Modal" onPress={this.onClickDismissUnknownModal} />
        <Button title="Dismiss All Modals" onPress={this.onClickDismissAllModals} />
        {this.getPreviousModalId() ? (<Button title="Dismiss Previous Modal" onPress={this.onClickDismissPreviousModal} />) : undefined}
        {this.props.previousModalIds ? (<Button title="Dismiss ALL Previous Modals" onPress={this.onClickDismissAllPreviousModals} />) : undefined}
        {this.props.previousModalIds ? (<Button title="Dismiss First In Stack" onPress={this.onClickDismissFirstInStack} />) : undefined}
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>

      </View>
    );
  }

  onClickShowModal() {
    Navigation.showModal({
      container: {
        name: 'navigation.playground.ModalScreen',
        passProps: {
          modalPosition: this.getModalPosition() + 1,
          previousModalIds: _.concat([], this.props.previousModalIds || [], this.props.containerId)
        }
      }
    });
  }

  onClickDismissModal() {
    Navigation.dismissModal(this.props.containerId);
  }

  onClickDismissPreviousModal() {
    Navigation.dismissModal(this.getPreviousModalId());
  }

  onClickDismissUnknownModal() {
    Navigation.dismissModal('unknown');
  }

  onClickDismissAllPreviousModals() {
    _.forEach(this.props.previousModalIds, (id) => Navigation.dismissModal(id));
  }

  onClickDismissFirstInStack() {
    Navigation.dismissModal(_.head(this.props.previousModalIds));
  }

  onClickDismissAllModals() {
    Navigation.dismissAllModals();
  }

  getModalPosition() {
    return (this.props.modalPosition || 1);
  }

  getPreviousModalId() {
    return _.last(this.props.previousModalIds);
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};

module.exports = ModalScreen;
