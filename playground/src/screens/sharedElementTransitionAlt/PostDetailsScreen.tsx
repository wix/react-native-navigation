import React from 'react';
import { Dimensions, Image, ScrollView, StyleSheet, Text, View } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import Navigation from '../../services/Navigation';
import { PostItem } from '../../assets/posts';

interface Props extends NavigationComponentProps, PostItem {}

export default class PostDetailsScreen extends React.Component<Props> {
  static options() {
    return {
      statusBar: {
        visible: false,
      },
      topBar: {
        visible: false,
      },
    };
  }

  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  render() {
    return (
      <View style={styles.container}>
        <Image
          source={this.props.image}
          // @ts-ignore nativeID isn't included in react-native Image props.
          nativeID={`image${this.props.id}Dest`}
          style={styles.headerImage}
          resizeMode="contain"
          fadeDuration={0}
        />
        <ScrollView>
          <Text style={styles.title} nativeID={`title${this.props.id}Dest`}>
            {this.props.name}
          </Text>
          <Text nativeID="description" style={styles.description}>
            {this.props.description}
          </Text>
        </ScrollView>
      </View>
    );
  }
}

const HEADER = 200;
const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  headerImage: {
    height: HEADER,
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
