const React = require('react');
const {TextInput} = require('react-native');
const { View, Text, StyleSheet, ScrollView } = require('react-native');

module.exports = (props) =>
  <ScrollView contentContainerStyle={[styles.scrollView, props.style]}>
    {props.children}
    {props.componentId && <View style={styles.footer}>
      <TextInput placeholder='Input' style={{borderWidth: 1, borderRadius: 10, height: 40, width: 100}}/>
      {props.footer && <Text style={styles.footerText}>{props.footer}</Text>}
      <Text style={styles.footerText}>{`this.props.componentId = ${props.componentId}`}</Text>
    </View>}
  </ScrollView>;

const styles = StyleSheet.create({
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