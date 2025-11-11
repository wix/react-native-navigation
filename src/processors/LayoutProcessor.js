"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.LayoutProcessor = void 0;
class LayoutProcessor {
    layoutProcessorsStore;
    constructor(layoutProcessorsStore) {
        this.layoutProcessorsStore = layoutProcessorsStore;
    }
    process(layout, commandName) {
        const processors = this.layoutProcessorsStore.getProcessors();
        processors.forEach((processor) => {
            layout = processor(layout, commandName);
        });
        return layout;
    }
}
exports.LayoutProcessor = LayoutProcessor;
