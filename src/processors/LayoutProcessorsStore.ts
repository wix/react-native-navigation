import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { LayoutProcessor } from '../interfaces/Processors';

export class LayoutProcessorsStore {
  private layoutProcessors: LayoutProcessor[] = [];

  public addProcessor(processor: LayoutProcessor): ProcessorSubscription {
    this.layoutProcessors.push(processor);

    return { remove: () => this.removeProcessor(processor) };
  }

  public getProcessors(): LayoutProcessor[] {
    return this.layoutProcessors;
  }

  private removeProcessor(processor: LayoutProcessor) {
    this.layoutProcessors.splice(this.layoutProcessors.indexOf(processor));
  }
}
