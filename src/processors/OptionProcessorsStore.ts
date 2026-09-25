import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { OptionsProcessor } from '../interfaces/Processors';

export class OptionProcessorsStore {
  private optionsProcessorsByObjectPath: Record<string, OptionsProcessor<any>[]> = Object.create(
    null
  );

  public addProcessor<T>(
    optionPath: string,
    processor: OptionsProcessor<T>
  ): ProcessorSubscription {
    if (!this.optionsProcessorsByObjectPath[optionPath])
      this.optionsProcessorsByObjectPath[optionPath] = [];

    this.optionsProcessorsByObjectPath[optionPath].push(processor);
    let removed = false;

    return {
      remove: () => {
        if (!removed) {
          this.removeProcessor(optionPath, processor);
          removed = true;
        }
      },
    };
  }

  public getProcessors(optionPath: string) {
    return this.optionsProcessorsByObjectPath[optionPath];
  }

  private removeProcessor(optionPath: string, processor: OptionsProcessor<any>) {
    const processors = this.optionsProcessorsByObjectPath[optionPath];
    const index = processors.indexOf(processor);
    if (index !== -1) processors.splice(index, 1);
  }
}
