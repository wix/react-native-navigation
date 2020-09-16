import React, { useCallback } from 'react';
import { Platform, SafeAreaView, ScrollView, StyleSheet } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';
import posts, { PostItem } from '../../assets/posts';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import { buildSharedElementAnimations } from './Constants';
import PostCard from './PostCard';

// SET = Shared Element Transition
// TODO: 1. Animate borderRadius in SET: https://github.com/wix/react-native-navigation/issues/6519
// TODO: 2. Spring interpolation including configuration of mass/springiness: https://github.com/wix/react-native-navigation/issues/6431
// TODO: 3. Fix weird ScrollView insets on iOS in the PostDetailsScreen, see: https://github.com/wix/react-native-navigation/issues/6510
// TODO: 4. Fix image transitions on iOS, see: https://github.com/wix/react-native-navigation/issues/6505
// TODO: 5. Make SETs for Overlays possible OR allow parent screen to be visible beneath PostDetailsScreen to allow animation similar to Apple's App of the Day (AppStore) animation: https://github.com/wix/react-native-navigation/issues/6431
// TODO: 6. Fix ScaleTypeCenterCrop hard-crash on Android: https://github.com/wix/react-native-navigation/issues/6517
// TODO: 7. Add bottomTabs animation support so it slides out nicely (translateY): https://github.com/wix/react-native-navigation/issues/6340 and https://github.com/wix/react-native-navigation/issues/6567
// TODO: 8. Add topBar animation support so it slides out nicely (translateY): (no issue for that yet?)

const PostsListScreen: NavigationFunctionComponent = ({ componentId }) => {
  const onPostPressed = useCallback(
    async (post: PostItem) => {
      const navigationAnimations = await buildSharedElementAnimations(post);
      Navigation.push(componentId, {
        component: {
          name: Screens.PostDetailsScreen,
          passProps: { post: post },
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
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {posts.map((p) => (
          <PostCard
            key={p.id}
            parentComponentId={componentId}
            onPostPressed={() => onPostPressed(p)}
            post={p}
          />
        ))}
      </ScrollView>
    </SafeAreaView>
  );
};

PostsListScreen.options = {
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

export default PostsListScreen;

const styles = StyleSheet.create({
  scrollContent: {
    paddingVertical: 25,
  },
});
