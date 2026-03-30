import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { OptionsProcessor } from '../interfaces/Processors';

export class OptionProcessorsStore {
  private optionsProcessorsByObjectPath: Record<string, OptionsProcessor<any>[]> = {};

  public addProcessor<T>(
    optionPath: string,
    processor: OptionsProcessor<T>
  ): ProcessorSubscription {
    if (!this.optionsProcessorsByObjectPath[optionPath])
      this.optionsProcessorsByObjectPath[optionPath] = [];

    this.optionsProcessorsByObjectPath[optionPath].push(processor);

    return { remove: () => this.removeProcessor(optionPath, processor) };
  }

  public getProcessors(optionPath: string) {
    return this.optionsProcessorsByObjectPath[optionPath];
  }

  private removeProcessor(optionPath: string, processor: OptionsProcessor<any>) {
    this.optionsProcessorsByObjectPath[optionPath].splice(
      this.optionsProcessorsByObjectPath[optionPath].indexOf(processor)
    );
  }
}
