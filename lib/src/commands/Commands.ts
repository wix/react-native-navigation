import cloneDeepWith from 'lodash/cloneDeepWith';
import cloneDeep from 'lodash/cloneDeep';
import map from 'lodash/map';
import { CommandsObserver } from '../events/CommandsObserver';
import { NativeCommandsSender } from '../adapters/NativeCommandsSender';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Options } from '../interfaces/Options';
import { Layout, LayoutRoot } from '../interfaces/Layout';
import { LayoutTreeParser } from './LayoutTreeParser';
import { LayoutTreeCrawler } from './LayoutTreeCrawler';
import { OptionsProcessor } from './OptionsProcessor';
import { Store } from '../components/Store';
import { LayoutProcessor } from '../processors/LayoutProcessor';
import { CommandName } from '../interfaces/CommandName';
import { OptionsCrawler } from './OptionsCrawler';

export class Commands {
  constructor(
    private readonly store: Store,
    private readonly nativeCommandsSender: NativeCommandsSender,
    private readonly layoutTreeParser: LayoutTreeParser,
    private readonly layoutTreeCrawler: LayoutTreeCrawler,
    private readonly commandsObserver: CommandsObserver,
    private readonly uniqueIdProvider: UniqueIdProvider,
    private readonly optionsProcessor: OptionsProcessor,
    private readonly layoutProcessor: LayoutProcessor,
    private readonly optionsCrawler: OptionsCrawler
  ) {}

  public setRoot(simpleApi: LayoutRoot) {
    const input = cloneLayout(simpleApi);
    this.optionsCrawler.crawl(input.root);
    const processedRoot = this.layoutProcessor.process(input.root, CommandName.SetRoot);
    const root = this.layoutTreeParser.parse(processedRoot);

    const modals = map(input.modals, (modal) => {
      this.optionsCrawler.crawl(modal);
      const processedModal = this.layoutProcessor.process(modal, CommandName.SetRoot);
      return this.layoutTreeParser.parse(processedModal);
    });

    const overlays = map(input.overlays, (overlay: any) => {
      this.optionsCrawler.crawl(overlay);
      const processedOverlay = this.layoutProcessor.process(overlay, CommandName.SetRoot);
      return this.layoutTreeParser.parse(processedOverlay);
    });

    const commandId = this.uniqueIdProvider.generate(CommandName.SetRoot);
    this.commandsObserver.notify(CommandName.SetRoot, {
      commandId,
      layout: { root, modals, overlays },
    });

    this.layoutTreeCrawler.crawl(root, CommandName.SetRoot);
    modals.forEach((modalLayout) => {
      this.layoutTreeCrawler.crawl(modalLayout, CommandName.SetRoot);
    });
    overlays.forEach((overlayLayout) => {
      this.layoutTreeCrawler.crawl(overlayLayout, CommandName.SetRoot);
    });

    const result = this.nativeCommandsSender.setRoot(commandId, { root, modals, overlays });
    return result;
  }

  public setDefaultOptions(options: Options) {
    const input = cloneDeep(options);
    this.optionsProcessor.processDefaultOptions(input, CommandName.SetDefaultOptions);

    this.nativeCommandsSender.setDefaultOptions(input);
    this.commandsObserver.notify(CommandName.SetDefaultOptions, { options });
  }

  public mergeOptions(componentId: string, options: Options) {
    const input = cloneDeep(options);
    this.optionsProcessor.processOptions(CommandName.MergeOptions, input);

    const component = this.store.getComponentInstance(componentId);
    if (component && !component.isMounted)
      console.warn(
        `Navigation.mergeOptions was invoked on component with id: ${componentId} before it is mounted, this can cause UI issues and should be avoided.\n Use static options instead.`
      );

    this.nativeCommandsSender.mergeOptions(componentId, input);
    this.commandsObserver.notify(CommandName.MergeOptions, { componentId, options });
  }

  public updateProps(componentId: string, props: object, callback?: () => void) {
    this.store.updateProps(componentId, props, callback);
    this.commandsObserver.notify(CommandName.UpdateProps, { componentId, props });
  }

  public showModal(layout: Layout) {
    const layoutCloned = cloneLayout(layout);
    this.optionsCrawler.crawl(layoutCloned);
    const layoutProcessed = this.layoutProcessor.process(layoutCloned, CommandName.ShowModal);
    const layoutNode = this.layoutTreeParser.parse(layoutProcessed);

    const commandId = this.uniqueIdProvider.generate(CommandName.ShowModal);
    this.commandsObserver.notify(CommandName.ShowModal, { commandId, layout: layoutNode });
    this.layoutTreeCrawler.crawl(layoutNode, CommandName.ShowModal);

    const result = this.nativeCommandsSender.showModal(commandId, layoutNode);
    return result;
  }

