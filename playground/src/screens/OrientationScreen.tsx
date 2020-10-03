import React from 'react';
import { Navigation, NavigationComponentProps, LayoutOrientation } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import testIDs from '../testIDs';

const {
  LANDSCAPE_PORTRAIT_ORIENTATION_BTN,
  LANDSCAPE_ORIENTATION_BTN,
  PORTRAIT_ORIENTATION_BTN,
} = testIDs;

export default class OrientationScreen extends React.Component<NavigationComponentProps> {
  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button
          label="Landscape and Portrait"
          testID={LANDSCAPE_PORTRAIT_ORIENTATION_BTN}
          onPress={() => this.orientation(['landscape', 'portrait'])}
        />
        <Button
          label="Portrait"
          testID={PORTRAIT_ORIENTATION_BTN}
          onPress={() => this.orientation(['portrait'])}
        />
        <Button
          label="Landscape"
          testID={LANDSCAPE_ORIENTATION_BTN}
          onPress={() => this.orientation(['landscape'])}
        />
      </Root>
    );
  }

  // LayoutOrientation is not exposed by the API.
  orientation(orientation: LayoutOrientation[]) {
    Navigation.showModal({
      component: {
        name: Screens.OrientationDetect,
        passProps: {
          orientation,
        },
      },
    });
  }
}
