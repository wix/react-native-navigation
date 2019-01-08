import { processColor } from 'react-native';
import { Service } from 'typedi';

@Service('ColorService')
export class ColorService {
  toNativeColor(inputColor: string) {
    return processColor(inputColor);
  }
}
