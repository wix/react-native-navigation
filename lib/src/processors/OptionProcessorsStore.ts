import { ProcessorSubscription } from 'react-native-navigation/interfaces/ProcessorSubscription';

export class OptionProcessorsStore {
  private optionsProcessorsByObjectPath: Record<
    string,
    ((value: any, commandName: string) => any)[]
  > = {};

  public addProcessor<T>(
    optionPath: string,
    processor: (value: T, commandName: string) => T
  ): ProcessorSubscription {
    if (!this.optionsProcessorsByObjectPath[optionPath])
      this.optionsProcessorsByObjectPath[optionPath] = [];

    this.optionsProcessorsByObjectPath[optionPath].push(processor);

    return { remove: () => this.removeProcessor(optionPath, processor) };
  }

  public getProcessors(optionPath: string) {
    return this.optionsProcessorsByObjectPath[optionPath];
  }

  private removeProcessor(optionPath: string, processor: (value: any, commandName: string) => any) {
    this.optionsProcessorsByObjectPath[optionPath].splice(
      this.optionsProcessorsByObjectPath[optionPath].indexOf(processor)
    );
  }
}
