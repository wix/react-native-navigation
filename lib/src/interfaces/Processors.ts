import { Layout } from './Layout';
import { CommandName } from './CommandName';

export interface LayoutProcessor {
  (layout: Layout<{}>, commandName: CommandName): Layout<{}>;
}

export interface OptionsProcessor<T> {
  (value: T, commandName: CommandName): T;
}
