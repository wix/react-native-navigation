import React from 'react';
import { Dimensions, Image, ScrollView, StyleSheet, Text, View } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import { PostItem } from '../../assets/posts';

interface Props extends NavigationComponentProps {
  post: PostItem;
}

export default function PostDetailsScreen(props: Props): JSX.Element {
  return (
    <View style={styles.container}>
      <Image
        source={props.post.image}
        // @ts-ignore nativeID isn't included in react-native Image props.
        nativeID={`image${props.id}Dest`}
        style={styles.headerImage}
        resizeMode="cover"
        fadeDuration={0}
      />
      <ScrollView>
        <Text style={styles.title} nativeID={`title${props.post.id}Dest`}>
          {props.post.name}
        </Text>
        <Text style={styles.description}>{props.post.description}</Text>
      </ScrollView>
    </View>
  );
}

PostDetailsScreen.options = {
  statusBar: {
    visible: false,
  },
  topBar: {
    visible: false,
  },
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  headerImage: {
    height: 300,
    width: Dimensions.get('window').width,
  },
  title: {
    fontSize: 32,
    color: 'whitesmoke',
    marginLeft: 16,
    marginBottom: 16,
    zIndex: 2,
  },
  description: {
    fontSize: 15,
    letterSpacing: 0.2,
    lineHeight: 25,
    marginTop: 32,
    marginHorizontal: 24,
  },
});
