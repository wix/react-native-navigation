// CounterScreen.js
import React from 'react';
import { Button, Text, View } from 'react-native';
import { observer } from 'mobx-react-lite';
import { CounterStoreContext } from './counter.store';

const CounterScreen = observer((props) => {
  const { count, increment, decrement } = React.useContext(CounterStoreContext);

  return (
    <Root>
      <Text>{`Clicked ${count} times!`}</Text>
      <Button title="Increment" onPress={increment} />
      <Button title="Decrement" onPress={decrement} />
      <Button title="Push" onPress={() => Navigation.push(props.componentId, 'CounterScreen')} />
    </Root>
  );
});
module.exports = CounterScreen;
