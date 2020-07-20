import { Layout } from './Layout';

export interface ExternalLayoutProcessor {
  (layout: Layout<{}>, commandName: string): Layout<{}>;
}

export interface ExternalOptionsProcessor<T> {
  (value: T, commandName: string): T;
}
