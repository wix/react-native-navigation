import React from 'react';
import {
  StyleSheet,
  View,
  FlatList,
  Image,
  TouchableHighlight,
  ImageSourcePropType,
} from 'react-native';
import Navigation from '../../services/Navigation';

import images, { ImageGalleryAsset, ImageGalleryItem } from '../../assets/imageGallery';
import Screens from '../Screens';
import { NavigationComponent } from 'react-native-navigation';

const ROW_HEIGHT = 650;
const COLS = 2;
const MULTIPLIER = 1.2;
const LONG_DURATION = 540 * MULTIPLIER;
const SHORT_DURATION = 210 * MULTIPLIER;

export default class ImageGalleryListScreen extends NavigationComponent {
  onAssetPress = (image: ImageSourcePropType, key: string) => {
    Navigation.push(this, {
      component: {
        name: Screens.ImageGalleryItemScreen,
        passProps: {
          image,
          sharedElementId: key,
        },
        options: {
          animations: {
            push: {
              content: {
                alpha: {
                  from: 0,
                  to: 1,
                  duration: SHORT_DURATION,
                },
              },
              sharedElementTransitions: [
                {
                  fromId: `image-${key}`,
                  toId: `image-${key}Dest`,
                  duration: LONG_DURATION,
                },
              ],
            },
            pop: {
              content: {
                alpha: {
                  from: 1,
                  to: 0,
                  duration: SHORT_DURATION,
                },
              },
              sharedElementTransitions: [
                {
                  toId: `image-${key}`,
                  fromId: `image-${key}Dest`,
                  duration: LONG_DURATION,
                },
              ],
            },
          },
        },
      },
    });
  };

  renderAsset = (asset: ImageGalleryAsset, row: number, column: number, index: number) => {
    const key = `row_${row}_column_${column}_asset_${index}`;

    return (
      <TouchableHighlight
        key={key}
        onPress={() => {
          this.onAssetPress(asset.source, key);
        }}
        style={[styles.assetContainer, { flex: asset.weight }]}
      >
        <Image
          // @ts-ignore nativeID isn't included in react-native Image props.
          nativeID={`image-${key}`}
          source={asset.source}
          resizeMode={'cover'}
          style={styles.asset}
        />
      </TouchableHighlight>
    );
  };

  renderItem = ({ item, index }: { item: ImageGalleryItem; index: number }) => {
    return (
      <View key={`row_${index}`} style={[styles.item, { height: ROW_HEIGHT }]}>
        {[...new Array(COLS)].map((_column, columnIndex) => (
          <View key={`row_${index}_column_${columnIndex}`} style={styles.columnContainer}>
            {item.images[columnIndex].map((asset, assetIndex) =>
              this.renderAsset(asset, index, columnIndex, assetIndex)
            )}
          </View>
        ))}
      </View>
    );
  };

  render() {
    return (
      <View style={styles.container}>
        <FlatList
          data={images}
          renderItem={this.renderItem}
          keyExtractor={(item: ImageGalleryItem) => `${item.key}`}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ffffff',
  },
  item: {
    flex: 1,
    flexDirection: 'row',
  },
  assetContainer: {
    margin: 5,
    borderRadius: 6,
    borderWidth: StyleSheet.hairlineWidth,
  },
  columnContainer: {
    flex: 1,
  },
  asset: {
    width: '100%',
    height: '100%',
    borderRadius: 6,
  },
});
