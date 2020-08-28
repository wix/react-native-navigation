import React, { useCallback, useMemo } from 'react';
import {
  Dimensions,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  Insets
} from 'react-native';
import {
  Navigation,
  NavigationFunctionComponent,
} from 'react-native-navigation';
import { PostItem } from '../../assets/posts';
import Reanimated, { useValue } from 'react-native-reanimated';

const HEADER_HEIGHT = 300;
const INDICATOR_INSETS: Insets = { top: HEADER_HEIGHT };

interface Props {
  post: PostItem;
}

const PostDetailsScreen: NavigationFunctionComponent<Props> = ({
  post,
  componentId,
}) => {
  const onClosePressed = useCallback(() => {
    Navigation.pop(componentId);
  }, [componentId]);

  const scrollY = useValue(0);
  const onScroll = useMemo(() => Reanimated.event([{ nativeEvent: { contentOffset: { y: scrollY } } }]), [scrollY]);

  const headerY = useMemo(() => Reanimated.interpolate(scrollY, {
    inputRange: [0, HEADER_HEIGHT],
    outputRange: [0, -HEADER_HEIGHT],
    extrapolateLeft: Reanimated.Extrapolate.CLAMP,
    extrapolateRight: Reanimated.Extrapolate.EXTEND
  }), [])
  const imageStyle = useMemo(() => [styles.headerImage, { transform: [{ translateY: headerY }] }], [headerY]);

  return (
    <View style={styles.container}>
      <Reanimated.Image
        source={post.image}
        // @ts-ignore nativeID isn't included in react-native Image props.
        nativeID={`image${post.id}Dest`}
        style={imageStyle}
        resizeMode="cover"
        fadeDuration={0}
      />
      <Reanimated.ScrollView
        contentContainerStyle={styles.content}
        onScroll={onScroll}
        scrollIndicatorInsets={INDICATOR_INSETS}>
        <Text style={styles.title} nativeID={`title${post.id}Dest`}>
          {post.name}
        </Text>
        <Text style={styles.description}>{post.description}</Text>
      </Reanimated.ScrollView>
      <TouchableOpacity style={styles.closeButton} onPress={onClosePressed}>
        <Text style={styles.closeButtonText}>x</Text>
      </TouchableOpacity>
    </View>
  );
};
PostDetailsScreen.options = {
  statusBar: {
    visible: false,
  },
  topBar: {
    visible: false,
  },
  bottomTabs: {
    visible: false
  }
};
export default PostDetailsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  headerImage: {
    position: 'absolute',
    height: HEADER_HEIGHT,
    width: Dimensions.get('window').width,
  },
  content: {
    paddingTop: HEADER_HEIGHT,
    paddingHorizontal: 25,
  },
  title: {
    fontSize: 32,
    marginTop: 30,
    fontWeight: '500',
    zIndex: 2,
  },
  description: {
    fontSize: 15,
    letterSpacing: 0.2,
    lineHeight: 25,
    marginTop: 32,
  },
  closeButton: {
    position: 'absolute',
    top: 50,
    right: 15,
    backgroundColor: 'rgba(0,0,0,0.5)',
    borderRadius: 15,
    width: 30,
    height: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
  closeButtonText: {
    fontWeight: 'bold',
    color: 'white',
    fontSize: 16,
  },
});
