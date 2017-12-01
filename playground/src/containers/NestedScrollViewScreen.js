const React = require('react');
const { Component } = require('react');

const { StyleSheet, Text, View } = require('react-native');

const Navigation = require('react-native-navigation');
const CoordinatorLayout = Navigation.CoordinatorLayout;
const NestedScrollView = Navigation.NestedScrollView;
const FloatingActionButton = Navigation.FloatingActionButton;

class NestedScrollViewScreen extends Component {

  render() {
    return (
      <CoordinatorLayout style={{ flex: 1 }}>
        <NestedScrollView>
          <View collapsable={false} >
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
            <Text style={styles.text}>Test Nigga</Text>
          </View>
        </NestedScrollView>
        <FloatingActionButton
          gravityBottom
          gravityRight
          backgroundColor={'blue'}
        />
      </CoordinatorLayout>
    );
  }
}

module.exports = NestedScrollViewScreen;

const styles = StyleSheet.create({
  text: {
    marginBottom: 80,
    textAlign: 'center'
  }
});

