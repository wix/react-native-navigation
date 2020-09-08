// App.tsx
import React from 'react';
import { Button, Text, View } from 'react-native';
import { CounterContext } from './CounterContext';

export const App = () => {
  // Using Context consumer
  return (
    <CounterContext.Consumer>
      {({ count, increment, decrement }) => (
        <View>
          <Text>{`Clicked ${count} times!`}</Text>
          <Button title="Increment" onPress={increment} />
          <Button title="Decrement" onPress={decrement} />
        </View>
      )}
    </CounterContext.Consumer>
  );
};
