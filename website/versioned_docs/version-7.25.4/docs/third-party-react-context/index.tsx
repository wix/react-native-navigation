// index.tsx
import { Navigation } from 'react-native-navigation';
import { CounterContextProvider } from './CounterContext';
import { App } from './App';

Navigation.registerComponent(
  'App',
  () => (props) => (
    <CounterContextProvider>
      <App {...props} />
    </CounterContextProvider>
  ),
  () => App
);
