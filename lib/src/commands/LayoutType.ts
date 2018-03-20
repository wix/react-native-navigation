export enum LayoutType {
  Component = 'Component',
  Stack = 'Stack',
  BottomTabs = 'BottomTabs',
  SideMenuRoot = 'SideMenuRoot',
  SideMenuCenter = 'SideMenuCenter',
  SideMenuLeft = 'SideMenuLeft',
  SideMenuRight = 'SideMenuRight',
  TopTabs = 'TopTabs',
  ExternalComponent = 'ExternalComponent'
}

export function isLayoutType(name: string): boolean {
  return !!LayoutType[name];
}
