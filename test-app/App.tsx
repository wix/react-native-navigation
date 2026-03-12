import React from 'react';
import {View, Text, ScrollView, StyleSheet} from 'react-native';
import {Navigation, NavigationFunctionComponent} from 'react-native-navigation';

const HomeScreen: NavigationFunctionComponent = () => {
  const items = Array.from({length: 40}, (_, i) => `Item ${i + 1}`);
  return (
    <ScrollView style={styles.scroll} contentContainerStyle={styles.content}>
      {items.map(item => (
        <View key={item} style={styles.card}>
          <Text style={styles.cardText}>{item}</Text>
        </View>
      ))}
    </ScrollView>
  );
};
HomeScreen.options = {
  topBar: {title: {text: 'Home'}},
  bottomTab: {text: 'Home'},
};

const SettingsScreen: NavigationFunctionComponent = () => (
  <View style={styles.center}>
    <Text style={styles.title}>Settings</Text>
  </View>
);
SettingsScreen.options = {
  topBar: {title: {text: 'Settings'}},
  bottomTab: {text: 'Settings'},
};

Navigation.registerComponent('Home', () => HomeScreen);
Navigation.registerComponent('Settings', () => SettingsScreen);

Navigation.events().registerAppLaunchedListener(() => {
  Navigation.setRoot({
    root: {
      bottomTabs: {
        children: [
          {
            stack: {
              children: [{component: {name: 'Home'}}],
            },
          },
          {
            stack: {
              children: [{component: {name: 'Settings'}}],
            },
          },
        ],
      },
    },
  });
});

const styles = StyleSheet.create({
  scroll: {flex: 1, backgroundColor: '#f5f5f5'},
  content: {padding: 16},
  card: {
    backgroundColor: '#fff',
    padding: 20,
    marginBottom: 12,
    borderRadius: 8,
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 4,
    shadowOffset: {width: 0, height: 2},
  },
  cardText: {fontSize: 16},
  center: {flex: 1, justifyContent: 'center', alignItems: 'center'},
  title: {fontSize: 24},
});
