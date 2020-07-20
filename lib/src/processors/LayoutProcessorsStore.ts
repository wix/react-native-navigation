import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { ExternalLayoutProcessor } from '../interfaces/Processors';

export class LayoutProcessorsStore {
  private layoutProcessors: ExternalLayoutProcessor[] = [];

  public addProcessor(processor: ExternalLayoutProcessor): ProcessorSubscription {
    this.layoutProcessors.push(processor);

    return { remove: () => this.removeProcessor(processor) };
  }

  public getProcessors(): ExternalLayoutProcessor[] {
    return this.layoutProcessors;
  }

  private removeProcessor(processor: ExternalLayoutProcessor) {
    this.layoutProcessors.splice(this.layoutProcessors.indexOf(processor));
  }
}
