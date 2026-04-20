import React from 'react';
import { ScrollView, Text, StyleSheet, View } from 'react-native';
import { NavigationProps, NavigationState, NavigationStateChangedEvent } from 'react-native-navigation';
import Navigation from '../services/Navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import testIDs from '../testIDs';

const { NAV_STATE_GET_BTN, NAV_STATE_RESULT } = testIDs;

interface State {
  navigationState: NavigationState | null;
  lastEvent: NavigationStateChangedEvent | null;
}

export default class NavigationStateScreen extends React.Component<NavigationProps, State> {
  private stateListener: any;

  constructor(props: NavigationProps) {
    super(props);
    this.state = {
      navigationState: null,
      lastEvent: null,
    };
  }

  componentDidMount() {
    this.stateListener = Navigation.events().registerNavigationStateChangedListener(
      (event: NavigationStateChangedEvent) => {
        this.setState({ lastEvent: event });
      }
    );
  }

  componentWillUnmount() {
    if (this.stateListener) {
      this.stateListener.remove();
    }
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Get Navigation State" testID={NAV_STATE_GET_BTN} onPress={this.getState} />
        <Button label="Push Screen" onPress={this.push} />
        <Button label="Show Modal" onPress={this.showModal} />
        <ScrollView style={styles.scrollView}>
          {this.state.navigationState && (
            <View testID={NAV_STATE_RESULT}>
              <Text style={styles.title}>Navigation State:</Text>
              <Text style={styles.json}>
                {JSON.stringify(this.state.navigationState, null, 2)}
              </Text>
            </View>
          )}
          {this.state.lastEvent && (
            <View>
              <Text style={styles.title}>
                Last State Change ({this.state.lastEvent.commandName}):
              </Text>
              <Text style={styles.json}>
                {JSON.stringify(this.state.lastEvent.state, null, 2)}
              </Text>
            </View>
          )}
        </ScrollView>
      </Root>
    );
  }

  getState = async () => {
    const state = await Navigation.getState();
    this.setState({ navigationState: state });
  };

  push = () => Navigation.push(this, Screens.Pushed);

  showModal = () => Navigation.showModal(Screens.Modal);
}

const styles = StyleSheet.create({
  scrollView: {
    flex: 1,
    padding: 8,
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 12,
    marginBottom: 4,
  },
  json: {
    fontFamily: 'Courier',
    fontSize: 12,
    color: '#333',
  },
});
