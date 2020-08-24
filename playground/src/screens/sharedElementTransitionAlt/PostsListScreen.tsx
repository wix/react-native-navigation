import React, { useCallback } from 'react';
import { Platform, SafeAreaView, ScrollView, TouchableOpacity } from 'react-native';
import { NavigationFunctionComponent } from 'react-native-navigation';
import posts, { PostItem } from '../../assets/posts';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import PostCard from './PostCard';

const MULTIPLIER = 1.15;
const POP_MULTIPLIER = 1.15;
const LONG_DURATION = 350 * MULTIPLIER;

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
                    duration: LONG_DURATION,
                  },
                },
                sharedElementTransitions: [
                  {
                    fromId: `image${post.id}`,
                    toId: `image${post.id}Dest`,
                    duration: LONG_DURATION,
                    interpolation: 'spring',
                  },
                  {
                    fromId: `title${post.id}`,
                    toId: `title${post.id}Dest`,
                    duration: LONG_DURATION,
                    interpolation: 'spring',
                  },
                ],
              },
              pop: {
                content: {
                  alpha: {
                    from: 1,
                    to: 0,
                    duration: LONG_DURATION * POP_MULTIPLIER,
                  },
                },
                sharedElementTransitions: [
                  {
                    fromId: `image${post.id}Dest`,
                    toId: `image${post.id}`,
                    duration: LONG_DURATION * POP_MULTIPLIER,
                    interpolation: 'spring',
                  },
                  {
                    fromId: `title${post.id}Dest`,
                    toId: `title${post.id}`,
                    duration: LONG_DURATION * POP_MULTIPLIER,
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
      <ScrollView>
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
