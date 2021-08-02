import React, { useCallback } from 'react';
import { Platform, SafeAreaView, ScrollView, StyleSheet, View, Text } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';
import cars, { CarItem } from '../../assets/cars';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import { buildSharedElementAnimations, buildStorySharedElementAnimations } from './Constants';
import CarCard from './CarCard';
import PressableScale from '../../components/PressableScale';
import colors from '../../commons/Colors';

const STORY_SIZE = 60;

// SET = Shared Element Transition
// TODO: 1. Spring interpolation including configuration of mass/springiness: https://github.com/wix/react-native-navigation/issues/6431
// TODO: 2. Make SETs for Overlays possible OR allow parent screen to be visible beneath PostDetailsScreen to allow animation similar to Apple's App of the Day (AppStore) animation: https://github.com/wix/react-native-navigation/issues/6431
// TODO: 3. Add bottomTabs animation support so it slides out nicely (translateY): https://github.com/wix/react-native-navigation/issues/6340 and https://github.com/wix/react-native-navigation/issues/6567
// TODO: 4. Add topBar animation support so it slides out nicely (translateY): (no issue for that yet?)

const CarsListScreen: NavigationFunctionComponent = ({ componentId }) => {
  const onCarPressed = useCallback((car: CarItem) => {
    Navigation.showModal({
      stack: {
        children: [
          {
            component: {
              name: Screens.CarDetailsScreen,
              passProps: { car: car },
              options: {
                animations: buildSharedElementAnimations(car),
              },
            },
          },
        ],
      },
    });
  }, []);
  const onCarStoryPressed = useCallback((car: CarItem) => {
    Navigation.showModal({
      component: {
        name: Screens.CarStoryScreen,
        passProps: { car: car },
        options: {
          animations: buildStorySharedElementAnimations(car),
        },
      },
    });
  }, []);

  return (
    <SafeAreaView>
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        contentInsetAdjustmentBehavior="never"
      >
        <ScrollView horizontal={true} style={styles.storyScrollView}>
          {cars.map((car) => (
            <PressableScale
              key={car.id}
              style={styles.storyContainer}
              onPress={() => onCarStoryPressed(car)}
            >
              <View style={styles.storyBackground} nativeID={`story.${car.id}.background.from`} />
              <View style={styles.story}>
                <Text style={styles.storyIcon} nativeID={`story.${car.id}.icon.from`}>
                  {car.name.charAt(0)}
                </Text>
              </View>
              <Text
                style={styles.storyTitle}
                nativeID={`story.${car.id}.title.from`}
                ellipsizeMode="tail"
                numberOfLines={1}
              >
                {car.name}
              </Text>
            </PressableScale>
          ))}
        </ScrollView>
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
  storyScrollView: {
    paddingLeft: 20,
  },
  storyContainer: {
    width: STORY_SIZE,
    marginRight: 15,
    borderRadius: STORY_SIZE / 2,
  },
  storyBackground: {
    position: 'absolute',
    width: STORY_SIZE,
    height: STORY_SIZE,
    borderRadius: STORY_SIZE / 2,
    backgroundColor: colors.primary.light,
  },
  story: {
    width: STORY_SIZE,
    height: STORY_SIZE,
    justifyContent: 'center',
    alignItems: 'center',
  },
  storyIcon: {
    fontSize: 20,
    fontWeight: 'bold',
    color: 'white',
  },
  storyTitle: {
    fontSize: 15,
    fontWeight: 'bold',
    color: 'black',
    maxWidth: '100%',
  },
});
