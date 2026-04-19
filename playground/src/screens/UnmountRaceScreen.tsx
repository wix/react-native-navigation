import React from 'react';
import { Text, View, StyleSheet } from 'react-native';
import { NavigationProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

let sharedData: string | null = null;

interface Props extends NavigationProps {
  stackComponentId?: string;
}

export default class UnmountRaceScreen extends React.Component<Props, { data: string }> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Unmount Race',
        },
      },
    };
  }

  state = { data: 'loading' };
  private checkTimer: ReturnType<typeof setTimeout> | null = null;

  componentDidMount() {
    sharedData = 'loaded';
    this.checkTimer = setTimeout(() => {
      this.setState({ data: sharedData ?? 'stale_unmount' });
    }, 600);
  }

  componentWillUnmount() {
    if (this.checkTimer) {
      clearTimeout(this.checkTimer);
    }
    sharedData = null;
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <View style={styles.container}>
          <Text testID={testIDs.UNMOUNT_RACE_DATA} style={styles.text}>
            {this.state.data}
          </Text>
        </View>
        <Button
          label="Pop and Re-push"
          testID={testIDs.POP_AND_REPUSH_BTN}
          onPress={this.popAndRepush}
        />
        <Button label="Pop" testID={testIDs.POP_BTN} onPress={this.pop} />
      </Root>
    );
  }

  popAndRepush = () => {
    const stackId = this.props.stackComponentId;
    if (!stackId) return;
    Navigation.pop(this.props.componentId);
    Navigation.push(stackId, {
      component: {
        name: Screens.UnmountRace,
        passProps: { stackComponentId: stackId },
      },
    });
  };

  pop = () => Navigation.pop(this);
}

const styles = StyleSheet.create({
  container: { padding: 20, alignItems: 'center' },
  text: { fontSize: 24 },
});
