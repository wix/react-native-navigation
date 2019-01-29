"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const _ = require("lodash");
class Commands {
    constructor(nativeCommandsSender, layoutTreeParser, layoutTreeCrawler, commandsObserver, uniqueIdProvider, optionsProcessor) {
        this.nativeCommandsSender = nativeCommandsSender;
        this.layoutTreeParser = layoutTreeParser;
        this.layoutTreeCrawler = layoutTreeCrawler;
        this.commandsObserver = commandsObserver;
        this.uniqueIdProvider = uniqueIdProvider;
        this.optionsProcessor = optionsProcessor;
    }
    setRoot(simpleApi) {
        const input = _.cloneDeep(simpleApi);
        const root = this.layoutTreeParser.parse(input.root);
        this.layoutTreeCrawler.crawl(root);
        const modals = _.map(input.modals, (modal) => {
            const modalLayout = this.layoutTreeParser.parse(modal);
            this.layoutTreeCrawler.crawl(modalLayout);
            return modalLayout;
        });
        const overlays = _.map(input.overlays, (overlay) => {
            const overlayLayout = this.layoutTreeParser.parse(overlay);
            this.layoutTreeCrawler.crawl(overlayLayout);
            return overlayLayout;
        });
        const commandId = this.uniqueIdProvider.generate('setRoot');
        const result = this.nativeCommandsSender.setRoot(commandId, { root, modals, overlays });
        this.commandsObserver.notify('setRoot', { commandId, layout: { root, modals, overlays } });
        return result;
    }
    setDefaultOptions(options) {
        const input = _.cloneDeep(options);
        this.optionsProcessor.processOptions(input);
        this.nativeCommandsSender.setDefaultOptions(input);
        this.commandsObserver.notify('setDefaultOptions', { options });
    }
    mergeOptions(componentId, options) {
        const input = _.cloneDeep(options);
        this.optionsProcessor.processOptions(input);
        this.nativeCommandsSender.mergeOptions(componentId, input);
        this.commandsObserver.notify('mergeOptions', { componentId, options });
    }
    showModal(layout) {
        const layoutCloned = _.cloneDeep(layout);
        const layoutNode = this.layoutTreeParser.parse(layoutCloned);
        this.layoutTreeCrawler.crawl(layoutNode);
        const commandId = this.uniqueIdProvider.generate('showModal');
        const result = this.nativeCommandsSender.showModal(commandId, layoutNode);
        this.commandsObserver.notify('showModal', { commandId, layout: layoutNode });
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
        const input = _.cloneDeep(simpleApi);
        const layout = this.layoutTreeParser.parse(input);
        this.layoutTreeCrawler.crawl(layout);
        const commandId = this.uniqueIdProvider.generate('push');
        const result = this.nativeCommandsSender.push(commandId, componentId, layout);
        this.commandsObserver.notify('push', { commandId, componentId, layout });
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
        const input = _.map(_.cloneDeep(children), (simpleApi) => {
            const layout = this.layoutTreeParser.parse(simpleApi);
            this.layoutTreeCrawler.crawl(layout);
            return layout;
        });
        const commandId = this.uniqueIdProvider.generate('setStackRoot');
        const result = this.nativeCommandsSender.setStackRoot(commandId, componentId, input);
        this.commandsObserver.notify('setStackRoot', { commandId, componentId, layout: input });
        return result;
    }
    showOverlay(simpleApi) {
        const input = _.cloneDeep(simpleApi);
        const layout = this.layoutTreeParser.parse(input);
        this.layoutTreeCrawler.crawl(layout);
        const commandId = this.uniqueIdProvider.generate('showOverlay');
        const result = this.nativeCommandsSender.showOverlay(commandId, layout);
        this.commandsObserver.notify('showOverlay', { commandId, layout });
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
