import { ProcessorSubscription } from 'react-native-navigation/interfaces/ProcessorSubscription';
import { ExternalLayoutProcessor } from 'react-native-navigation/interfaces/Processors';

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
