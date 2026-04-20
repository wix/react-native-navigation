import { LayoutType } from '../commands/LayoutType';

export interface NavigationStateNode {
  id: string;
  type: LayoutType;
  name?: string;
  children: NavigationStateNode[];
  selectedIndex?: number;
}

export interface NavigationState {
  root: NavigationStateNode | null;
  modals: NavigationStateNode[];
  overlays: NavigationStateNode[];
}
