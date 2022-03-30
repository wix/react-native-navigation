import { Platform, PlatformColor } from 'react-native';

const colors = {
  background: { light: '#e8e8e8', dark: '#282528' },
  statusBarColor: { light: '#FFFFFF', dark: '#282528' },
  barBackground: { light: 'white', dark: '#282528' },
  primary: { light: '#5847ff', dark: '#BA292E' },
  secondary: { light: '#FFC249', dark: '#5847ff' },
  accent: { light: '#65C888', dark: '#FFA73C' },
  textColor:
    Platform.OS === 'ios'
      ? { light: '#5847ff', dark: '#BA292E' }
      : PlatformColor('@color/textColor'),
  activeTextColor: { light: '#5847ff', dark: '#FFA73C' },
  iconTint: { light: 'black', dark: '#BA292E' },
  activeIconTint: { light: '#5847ff', dark: '#FFA73C' },
  buttonColor: { light: 'black', dark: '#BA292E' },
  disabledButtonColor: { light: 'grey', dark: '#BA292E' },
};

export default colors;

export function hexToRgba(hex: string, a = 1): string {
  // Expand shorthand form (e.g. "03F") to full form (e.g. "0033FF")
  const shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
  hex = hex.replace(shorthandRegex, function (_, r, g, b) {
    return r + r + g + g + b + b;
  });

  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result
    ? `rgba(${parseInt(result[1], 16)}, ${parseInt(result[2], 16)}, ${parseInt(
        result[3],
        16
      )}, ${a})`
    : hex;
}
