import { processColor } from 'react-native';

export class ColorService {
  toNativeColor(inputColor: string) {
    return processColor(inputColor);
  }
}
