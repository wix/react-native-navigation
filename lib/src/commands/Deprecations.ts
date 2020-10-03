import once from 'lodash/once';
import { Platform } from 'react-native';

export class Deprecations {
  public onProcessOptions(key: string, parentOptions: Record<string, any>, commandName: string) {
    if (
      key === 'bottomTabs' &&
      parentOptions[key].visible !== undefined &&
      Platform.OS === 'ios' &&
      commandName === 'mergeOptions'
    ) {
      this.deprecateBottomTabsVisibility(parentOptions);
    }
  }

  public onProcessDefaultOptions(_key: string, _parentOptions: Record<string, any>) {}

  private deprecateBottomTabsVisibility = once((parentOptions: object) => {
    console.warn(
      `toggling bottomTabs visibility is deprecated on iOS. For more information see https://github.com/wix/react-native-navigation/issues/6416`,
      parentOptions
    );
  });
}
