import { LayoutProcessorsStore } from './LayoutProcessorsStore';
import { ExternalLayoutProcessor } from 'react-native-navigation/interfaces/Processors';
import { Layout } from '../interfaces/Layout';

export class LayoutProcessor {
  constructor(private layoutProcessorsStore: LayoutProcessorsStore) {}

  public process(layout: Layout, commandName: string): Layout {
    if (layout) {
      const processors: ExternalLayoutProcessor[] = this.layoutProcessorsStore.getProcessors();
      processors.forEach((processor) => {
        layout = processor(layout, commandName);
      });
    }

    return layout;
  }
}
