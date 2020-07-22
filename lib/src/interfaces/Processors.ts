import { Layout } from './Layout';

export interface LayoutProcessor {
  (layout: Layout<{}>, commandName: string): Layout<{}>;
}

export interface OptionsProcessor<T> {
  (value: T, commandName: string): T;
}
