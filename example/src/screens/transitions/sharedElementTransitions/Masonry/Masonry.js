import React from 'react';
import { StyleSheet, View, Text, PixelRatio, FlatList, Image, TouchableHighlight } from 'react-native';
import { Navigation, SharedElementTransition } from 'react-native-navigation';

import images from './images';

const ROW_HEIGHT = 650;
const COLS = 2;

const data = [
    {
        key: 1,
        images: [[2, 1, 3], [1, 3, 1, 1]],
    },
    {
        key: 2,
        images: [[1, 1, 1, 3], [3, 3]],
    },
    {
        key: 3,
        images: [[1, 2, 1, 2], [2, 1, 2, 1]],
    }
];

class Masonry extends React.Component {

    onAssetPress = (image, key) => {
        this.props.navigator.push({
            screen: 'example.Transitions.SharedElementTransitions.Masonry.Item',
            sharedElements: [key],
            passProps: {
                image,
                sharedImageId: key,
            },
        });
    };

    renderAsset = (asset, row, column, index) => {
        const key = `row_${row}_column_${column}_asset_${index}`;
        const image = images[Math.floor(Math.random() * images.length)];

        return (
            <TouchableHighlight
                key={key}
                onPress={() => {
                    this.onAssetPress(image, key);
                }}
                style={[styles.assetContainer, { flex: asset }]}
            >
                <View style={{ flex: 1 }}>
                    <SharedElementTransition
                        style={{ flex: 1 }}
                        sharedElementId={key}
                    >
                        <Image
                            source={image}
                            resizeMode={'cover'}
                            style={styles.asset}
                        />
                    </SharedElementTransition>
                </View>
            </TouchableHighlight>
        );
    };

    renderItem = ({ item, index }) => {
        return (
            <View style={[styles.item, { height: ROW_HEIGHT }]}>
                {[...new Array(COLS)].map((column, columnIndex) => (
                    <View
                        key={`row_${index}_column_${columnIndex}`}
                        style={{ flex: 1 }}
                    >
                        {item.images[columnIndex].map((asset, assetIndex) => this.renderAsset(asset, index, columnIndex, assetIndex))}
                    </View>
                ))}
            </View>
        );
    };

    render() {
        return (
            <View style={styles.container}>
                <FlatList
                    data={data}
                    renderItem={this.renderItem}
                    getItemLayout={(layout, index) => ({ length: ROW_HEIGHT, offset: ROW_HEIGHT * index, index })}
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
    asset: {
        flex: 1,
        borderRadius: 6,
    },
});

export default Masonry;
