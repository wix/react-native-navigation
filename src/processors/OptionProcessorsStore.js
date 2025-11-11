"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.OptionProcessorsStore = void 0;
class OptionProcessorsStore {
    optionsProcessorsByObjectPath = {};
    addProcessor(optionPath, processor) {
        if (!this.optionsProcessorsByObjectPath[optionPath])
            this.optionsProcessorsByObjectPath[optionPath] = [];
        this.optionsProcessorsByObjectPath[optionPath].push(processor);
        return { remove: () => this.removeProcessor(optionPath, processor) };
    }
    getProcessors(optionPath) {
        return this.optionsProcessorsByObjectPath[optionPath];
    }
    removeProcessor(optionPath, processor) {
        this.optionsProcessorsByObjectPath[optionPath].splice(this.optionsProcessorsByObjectPath[optionPath].indexOf(processor));
    }
}
exports.OptionProcessorsStore = OptionProcessorsStore;
