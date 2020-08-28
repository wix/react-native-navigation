import React, { useCallback } from 'react';
import { Platform, SafeAreaView, ScrollView, StyleSheet, TouchableOpacity } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';
import posts, { PostItem } from '../../assets/posts';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import PostCard from './PostCard';

const SET_DURATION = 500;

const PostsListScreen: NavigationFunctionComponent = (props) => {
  const onPostPressed = useCallback(
    (post: PostItem) => {
      Navigation.push(props.componentId, {
        component: {
          name: Screens.PostDetailsScreen,
          passProps: { post: post },
          options: {
            animations: {
              push: {
                content: {
                  alpha: {
                    from: 0,
                    to: 1,
                    duration: SET_DURATION,
                  },
                },
                sharedElementTransitions: [
                  {
                    fromId: `image${post.id}`,
                    toId: `image${post.id}Dest`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                  {
                    fromId: `title${post.id}`,
                    toId: `title${post.id}Dest`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                ],
              },
              pop: {
                content: {
                  alpha: {
                    from: 1,
                    to: 0,
                    duration: SET_DURATION,
                  },
                },
                sharedElementTransitions: [
                  {
                    fromId: `image${post.id}Dest`,
                    toId: `image${post.id}`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                  {
                    fromId: `title${post.id}Dest`,
                    toId: `title${post.id}`,
                    duration: SET_DURATION,
                    interpolation: 'spring',
                  },
                ],
              },
            },
          },
        },
      });
    },
    [props.componentId]
  );

  return (
    <SafeAreaView>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {posts.map((p) => (
          <TouchableOpacity key={p.id} onPress={() => onPostPressed(p)}>
            <PostCard post={p} />
          </TouchableOpacity>
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
      text: 'Posts',
    },
  },
};

export default PostsListScreen;

const styles = StyleSheet.create({
  scrollContent: {
    paddingVertical: 25
  }
})
