import { LayoutProcessorsStore } from './LayoutProcessorsStore';
import { LayoutProcessor as ILayoutProcessor } from '../interfaces/Processors';
import { Layout } from '../interfaces/Layout';

export class LayoutProcessor {
  constructor(private layoutProcessorsStore: LayoutProcessorsStore) {}

  public process(layout: Layout, commandName: string): Layout {
    const processors: ILayoutProcessor[] = this.layoutProcessorsStore.getProcessors();
    processors.forEach((processor) => {
      layout = processor(layout, commandName);
    });

    return layout;
  }
}
