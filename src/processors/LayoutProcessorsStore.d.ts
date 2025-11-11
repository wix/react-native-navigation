import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { LayoutProcessor } from '../interfaces/Processors';
export declare class LayoutProcessorsStore {
    private layoutProcessors;
    addProcessor(processor: LayoutProcessor): ProcessorSubscription;
    getProcessors(): LayoutProcessor[];
    private removeProcessor;
}
