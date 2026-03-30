import { forEach, keys, map } from 'lodash';

import MyIcon from './MyIcon';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { Icon } from 'react-native-vector-icons/Icon';

const iconsMap: {
  [key: string]: number;
} = {
  book: MyIcon.getImageSourceSync('book', 30, '#888'),
  gear: MyIcon.getImageSourceSync('gear', 30, '#888'),
  'arrow-back': MaterialIcons.getImageSourceSync('arrow-back', 24, '#888'),
  add: MaterialIcons.getImageSourceSync('add', 28, '#888'),
};

export {
  iconsMap,
};


