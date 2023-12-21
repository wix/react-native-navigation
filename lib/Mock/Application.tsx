import * as React from 'react';
import { View } from 'react-native';
import { observer } from 'remx';

interface ApplicationProps {
  entryPoint: () => void;
}

export const Application = observer(
  class extends React.Component<ApplicationProps> {
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
