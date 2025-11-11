import { Image } from 'react-native';
import type { ImageSourcePropType } from 'react-native';

export class AssetService {
  resolveFromRequire(value: ImageSourcePropType) {
    return Image.resolveAssetSource(value);
  }
}
