import React from 'react';
import { TextInput, SafeAreaView, View, Text, StyleSheet, ScrollView, StyleProp, ViewStyle } from 'react-native';
import { KeyboardAwareInsetsView } from 'react-native-keyboard-tracking-view';
const { showTextInputToTestKeyboardInteraction } = require('../flags');

export type RootProps = {
  componentId?: string;
  style?: StyleProp<ViewStyle>;
  footer?: string;
  testID?: string;
  children?: React.ReactNode;
};

const Root = ({ children, componentId, footer, style, testID }: RootProps) => (
  <SafeAreaView style={styles.root} testID={testID}>
    <ScrollView contentContainerStyle={[styles.scrollView, style]}>
      {children}
      <Footer componentId={componentId} footer={footer} />
    </ScrollView>
    {showTextInputToTestKeyboardInteraction && <KeyboardAwareInsetsView />}
  </SafeAreaView>
);

type FooterProps = Pick<RootProps, 'componentId' | 'footer'>;

const Footer: React.FC<FooterProps> = ({ componentId, footer }) => {
  if (!componentId) {
    return null;
  }

  return (
    <View style={styles.footer}>
      {/* Rendering TextInput for a test. */}
      {showTextInputToTestKeyboardInteraction && <TextInput placeholder="Input" style={styles.testInput} />}

      {/* Rendering Footer. */}
      {footer && <Text style={styles.footerText}>{footer}</Text>}

      {/* Rendering component id. */}
      <Text style={styles.footerText}>{`this.props.componentId = ${componentId}`}</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
  },
  scrollView: {
    flexGrow: 1,
    alignItems: 'center',
    padding: 16,
  },
  testInput: {
    borderWidth: 1,
    borderRadius: 10,
    height: 40,
    width: 100,
  },
  footer: {
    flex: 1,
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  footerText: {
    fontSize: 10,
    color: '#888',
    marginTop: 10,
  },
});

export default Root;

// Keeping the old behavior to avoid a breaking change.
// This allows importing the component as `const Root = require('./Root')`.
module.exports = Root;
