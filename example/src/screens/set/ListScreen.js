import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  TouchableWithoutFeedback,
  StyleSheet,
  ListView,
  Image
} from 'react-native';
import heroes from './heroes';
import {SharedElementTransition} from 'react-native-navigation';
import * as heroStyles from './styles';

const LOREM_IPSUM = 'Lorem ipsum dolor sit amet, ius ad pertinax oportere accommodare, an vix civibus corrumpit referrentur. Te nam case ludus inciderint, te mea facilisi adipiscing. Sea id integre luptatum. In tota sale consequuntur nec. Erat ocurreret mei ei. Eu paulo sapientem vulputate est, vel an accusam intellegam interesset. Nam eu stet pericula reprimique, ea vim illud modus, putant invidunt reprehendunt ne qui.';
const hashCode = function(str) {
  var hash = 15;
  for (var ii = str.length - 1; ii >= 0; ii--) {
    hash = ((hash << 5) - hash) + str.charCodeAt(ii);
  }
  return hash;
};

export default class ListScreen extends Component {
  static navigatorStyle = {
    ...heroStyles.navigatorStyle
  };

  static navigatorButtons = {
    rightButtons: [
      {
        title: 'Add',
        icon: require('../../../img/navicon_add.png'),
        id: 'add',
        showAsAction: 'always'
      },
      {
        title: 'All',
        id: 'all',
        showAsAction: 'never'
      },
      {
        title: 'Upcoming',
        id: 'upcoming',
        showAsAction: 'never'
      },
      {
        title: 'My',
        id: 'my',
        showAsAction: 'never'
      },
      {
        title: 'Participant',
        id: 'participant',
        showAsAction: 'never'
      },
    ],
    fab: {
      collapsedId: 'share',
      collapsedIcon: require('../../../img/navicon_add.png'),
      backgroundColor: '#31363c'
    }
  };

  constructor(props) {
    super(props);
    this._onRowPress = this._onRowPress.bind(this);
    this._renderRow = this._renderRow.bind(this);
    this.props.navigator.setOnNavigatorEvent(this.onNavigatorEvent.bind(this));
    this.onPress = [];
    this.counter = 0;

    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
    this.state = {
      dataSource: ds.cloneWithRows(this._genRows({}))
    }
  }

  onNavigatorEvent(event) {
    console.log('ListScreen', 'onNavigatorEvent ', event.id);
    if (event.id === 'didAppear' && __STRESS_TEST__) {
      setTimeout(() => {
        this.onPress[Math.floor((Math.random() * 4))]();
        console.log('count', this.counter++);
      }, 750);
    }
  }

  render() {
    return (
      <ListView
        dataSource={this.state.dataSource}
        renderRow={(rowData, sectionID, rowID) => this._renderRow(rowData, sectionID, rowID)}/>
    );
  }

  _renderRow(rowData, sectionID, rowID) {
    console.log('ListScreen', '_renderRow ', rowID);
    const rowHash = Math.abs(hashCode(rowData.rowTitle));
    const sharedIconId = "image" + rowID;
    const sharedTitleId = "title" + rowID;

    const onPress = () => this._onRowPress({
      rowData,
      sharedIconId,
      sharedTitleId
    });
    if (['0', '1', '2', '3'].includes(rowID)) {
      this.onPress.push(onPress);
    }
    return (
      <TouchableWithoutFeedback
        style={styles.row}
        onPress={onPress}
      >
        <View style={[styles.sharedRowContent, heroStyles.primaryLight]}>
          {this._renderRowIcon({rowData, sharedIconId})}
          {this._renderRowContent({rowData, sharedTitleId, rowHash})}
        </View>
      </TouchableWithoutFeedback>
    );
  }

  _renderRowIcon({rowData, sharedIconId}) {
    return (
      <View>
        <SharedElementTransition
          key={sharedIconId}
          sharedElementId={sharedIconId}
          style={styles.imageContainer}
        >
          <Image
            source={rowData.icon}
            style={styles.heroIcon}
            fadeDuration={0}
          />
        </SharedElementTransition>
      </View>
    );
  }

  _renderRowContent({rowData, sharedTitleId, rowHash}) {
    return (
      <View style={styles.itemContentContainer}>
        <SharedElementTransition sharedElementId={sharedTitleId}>
          <Text style={styles.itemTitle}>{rowData.title}</Text>
        </SharedElementTransition>
        <Text style={styles.text}>
          {rowData.rowTitle + ' - ' + LOREM_IPSUM.substr(0, rowHash % 301 + 10)}
        </Text>
      </View>
    );
  }

  _onRowPress(data) {
    const {rowData, sharedIconId, sharedTitleId} = data;
    this.props.navigator.push({
      screen: 'example.HeroScreen',
      sharedElements: [
        data.sharedElementId,
        data.sharedElementId + 'container'
      ],
      animated: false,
      overrideBackPress: true,
      passProps: {
        sharedIconId: sharedIconId,
        sharedTitleId: sharedTitleId,
        ...rowData
      }
    });
  }

  _genRows() {
    const dataBlob = [];
    for (let ii = 0; ii < 100; ii++) {
      dataBlob.push({
        rowTitle: 'Row ' + ii + ' ',
        icon: heroes[ii % 5].icon,
        title: heroes[ii % 5].title,
        primaryColor: heroes[ii % 5].primaryColor,
        titleColor: heroes[ii % 5].titleColor
      });
    }
    return dataBlob;
  }

  _renderSeparator(sectionID, rowID) {
    return (
      <View
        key={`${sectionID}-${rowID}`}
        style={{
          height: 1,
          backgroundColor: rowID % 2 == 0 ? '#3B5998' : '#CCCCCC'
        }}
      />
    );
  }
}

const styles = StyleSheet.create({
  row: {
    padding: 5,
    height: 110,
    flexDirection: 'row',
    justifyContent: 'center',
    borderWidth: 1,
  },
  sharedRowContent: {
    flexDirection: 'row',
  },
  imageContainer: {
    justifyContent: 'center'
  },
  heroIcon: {
    width: 100,
    height: 100,
    resizeMode: 'contain'
  },
  itemContentContainer: {
    flex: 1,
    paddingLeft: 5,
    flexDirection: 'column',
    alignItems: 'flex-start'
  },
  itemTitle: {
    fontSize: 19,
    ...heroStyles.textDark
  },
  text: {
    ...heroStyles.textDark
  }
});


