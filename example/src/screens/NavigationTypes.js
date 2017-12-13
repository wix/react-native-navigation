import React from 'react';
import {StyleSheet, ScrollView} from 'react-native';
import Row from '../components/Row';

class NavigationTypes extends React.Component {

  constructor(props) {
    super(props);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
  }

  onNavigatorEvent(event) {
    if (event.type === 'DeepLink') {
      const parts = event.link.split('/');
      if (parts[0] === 'tab1') {
        this.props.navigator.push({
          screen: parts[1]
        });
      }
    }
  }

  toggleDrawer = () => {
    this.props.navigator.toggleDrawer({
      side: 'left',
      animated: true
    });
  };

  pushScreen = () => {
    this.props.navigator.push({
      screen: 'example.Types.Push',
      title: 'New Screen',
    });
  };

  previewScreen = () => {
    this.props.navigator.push({
      screen: 'example.Types.Push',
      title: 'New Screen',
      previewCommit: true,
      previewHeight: 250,
      previewView: this.previewRef,
      previewActions: [{
        id: 'action-cancel',
        title: 'Cancel'
      }, {
        id: 'action-delete',
        title: 'Delete',
        actions: [{
          id: 'action-delete-sure',
          title: 'Are you sure?',
          style: 'destructive'
        }]
      }]
    });
  };

  pushListScreen = () => {
    console.log('RANG', 'pushListScreen');
    this.props.navigator.push({
      screen: 'example.Types.ListScreen',
      title: 'List Screen',
    });
  };

  pushCustomTopBarScreen = () => {
    this.props.navigator.push({
      screen: 'example.Types.CustomTopBarScreen'
    });
  };

  pushCustomButtonScreen = () => {
    this.props.navigator.push({
      screen: 'example.Types.CustomButtonScreen',
      title: 'Custom Buttons'
    });
  };

  pushTopTabsScreen = () => {
    this.props.navigator.push({
      screen: 'example.Types.TopTabs',
      title: 'Top Tabs',
      topTabs: [{
        screenId: 'example.Types.TopTabs.TabOne',
        title: 'Tab One',
      }, {
        screenId: 'example.Types.TopTabs.TabTwo',
        title: 'Tab Two',
      }],
    });
  };

  showModal = () => {
    this.props.navigator.showModal({
      screen: 'example.Types.Modal',
      title: 'Modal',
    });
  };

  showLightBox = () => {
    this.props.navigator.showLightBox({
      screen: "example.Types.LightBox",
      passProps: {
        title: 'LightBox',
        content: 'Hey there, I\'m a light box screen :D',
        onClose: this.dismissLightBox,
      },
      style: {
        backgroundBlur: 'dark',
        backgroundColor: 'rgba(0, 0, 0, 0.7)',
        tapBackgroundToDismiss: true
      }
    });
  };

  dismissLightBox = () => {
    this.props.navigator.dismissLightBox();
  };

  showInAppNotification = () => {
    this.props.navigator.showInAppNotification({
      screen: 'example.Types.Notification',
    });
  };

  render() {
    return (
      <ScrollView style={styles.container}>
        <Row title={'Toggle Drawer'} onPress={this.toggleDrawer}/>
        <Row title={'Push Screen'} testID={'pushScreen'} onPress={this.pushScreen}/>
        <Row
          ref={(ref) => (this.previewRef = ref)}
          title={'Preview Screen'}
          testID={'previewScreen'}
          onPress={this.pushScreen}
          onPressIn={this.previewScreen}
        />
        {/*<Row title={'Push List Screen'} testID={'pushListScreen'} onPress={this.pushListScreen}/>*/}
        <Row title={'Custom TopBar'} onPress={this.pushCustomTopBarScreen}/>
        <Row title={'Custom Button'} onPress={this.pushCustomButtonScreen}/>
        <Row title={'Top Tabs Screen'} onPress={this.pushTopTabsScreen} platform={'android'}/>
        <Row title={'Show Modal'} onPress={this.showModal}/>
        <Row title={'Show Lightbox'} onPress={this.showLightBox}/>
        <Row title={'Show In-App Notification'} onPress={this.showInAppNotification}/>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  row: {
    height: 48,
    paddingHorizontal: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(0, 0, 0, 0.054)',
  },
  text: {
    fontSize: 16,
  },
});

export default NavigationTypes;
