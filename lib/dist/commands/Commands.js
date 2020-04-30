"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const cloneDeep_1 = tslib_1.__importDefault(require("lodash/cloneDeep"));
const map_1 = tslib_1.__importDefault(require("lodash/map"));
class Commands {
    constructor(store, nativeCommandsSender, layoutTreeParser, layoutTreeCrawler, commandsObserver, uniqueIdProvider, optionsProcessor) {
        this.store = store;
        this.nativeCommandsSender = nativeCommandsSender;
        this.layoutTreeParser = layoutTreeParser;
        this.layoutTreeCrawler = layoutTreeCrawler;
        this.commandsObserver = commandsObserver;
        this.uniqueIdProvider = uniqueIdProvider;
        this.optionsProcessor = optionsProcessor;
    }
    setRoot(simpleApi) {
        const input = cloneDeep_1.default(simpleApi);
        const root = this.layoutTreeParser.parse(input.root);
        const modals = map_1.default(input.modals, (modal) => {
            return this.layoutTreeParser.parse(modal);
        });
        const overlays = map_1.default(input.overlays, (overlay) => {
            return this.layoutTreeParser.parse(overlay);
        });
        const commandId = this.uniqueIdProvider.generate('setRoot');
        this.commandsObserver.notify('setRoot', { commandId, layout: { root, modals, overlays } });
        this.layoutTreeCrawler.crawl(root);
        modals.forEach((modalLayout) => {
            this.layoutTreeCrawler.crawl(modalLayout);
        });
        overlays.forEach((overlayLayout) => {
            this.layoutTreeCrawler.crawl(overlayLayout);
        });
        const result = this.nativeCommandsSender.setRoot(commandId, { root, modals, overlays });
        return result;
    }
    setDefaultOptions(options) {
        const input = cloneDeep_1.default(options);
        this.optionsProcessor.processDefaultOptions(input);
        this.nativeCommandsSender.setDefaultOptions(input);
        this.commandsObserver.notify('setDefaultOptions', { options });
    }
    mergeOptions(componentId, options) {
        const input = cloneDeep_1.default(options);
        this.optionsProcessor.processOptions(input);
        this.nativeCommandsSender.mergeOptions(componentId, input);
        this.commandsObserver.notify('mergeOptions', { componentId, options });
    }
    updateProps(componentId, props) {
        const input = cloneDeep_1.default(props);
        this.store.updateProps(componentId, input);
        this.commandsObserver.notify('updateProps', { componentId, props });
    }
    showModal(layout) {
        const layoutCloned = cloneDeep_1.default(layout);
        const layoutNode = this.layoutTreeParser.parse(layoutCloned);
        const commandId = this.uniqueIdProvider.generate('showModal');
        this.commandsObserver.notify('showModal', { commandId, layout: layoutNode });
        this.layoutTreeCrawler.crawl(layoutNode);
        const result = this.nativeCommandsSender.showModal(commandId, layoutNode);
        return result;
    }
    dismissModal(componentId, mergeOptions) {
        const commandId = this.uniqueIdProvider.generate('dismissModal');
        const result = this.nativeCommandsSender.dismissModal(commandId, componentId, mergeOptions);
        this.commandsObserver.notify('dismissModal', { commandId, componentId, mergeOptions });
        return result;
    }
    dismissAllModals(mergeOptions) {
        const commandId = this.uniqueIdProvider.generate('dismissAllModals');
        const result = this.nativeCommandsSender.dismissAllModals(commandId, mergeOptions);
        this.commandsObserver.notify('dismissAllModals', { commandId, mergeOptions });
        return result;
    }
    push(componentId, simpleApi) {
        const input = cloneDeep_1.default(simpleApi);
        const layout = this.layoutTreeParser.parse(input);
        const commandId = this.uniqueIdProvider.generate('push');
        this.commandsObserver.notify('push', { commandId, componentId, layout });
        this.layoutTreeCrawler.crawl(layout);
        const result = this.nativeCommandsSender.push(commandId, componentId, layout);
        return result;
    }
    pop(componentId, mergeOptions) {
        const commandId = this.uniqueIdProvider.generate('pop');
        const result = this.nativeCommandsSender.pop(commandId, componentId, mergeOptions);
        this.commandsObserver.notify('pop', { commandId, componentId, mergeOptions });
        return result;
    }
    popTo(componentId, mergeOptions) {
        const commandId = this.uniqueIdProvider.generate('popTo');
        const result = this.nativeCommandsSender.popTo(commandId, componentId, mergeOptions);
        this.commandsObserver.notify('popTo', { commandId, componentId, mergeOptions });
        return result;
    }
    popToRoot(componentId, mergeOptions) {
        const commandId = this.uniqueIdProvider.generate('popToRoot');
        const result = this.nativeCommandsSender.popToRoot(commandId, componentId, mergeOptions);
        this.commandsObserver.notify('popToRoot', { commandId, componentId, mergeOptions });
        return result;
    }
    setStackRoot(componentId, children) {
        const input = map_1.default(cloneDeep_1.default(children), (simpleApi) => {
            const layout = this.layoutTreeParser.parse(simpleApi);
            return layout;
        });
        const commandId = this.uniqueIdProvider.generate('setStackRoot');
        this.commandsObserver.notify('setStackRoot', { commandId, componentId, layout: input });
        input.forEach((layoutNode) => {
            this.layoutTreeCrawler.crawl(layoutNode);
        });
        const result = this.nativeCommandsSender.setStackRoot(commandId, componentId, input);
        return result;
    }
    showOverlay(simpleApi) {
        const input = cloneDeep_1.default(simpleApi);
        const layout = this.layoutTreeParser.parse(input);
        const commandId = this.uniqueIdProvider.generate('showOverlay');
        this.commandsObserver.notify('showOverlay', { commandId, layout });
        this.layoutTreeCrawler.crawl(layout);
        const result = this.nativeCommandsSender.showOverlay(commandId, layout);
        return result;
    }
    dismissOverlay(componentId) {
        const commandId = this.uniqueIdProvider.generate('dismissOverlay');
        const result = this.nativeCommandsSender.dismissOverlay(commandId, componentId);
        this.commandsObserver.notify('dismissOverlay', { commandId, componentId });
        return result;
    }
    getLaunchArgs() {
        const commandId = this.uniqueIdProvider.generate('getLaunchArgs');
        const result = this.nativeCommandsSender.getLaunchArgs(commandId);
        this.commandsObserver.notify('getLaunchArgs', { commandId });
        return result;
    }
}
exports.Commands = Commands;
