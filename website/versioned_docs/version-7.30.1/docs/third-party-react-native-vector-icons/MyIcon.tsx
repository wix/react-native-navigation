import { createIconSetFromIcoMoon } from 'react-native-vector-icons';

// This file is a renamed "selection.json" file exported in the IcoMoon bundle
import myIconConfig from 'assets/myicon-config.json';

// Constructing that the font has been properly registered in each platfom build.
const MyIcon = createIconSetFromIcoMoon(myIconConfig);

export default MyIcon;

