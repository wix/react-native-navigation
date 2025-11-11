import type { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import type { OptionsProcessor } from '../interfaces/Processors';

export class OptionProcessorsStore {
  private optionsProcessorsByObjectPath: Record<string, OptionsProcessor<any>[]> = {};

  public addProcessor<T>(
    optionPath: string,
    processor: OptionsProcessor<T>
  ): ProcessorSubscription {
    if (!this.optionsProcessorsByObjectPath[optionPath])
      this.optionsProcessorsByObjectPath[optionPath] = [];

    const processors = this.optionsProcessorsByObjectPath[optionPath];
    if (!processors) {
      throw new Error(`Processors array not initialized for ${optionPath}`);
    }
    processors.push(processor);

    return { remove: () => this.removeProcessor(optionPath, processor) };
  }

  public getProcessors(optionPath: string) {
    return this.optionsProcessorsByObjectPath[optionPath];
  }

  private removeProcessor(optionPath: string, processor: OptionsProcessor<any>) {
    const processors = this.optionsProcessorsByObjectPath[optionPath];
    if (!processors) return;
    processors.splice(processors.indexOf(processor), 1);
  }
}
