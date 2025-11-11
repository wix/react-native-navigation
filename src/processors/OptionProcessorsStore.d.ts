import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { OptionsProcessor } from '../interfaces/Processors';
export declare class OptionProcessorsStore {
    private optionsProcessorsByObjectPath;
    addProcessor<T>(optionPath: string, processor: OptionsProcessor<T>): ProcessorSubscription;
    getProcessors(optionPath: string): OptionsProcessor<any, any>[];
    private removeProcessor;
}
