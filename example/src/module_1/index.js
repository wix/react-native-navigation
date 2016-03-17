// need to import and register every screen we want to be pushable
import StyledScreen from './StyledScreen';
import ModalScreen from './ModalScreen';
import FirstTabScreen from './FirstTabScreen';
import SideMenu from './SideMenu';

//the names must be unique across the entire application. Scope them to the module/package to avoid conflicts
export default function() {
  Navigation.registerScreen('module_1.ModalScreen', () => ModalScreen);
  Navigation.registerScreen('module_1.StyledScreen', () => StyledScreen);
  Navigation.registerScreen('module_1.FirstTabScreen', () => FirstTabScreen);
  Navigation.registerScreen('module_1.SideMenu', () => SideMenu);

}