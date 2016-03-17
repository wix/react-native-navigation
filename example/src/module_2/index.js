// need to import and register every screen we want to be pushable
import PushedScreen from './PushedScreen';
import SecondTabScreen from './SecondTabScreen';
import ThirdTabScreen from './ThirdTabScreen';


export default function () {
  //the names must be unique across the entire application. Scope them to the module/package to avoid conflicts
  Navigation.registerScreen('module_2.PushedScreen', () => PushedScreen);
  Navigation.registerScreen('module_2.ThirdTabScreen', () => ThirdTabScreen);
  Navigation.registerScreen('module_2.SecondTabScreen', () => SecondTabScreen);

}

