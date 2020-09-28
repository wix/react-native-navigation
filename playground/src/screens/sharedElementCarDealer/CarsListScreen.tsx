import React, { useCallback } from 'react';
import { Platform, SafeAreaView, ScrollView, StyleSheet } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';
import cars, { CarItem } from '../../assets/cars';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import { buildSharedElementAnimations } from './Constants';
import CarCard from './CarCard';

// SET = Shared Element Transition
// TODO: 1. Spring interpolation including configuration of mass/springiness: https://github.com/wix/react-native-navigation/issues/6431
// TODO: 2. Make SETs for Overlays possible OR allow parent screen to be visible beneath PostDetailsScreen to allow animation similar to Apple's App of the Day (AppStore) animation: https://github.com/wix/react-native-navigation/issues/6431
// TODO: 3. Add bottomTabs animation support so it slides out nicely (translateY): https://github.com/wix/react-native-navigation/issues/6340 and https://github.com/wix/react-native-navigation/issues/6567
// TODO: 4. Add topBar animation support so it slides out nicely (translateY): (no issue for that yet?)

const CarsListScreen: NavigationFunctionComponent = ({ componentId }) => {
  const onCarPressed = useCallback(
    async (car: CarItem) => {
      const navigationAnimations = await buildSharedElementAnimations(car);
      Navigation.push(componentId, {
        component: {
          name: Screens.CarDetailsScreen,
          passProps: { car: car },
          options: {
            animations: navigationAnimations,
          },
        },
      });
    },
    [componentId]
  );

  return (
    <SafeAreaView>
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        contentInsetAdjustmentBehavior="never"
      >
        {cars.map((car) => (
          <CarCard
            key={car.id}
            parentComponentId={componentId}
            onCarPressed={() => onCarPressed(car)}
            car={car}
          />
        ))}
      </ScrollView>
    </SafeAreaView>
  );
};

CarsListScreen.options = {
  ...Platform.select({
    android: {
      statusBar: {
        style: 'dark' as const,
        backgroundColor: 'white',
      },
    },
  }),
  topBar: {
    title: {
      text: 'Car Dealer',
    },
  },
};

export default CarsListScreen;

const styles = StyleSheet.create({
  scrollContent: {
    paddingVertical: 25,
  },
});
