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

enum CommandNames {
  SetRoot = 'setRoot',
  SetDefaultOptions = 'setDefaultOptions',
  MergeOptions = 'mergeOptions',
  UpdateProps = 'updateProps',
  ShowModal = 'showModal',
  DismissModal = 'dismissModal',
  DismissAllModals = 'dismissAllModals',
  Push = 'push',
  Pop = 'pop',
  PopTo = 'popTo',
  PopToRoot = 'popToRoot',
  SetStackRoot = 'setStackRoot',
  ShowOverlay = 'showOverlay',
  DismissOverlay = 'dismissOverlay',
  GetLaunchArgs = 'getLaunchArgs',
}

export class Commands {
  constructor(
    private readonly store: Store,
    private readonly nativeCommandsSender: NativeCommandsSender,
    private readonly layoutTreeParser: LayoutTreeParser,
    private readonly layoutTreeCrawler: LayoutTreeCrawler,
    private readonly commandsObserver: CommandsObserver,
    private readonly uniqueIdProvider: UniqueIdProvider,
    private readonly optionsProcessor: OptionsProcessor
  ) {}

  public setRoot(simpleApi: LayoutRoot) {
    const input = cloneDeep(simpleApi);
    const root = this.layoutTreeParser.parse(input.root);

    const modals = map(input.modals, (modal) => {
      return this.layoutTreeParser.parse(modal);
    });

    const overlays = map(input.overlays, (overlay) => {
      return this.layoutTreeParser.parse(overlay);
    });

    const commandId = this.uniqueIdProvider.generate(CommandNames.SetRoot);
    this.commandsObserver.notify(CommandNames.SetRoot, {
      commandId,
      layout: { root, modals, overlays },
    });

    this.layoutTreeCrawler.crawl(root, CommandNames.SetRoot);
    modals.forEach((modalLayout) => {
      this.layoutTreeCrawler.crawl(modalLayout, CommandNames.SetRoot);
    });
    overlays.forEach((overlayLayout) => {
      this.layoutTreeCrawler.crawl(overlayLayout, CommandNames.SetRoot);
    });

    const result = this.nativeCommandsSender.setRoot(commandId, { root, modals, overlays });
    return result;
  }

  public setDefaultOptions(options: Options) {
    const input = cloneDeep(options);
    this.optionsProcessor.processDefaultOptions(input, CommandNames.SetDefaultOptions);

    this.nativeCommandsSender.setDefaultOptions(input);
    this.commandsObserver.notify(CommandNames.SetDefaultOptions, { options });
  }

  public mergeOptions(componentId: string, options: Options) {
    const input = cloneDeep(options);
    this.optionsProcessor.processOptions(input, CommandNames.MergeOptions);

    this.nativeCommandsSender.mergeOptions(componentId, input);
    this.commandsObserver.notify(CommandNames.MergeOptions, { componentId, options });
  }

  public updateProps(componentId: string, props: object) {
    this.store.updateProps(componentId, props);
    this.commandsObserver.notify(CommandNames.UpdateProps, { componentId, props });
  }

  public showModal(layout: Layout) {
    const layoutCloned = cloneDeep(layout);
    const layoutNode = this.layoutTreeParser.parse(layoutCloned);

    const commandId = this.uniqueIdProvider.generate(CommandNames.ShowModal);
    this.commandsObserver.notify(CommandNames.ShowModal, { commandId, layout: layoutNode });
    this.layoutTreeCrawler.crawl(layoutNode, CommandNames.ShowModal);

    const result = this.nativeCommandsSender.showModal(commandId, layoutNode);
    return result;
  }

  public dismissModal(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandNames.DismissModal);
    const result = this.nativeCommandsSender.dismissModal(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandNames.DismissModal, {
      commandId,
      componentId,
      mergeOptions,
    });
    return result;
  }

  public dismissAllModals(mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandNames.DismissAllModals);
    const result = this.nativeCommandsSender.dismissAllModals(commandId, mergeOptions);
    this.commandsObserver.notify(CommandNames.DismissAllModals, { commandId, mergeOptions });
    return result;
  }

  public push(componentId: string, simpleApi: Layout) {
    const input = cloneDeep(simpleApi);
    const layout = this.layoutTreeParser.parse(input);

    const commandId = this.uniqueIdProvider.generate(CommandNames.Push);
    this.commandsObserver.notify(CommandNames.Push, { commandId, componentId, layout });
    this.layoutTreeCrawler.crawl(layout, CommandNames.Push);

    const result = this.nativeCommandsSender.push(commandId, componentId, layout);
    return result;
  }

  public pop(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandNames.Pop);
    const result = this.nativeCommandsSender.pop(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandNames.Pop, { commandId, componentId, mergeOptions });
    return result;
  }

  public popTo(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandNames.PopTo);
    const result = this.nativeCommandsSender.popTo(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandNames.PopTo, { commandId, componentId, mergeOptions });
    return result;
  }

  public popToRoot(componentId: string, mergeOptions?: Options) {
    const commandId = this.uniqueIdProvider.generate(CommandNames.PopToRoot);
    const result = this.nativeCommandsSender.popToRoot(commandId, componentId, mergeOptions);
    this.commandsObserver.notify(CommandNames.PopToRoot, { commandId, componentId, mergeOptions });
    return result;
  }

  public setStackRoot(componentId: string, children: Layout[]) {
    const input = map(cloneDeep(children), (simpleApi) => {
      const layout = this.layoutTreeParser.parse(simpleApi);
      return layout;
    });

    const commandId = this.uniqueIdProvider.generate(CommandNames.SetStackRoot);
    this.commandsObserver.notify(CommandNames.SetStackRoot, {
      commandId,
      componentId,
      layout: input,
    });
    input.forEach((layoutNode) => {
      this.layoutTreeCrawler.crawl(layoutNode, CommandNames.SetStackRoot);
    });

    const result = this.nativeCommandsSender.setStackRoot(commandId, componentId, input);
    return result;
  }

  public showOverlay(simpleApi: Layout) {
    const input = cloneDeep(simpleApi);
    const layout = this.layoutTreeParser.parse(input);

    const commandId = this.uniqueIdProvider.generate(CommandNames.ShowOverlay);
    this.commandsObserver.notify(CommandNames.ShowOverlay, { commandId, layout });
    this.layoutTreeCrawler.crawl(layout, CommandNames.ShowOverlay);

    const result = this.nativeCommandsSender.showOverlay(commandId, layout);
    return result;
  }

  public dismissOverlay(componentId: string) {
    const commandId = this.uniqueIdProvider.generate(CommandNames.DismissOverlay);
    const result = this.nativeCommandsSender.dismissOverlay(commandId, componentId);
    this.commandsObserver.notify(CommandNames.DismissOverlay, { commandId, componentId });
    return result;
  }

  public getLaunchArgs() {
    const commandId = this.uniqueIdProvider.generate(CommandNames.GetLaunchArgs);
    const result = this.nativeCommandsSender.getLaunchArgs(commandId);
    this.commandsObserver.notify(CommandNames.GetLaunchArgs, { commandId });
    return result;
  }
}
