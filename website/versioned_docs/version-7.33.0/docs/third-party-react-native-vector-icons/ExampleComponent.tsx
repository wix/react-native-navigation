import React from 'react';
import { Text, View } from 'react-native';

import { iconsMap } from './NavIcons';
interface Props {
  // ... my props
}

function ExampleComponent({ }: Props) {
  return (
    <View>
      <Text>An example component</Text>
    </View>
  )
}

ExampleComponent.options = () => {
  return {
    topBar: {
      title: {
        text: 'Example Component',
      },
      leftButtons: [{
        icon: iconsMap.add,
        color: '#888',
        accessibilityLabel: 'Add',
      }],
    },
  };
};

export default ExampleComponent;
 
