import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { LayoutProcessor } from '../interfaces/Processors';

export class LayoutProcessorsStore {
  private layoutProcessors: LayoutProcessor[] = [];

  public addProcessor(processor: LayoutProcessor): ProcessorSubscription {
    this.layoutProcessors.push(processor);
    let removed = false;

    return {
      remove: () => {
        if (!removed) {
          this.removeProcessor(processor);
          removed = true;
        }
      },
    };
  }

  public getProcessors(): LayoutProcessor[] {
    return this.layoutProcessors;
  }

  private removeProcessor(processor: LayoutProcessor) {
    const index = this.layoutProcessors.indexOf(processor);
    if (index !== -1) this.layoutProcessors.splice(index, 1);
  }
}
