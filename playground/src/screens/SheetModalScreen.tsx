import React from 'react';
import { ScrollView, StyleSheet, Text, View } from 'react-native';
import { NavigationComponent, OptionsModalPresentationStyle } from 'react-native-navigation';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const {
  SHEET_MODAL_SCREEN,
  SHEET_DETENT_STATUS,
  SHEET_DETENT_EXPAND_LARGE_BTN,
  SHEET_DETENT_COMPACT_BTN,
  SHEET_DETENT_MEDIUM_BTN,
  SHEET_DETENT_DISMISS_BTN,
} = testIDs;

const PLACEHOLDER_LINES = Array.from({ length: 24 }, (_, index) => `Row ${index + 1}`);

type State = {
  activeDetent: string;
};

export default class SheetModalScreen extends NavigationComponent<Record<string, unknown>, State> {
  state: State = {
    activeDetent: 'medium',
  };

  static options() {
    return {
      topBar: {
        title: {
          text: 'Sheet Detents',
        },
      },
      modalPresentationStyle: OptionsModalPresentationStyle.pageSheet,
      modal: {
        detents: ['medium', { id: 'compact', height: 220 }, 'large'],
        selectedDetent: 'medium',
        largestUndimmedDetent: 'medium',
        prefersGrabberVisible: true,
        swipeToDismiss: true,
      },
    };
  }

  render() {
    return (
      <View style={styles.root} testID={SHEET_MODAL_SCREEN}>
        <Text style={styles.hint}>
          Drag the grabber to resize. Use the buttons to change the selected detent at runtime.
        </Text>
        <Text testID={SHEET_DETENT_STATUS}>{this.state.activeDetent}</Text>
        <Button
          label="Expand to large"
          testID={SHEET_DETENT_EXPAND_LARGE_BTN}
          onPress={this.selectLargeDetent}
        />
        <Button
          label="Snap to compact (220pt)"
          testID={SHEET_DETENT_COMPACT_BTN}
          onPress={this.selectCompactDetent}
        />
        <Button
          label="Back to medium"
          testID={SHEET_DETENT_MEDIUM_BTN}
          onPress={this.selectMediumDetent}
        />
        <Button label="Dismiss modal" testID={SHEET_DETENT_DISMISS_BTN} onPress={this.dismiss} />
        <ScrollView style={styles.scroll} contentContainerStyle={styles.scrollContent}>
          {PLACEHOLDER_LINES.map(line => (
            <View key={line} style={styles.row}>
              <Text style={styles.rowText}>{line}</Text>
            </View>
          ))}
        </ScrollView>
      </View>
    );
  }

  selectLargeDetent = () => {
    this.setState({ activeDetent: 'large' });
    Navigation.mergeOptions(this, {
      modal: {
        selectedDetent: 'large',
      },
    });
  };

  selectCompactDetent = () => {
    this.setState({ activeDetent: 'compact' });
    Navigation.mergeOptions(this, {
      modal: {
        selectedDetent: 'compact',
      },
    });
  };

  selectMediumDetent = () => {
    this.setState({ activeDetent: 'medium' });
    Navigation.mergeOptions(this, {
      modal: {
        selectedDetent: 'medium',
      },
    });
  };

  dismiss = () => Navigation.dismissModal(this);
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    padding: 16,
  },
  hint: {
    marginBottom: 12,
    fontSize: 15,
  },
  scroll: {
    flex: 1,
    marginTop: 12,
  },
  scrollContent: {
    paddingBottom: 48,
  },
  row: {
    paddingVertical: 14,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: '#ccc',
  },
  rowText: {
    fontSize: 16,
  },
});
