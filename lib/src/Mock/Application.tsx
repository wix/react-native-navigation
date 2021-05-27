import React, { Component } from 'react';
import { View } from 'react-native';
import {
  DEFAULT_BACK_BUTTON_TEST_ID,
  VISIBLE_OVERLAY_TEST_ID,
  VISIBLE_SCREEN_TEST_ID,
} from './constants';
import { connect } from './connect';

interface ApplicationProps {
  entryPoint: () => void;
}

export const Application = connect(
  class extends Component<ApplicationProps> {
    static VISIBLE_SCREEN_TEST_ID = VISIBLE_SCREEN_TEST_ID;
    static VISIBLE_OVERLAY_TEST_ID = VISIBLE_OVERLAY_TEST_ID;
    static DEFAULT_BACK_BUTTON_TEST_ID = DEFAULT_BACK_BUTTON_TEST_ID;
    constructor(props: ApplicationProps) {
      super(props);
      props.entryPoint();
    }

    render() {
      const { LayoutComponent } = require('./Components/LayoutComponent');
      const { LayoutStore } = require('./Stores/LayoutStore');
      const { Modals } = require('./Components/Modals');
      const { Overlays } = require('./Components/Overlays');
      return (
        <View testID={'Application'}>
          <LayoutComponent layoutNode={LayoutStore.getLayout()} />
          <Modals />
          <Overlays />
        </View>
      );
    }
  }
);
