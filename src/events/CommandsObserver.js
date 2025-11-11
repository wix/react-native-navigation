"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CommandsObserver = void 0;
class CommandsObserver {
    uniqueIdProvider;
    listeners = {};
    constructor(uniqueIdProvider) {
        this.uniqueIdProvider = uniqueIdProvider;
    }
    register(listener) {
        const id = this.uniqueIdProvider.generate();
        this.listeners[id] = listener;
        return {
            remove: () => delete this.listeners[id],
        };
    }
    notify(commandName, params) {
        Object.values(this.listeners).forEach((listener) => listener(commandName, params));
    }
}
exports.CommandsObserver = CommandsObserver;
