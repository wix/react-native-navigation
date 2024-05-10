import React, { useCallback } from 'react';
import { StyleSheet, View, Text, Pressable, Image, ImageProps, useWindowDimensions, Dimensions } from 'react-native';
import { Navigation, NavigationFunctionComponent } from 'react-native-navigation';
import Reanimated from 'react-native-reanimated';

interface Props {
  source: ImageProps['source'];
  sharedElementId: string;
}

const size = Dimensions.get('window');

const ImageFullScreenViewer: NavigationFunctionComponent<Props> = ({
  source,
  sharedElementId,
  componentId,
}): React.ReactElement => {
  const onClose = useCallback(() => {
    Navigation.dismissModal(componentId);
  }, [componentId]);

  return (
    <View style={{ flex: 1 }}>
      <View style={styles.container} nativeID='bg' />
      <Image
        nativeID={sharedElementId}
        style={{ width: size.width, height: size.height, position: 'absolute' }}
        source={source}
        resizeMode="contain"
      />
      <Pressable onPress={onClose} style={styles.closeButton}>
        <Text style={styles.closeText}>x</Text>
      </Pressable>
    </View>
  );
};

export default ImageFullScreenViewer;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'black',
  },
  closeButton: {
    position: 'absolute',
    top: 50,
    right: 15,
    backgroundColor: 'white',
    borderRadius: 15,
    width: 30,
    height: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
  closeText: {
    color: 'black',
    fontWeight: 'bold',
  },
});
