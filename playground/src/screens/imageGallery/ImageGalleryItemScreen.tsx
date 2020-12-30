import React from 'react';
import { StyleSheet, View, Image, ImageSourcePropType } from 'react-native';

interface Props {
  image: ImageSourcePropType;
  sharedElementId: string;
}

export default class ImageGalleryItemScreen extends React.Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <Image
          // @ts-ignore nativeID isn't included in react-native Image props.
          nativeID={`image-${this.props.sharedElementId}Dest`}
          style={styles.image}
          source={this.props.image}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ffffff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  image: {
    width: 400,
    height: 400,
  },
});
