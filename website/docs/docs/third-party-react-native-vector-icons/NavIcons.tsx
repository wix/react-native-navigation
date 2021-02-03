import { forEach, keys, map } from 'lodash';

import MyIcon from './MyIcon';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { Icon } from 'react-native-vector-icons/Icon';

const icons: {
  [iconName: string]: [number, string, typeof Icon];
} = {
  book: [30, '#888', MyIcon],
  gear: [30, '#888', MyIcon], 
  'arrow-back': [24, '#888', MaterialIcons],
  add: [28, '#888', MaterialIcons],
};

const iconsMap: {
  [key: string]: number;
} = {};

const iconsLoaded = new Promise((resolve) => {
  Promise.all(
    map(icons, ([size, color, Provider], iconName) => {
      return Provider.getImageSource(iconName, size, color);
    })
  ).then(sources => {
    forEach(keys(icons), (iconName, idx) => {
      iconsMap[iconName] = sources[idx];
    });

    // Call resolve (and we are done)
    resolve(true);
  });
});

export {
  iconsMap,
  iconsLoaded,
};


