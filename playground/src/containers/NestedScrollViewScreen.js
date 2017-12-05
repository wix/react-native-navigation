const React = require('react');
const { Component } = require('react');

const { StyleSheet, Text, View } = require('react-native');

const Navigation = require('react-native-navigation');
const CoordinatorLayout = Navigation.CoordinatorLayout;
const NestedScrollView = Navigation.NestedScrollView;
const FloatingActionButton = Navigation.FloatingActionButton;

class NestedScrollViewScreen extends Component {

  static get navigationOptions() {
    return {
      topBar: {
        title: 'Fab test',
        textColor: 'black',
        textFontSize: 16,
        textFontFamily: 'HelveticaNeue-Italic'
      }
    };
  }

  render() {
    return (
      <CoordinatorLayout style={{ flex: 1 }}>
        <NestedScrollView>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
          <Text style={styles.text}>Test Row</Text>
        </NestedScrollView>
        <FloatingActionButton
          collapsable={false}
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

