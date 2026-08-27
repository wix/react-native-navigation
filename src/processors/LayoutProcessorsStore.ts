import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { LayoutProcessor } from '../interfaces/Processors';

export class LayoutProcessorsStore {
  private layoutProcessors: { processor: LayoutProcessor }[] = [];

  public addProcessor(processor: LayoutProcessor): ProcessorSubscription {
    const registration = { processor };
    this.layoutProcessors.push(registration);

    return {
      remove: () => {
        const index = this.layoutProcessors.indexOf(registration);
        if (index !== -1) this.layoutProcessors.splice(index, 1);
      },
    };
  }

  public getProcessors(): LayoutProcessor[] {
    return this.layoutProcessors.map(({ processor }) => processor);
  }
}
