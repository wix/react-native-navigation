import React, { useCallback } from 'react';
import {
  Dimensions,
  Image,
  ScrollView,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
} from 'react-native';
import {
  NavigationComponentProps,
  Navigation,
  NavigationFunctionComponent,
} from 'react-native-navigation';
import { PostItem } from '../../assets/posts';

interface Props extends NavigationComponentProps {
  post: PostItem;
}

const PostDetailsScreen: NavigationFunctionComponent = ({
  post,
  componentId,
}: Props): JSX.Element => {
  const onClosePressed = useCallback(() => {
    Navigation.pop(componentId);
  }, [componentId]);

  return (
    <View style={styles.container}>
      <Image
        source={post.image}
        // @ts-ignore nativeID isn't included in react-native Image props.
        nativeID={`image${post.id}Dest`}
        style={styles.headerImage}
        resizeMode="cover"
        fadeDuration={0}
      />
      <ScrollView contentContainerStyle={styles.content}>
        <Text style={styles.title} nativeID={`title${post.id}Dest`}>
          {post.name}
        </Text>
        <Text style={styles.description}>{post.description}</Text>
      </ScrollView>
      <TouchableOpacity style={styles.closeButton} onPress={onClosePressed}>
        <Text style={styles.closeButtonText}>x</Text>
      </TouchableOpacity>
    </View>
  );
};
PostDetailsScreen.options = {};
export default PostDetailsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  headerImage: {
    height: 300,
    width: Dimensions.get('window').width,
  },
  content: {
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
