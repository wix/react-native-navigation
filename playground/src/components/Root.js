const React = require('react');
const { TextInput } = require('react-native');
const { SafeAreaView, View, Text, StyleSheet, ScrollView, } = require('react-native');
const { KeyboardAwareInsetsView } = require('react-native-keyboard-tracking-view');
const { showTextInputToTestKeyboardInteraction } = require('../flags');

module.exports = (props) =>
  <SafeAreaView style={styles.root}>
    <ScrollView contentContainerStyle={[styles.scrollView, props.style]}>
      {props.children}
      {renderFooter(props)}
    </ScrollView>
    {showTextInputToTestKeyboardInteraction && <KeyboardAwareInsetsView />}
  </SafeAreaView>

const renderFooter = (props) => props.componentId && <View style={styles.footer}>
  {renderInput()}
  {renderFooterText(props)}
  {renderComponentId(props)}
</View>;

const renderInput = () => showTextInputToTestKeyboardInteraction && <TextInput placeholder='Input' style={{ borderWidth: 1, borderRadius: 10, height: 40, width: 100 }} />;
const renderFooterText = (props) => props.footer && <Text style={styles.footerText}>{props.footer}</Text>;
const renderComponentId = (props) => <Text style={styles.footerText}>{`this.props.componentId = ${props.componentId}`}</Text>;

const styles = StyleSheet.create({
  root: {
    flex: 1
  },
  scrollView: {
    flexGrow: 1,
    alignItems: 'center',
    padding: 16
  },
  footer: {
    flex: 1,
    justifyContent: 'flex-end',
    alignItems: 'center'
  },
  footerText: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
});