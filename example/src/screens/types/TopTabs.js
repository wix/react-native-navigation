import React from 'react';
import {PixelRatio} from 'react-native';

class TopTabs extends React.Component {
  static navigatorStyle = {
    topTabTextColor: '#ffffff',
    topTabTextFontFamily: 'BioRhyme-Bold',
    selectedTopTabTextColor: '#ff505c',

    // Icons
    topTabIconColor: '#ffffff',
    selectedTopTabIconColor: '#ff505c',

    // Tab indicator
    selectedTopTabIndicatorHeight: PixelRatio.get() * 2,
    selectedTopTabIndicatorColor: '#ff505c',
  };
}

export default TopTabs;
