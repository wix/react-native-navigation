"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class CommandsObserver {
    constructor(uniqueIdProvider) {
        this.uniqueIdProvider = uniqueIdProvider;
        this.listeners = {};
    }
    register(listener) {
        const id = this.uniqueIdProvider.generate();
        this.listeners[id] = listener;
        return {
            remove: () => delete this.listeners[id]
        };
    }
    notify(commandName, params) {
        Object.values(this.listeners).forEach((listener) => listener(commandName, params));
    }
}
exports.CommandsObserver = CommandsObserver;