  public dismissModal(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandName.DismissModal);
    this.optionsProcessor.processOptions(CommandName.DismissModal, mergeOptions);
    const result = this.nativeCommandsSender.dismissModal(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandName.DismissModal, {
      commandId,
      componentId,
      mergeOptions,
    });
    return result;
  }

  public dismissAllModals(mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandName.DismissAllModals);
    this.optionsProcessor.processOptions(CommandName.DismissAllModals, mergeOptions);
    const result = this.nativeCommandsSender.dismissAllModals(commandId, mergeOptions);
    this.commandsObserver.notify(CommandName.DismissAllModals, { commandId, mergeOptions });
    return result;
  }

  public push(componentId: string, simpleApi: Layout) {
    const input = cloneLayout(simpleApi);
    this.optionsCrawler.crawl(input);
    const layoutProcessed = this.layoutProcessor.process(input, CommandName.Push);
    const layout = this.layoutTreeParser.parse(layoutProcessed);

    const commandId = this.uniqueIdProvider.generate(CommandName.Push);
    this.commandsObserver.notify(CommandName.Push, { commandId, componentId, layout });
    this.layoutTreeCrawler.crawl(layout, CommandName.Push);

    const result = this.nativeCommandsSender.push(commandId, componentId, layout);
    return result;
  }

  public pop(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandName.Pop);
    this.optionsProcessor.processOptions(CommandName.Pop, mergeOptions);
    const result = this.nativeCommandsSender.pop(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandName.Pop, { commandId, componentId, mergeOptions });
    return result;
  }

  public popTo(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandName.PopTo);
    this.optionsProcessor.processOptions(CommandName.PopTo, mergeOptions);
    const result = this.nativeCommandsSender.popTo(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandName.PopTo, { commandId, componentId, mergeOptions });
    return result;
  }

  public popToRoot(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandName.PopToRoot);
    this.optionsProcessor.processOptions(CommandName.PopToRoot, mergeOptions);
    const result = this.nativeCommandsSender.popToRoot(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandName.PopToRoot, { commandId, componentId, mergeOptions });
    return result;
  }

  public setStackRoot(componentId: string, children: Layout[]) {
    const input = map(cloneLayout(children), (simpleApi) => {
      this.optionsCrawler.crawl(simpleApi);
      const layoutProcessed = this.layoutProcessor.process(simpleApi, CommandName.SetStackRoot);
      const layout = this.layoutTreeParser.parse(layoutProcessed);
      return layout;
    });

    const commandId = this.uniqueIdProvider.generate(CommandName.SetStackRoot);
    this.commandsObserver.notify(CommandName.SetStackRoot, {
      commandId,
      componentId,
      layout: input,
    });
    input.forEach((layoutNode) => {
      this.layoutTreeCrawler.crawl(layoutNode, CommandName.SetStackRoot);
    });

    const result = this.nativeCommandsSender.setStackRoot(commandId, componentId, input);
    return result;
  }

  public showOverlay(simpleApi: Layout) {
    const input = cloneLayout(simpleApi);
    this.optionsCrawler.crawl(input);
    const layoutProcessed = this.layoutProcessor.process(input, CommandName.ShowOverlay);
    const layout = this.layoutTreeParser.parse(layoutProcessed);

    const commandId = this.uniqueIdProvider.generate(CommandName.ShowOverlay);
    this.commandsObserver.notify(CommandName.ShowOverlay, { commandId, layout });
    this.layoutTreeCrawler.crawl(layout, CommandName.ShowOverlay);

    const result = this.nativeCommandsSender.showOverlay(commandId, layout);
    return result;
  }

  public dismissOverlay(componentId: string) {
    const commandId = this.uniqueIdProvider.generate(CommandName.DismissOverlay);
    const result = this.nativeCommandsSender.dismissOverlay(commandId, componentId);
    this.commandsObserver.notify(CommandName.DismissOverlay, { commandId, componentId });
    return result;
  }

  public dismissAllOverlays() {
    const commandId = this.uniqueIdProvider.generate(CommandName.DismissAllOverlays);
    const result = this.nativeCommandsSender.dismissAllOverlays(commandId);
    this.commandsObserver.notify(CommandName.DismissAllOverlays, { commandId });
    return result;
  }

  public getLaunchArgs() {
    const commandId = this.uniqueIdProvider.generate(CommandName.GetLaunchArgs);
    const result = this.nativeCommandsSender.getLaunchArgs(commandId);
    this.commandsObserver.notify(CommandName.GetLaunchArgs, { commandId });
    return result;
  }
}

function cloneLayout<L>(layout: L): L {
  return cloneDeepWith(layout, (value, key) => {
    if (key === 'passProps' && typeof value === 'object' && value !== null) return { ...value };
  });
}
