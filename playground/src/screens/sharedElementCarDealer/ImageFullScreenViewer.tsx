import React, { useCallback } from 'react';
import { StyleSheet, View, Text, Pressable } from 'react-native';
import FastImage, { Source } from 'react-native-fast-image';
import { Navigation, NavigationFunctionComponent } from 'react-native-navigation';

interface Props {
  source: Source;
  sharedElementId: string;
}

const ImageFullScreenViewer: NavigationFunctionComponent<Props> = ({
  source,
  sharedElementId,
  componentId,
}): React.ReactElement => {
  const onClose = useCallback(() => {
    Navigation.dismissModal(componentId);
  }, [componentId]);

  return (
    <View style={styles.container}>
      <FastImage
        // @ts-ignore nativeID isn't included in FastImage props.
        nativeID={sharedElementId}
        style={StyleSheet.absoluteFill}
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
    alignItems: 'center',
    justifyContent: 'center',
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
