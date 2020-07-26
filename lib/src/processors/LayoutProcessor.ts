import { LayoutProcessorsStore } from './LayoutProcessorsStore';
import { LayoutProcessor as ILayoutProcessor } from '../interfaces/Processors';
import { Layout } from '../interfaces/Layout';
import { CommandName } from '../interfaces/CommandName';

export class LayoutProcessor {
  constructor(private layoutProcessorsStore: LayoutProcessorsStore) {}

  public process(layout: Layout, commandName: CommandName): Layout {
    const processors: ILayoutProcessor[] = this.layoutProcessorsStore.getProcessors();
    processors.forEach((processor) => {
      layout = processor(layout, commandName);
    });

    return layout;
  }
}
