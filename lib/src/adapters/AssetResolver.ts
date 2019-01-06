import { ImageRequireSource, Image } from 'react-native';
import { Service } from 'typedi';

@Service()
export class AssetService {
  resolveFromRequire(value: ImageRequireSource) {
    return Image.resolveAssetSource(value);
  }
}
