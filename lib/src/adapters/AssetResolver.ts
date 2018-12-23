import * as resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';
import { ImageRequireSource } from 'react-native';

export class AssetService {
  resolveFromRequire(value: ImageRequireSource) {
    return resolveAssetSource(value);
  }
}
