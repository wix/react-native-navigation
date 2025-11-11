import { Image, ImageSourcePropType } from 'react-native';

export class AssetService {
  resolveFromRequire(value: ImageSourcePropType) {
    return Image.resolveAssetSource(value);
  }
}
