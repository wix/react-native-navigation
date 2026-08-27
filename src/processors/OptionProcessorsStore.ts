import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { OptionsProcessor } from '../interfaces/Processors';

export class OptionProcessorsStore {
  private optionsProcessorsByObjectPath: Record<string, { processor: OptionsProcessor<any> }[]> =
    {};

  public addProcessor<T>(
    optionPath: string,
    processor: OptionsProcessor<T>
  ): ProcessorSubscription {
    if (!this.optionsProcessorsByObjectPath[optionPath])
      this.optionsProcessorsByObjectPath[optionPath] = [];

    const registration = { processor };
    const registrations = this.optionsProcessorsByObjectPath[optionPath];
    registrations.push(registration);

    return {
      remove: () => {
        const index = registrations.indexOf(registration);
        if (index !== -1) registrations.splice(index, 1);
      },
    };
  }

  public getProcessors(optionPath: string) {
    return this.optionsProcessorsByObjectPath[optionPath]?.map(({ processor }) => processor);
  }
}
