"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.LayoutProcessorsStore = void 0;
class LayoutProcessorsStore {
    layoutProcessors = [];
    addProcessor(processor) {
        this.layoutProcessors.push(processor);
        return { remove: () => this.removeProcessor(processor) };
    }
    getProcessors() {
        return this.layoutProcessors;
    }
    removeProcessor(processor) {
        this.layoutProcessors.splice(this.layoutProcessors.indexOf(processor));
    }
}
exports.LayoutProcessorsStore = LayoutProcessorsStore;
