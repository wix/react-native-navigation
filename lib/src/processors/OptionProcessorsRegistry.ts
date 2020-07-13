export class OptionProcessorsRegistry {
  private optionsProcessorsByObjectPath: Record<
    string,
    (value: any, commandName: string) => any
  > = {};

  public registerProcessor(
    objectPath: string,
    processor: (value: any, commandName: string) => any
  ) {
    if (this.optionsProcessorsByObjectPath[objectPath])
      throw new Error('Registering multiple option processors are not allowed');
    else this.optionsProcessorsByObjectPath[objectPath] = processor;
  }

  public unregisterProcessor(objectPath: string) {
    delete this.optionsProcessorsByObjectPath[objectPath];
  }

  public getProcessor(objectPath: string) {
    return this.optionsProcessorsByObjectPath[objectPath];
  }
}
