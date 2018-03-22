import * as _ from 'lodash';
import { OptionsProcessor } from './OptionsProcessor';
import { CommandsObserver } from '../events/CommandsObserver';

export class Commands {
  private optionsProcessor: OptionsProcessor;

  constructor(
    private readonly nativeCommandsSender,
    private readonly layoutTreeParser,
    private readonly layoutTreeCrawler,
    private readonly commandsObserver: CommandsObserver) {
    this.optionsProcessor = new OptionsProcessor(this.layoutTreeCrawler.store);
  }

  public setRoot(simpleApi) {
    const input = _.cloneDeep(simpleApi);
    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);

    const result = this.nativeCommandsSender.setRoot(layout);
    this.commandsObserver.notify('setRoot', { layout });
    return result;
  }

  public setDefaultOptions(options) {
    const input = _.cloneDeep(options);
    this.optionsProcessor.processOptions(input);

    this.nativeCommandsSender.setDefaultOptions(input);
    this.commandsObserver.notify('setDefaultOptions', { options });
  }

  public setOptions(componentId, options) {
    const input = _.cloneDeep(options);
    this.optionsProcessor.processOptions(input);

    this.nativeCommandsSender.setOptions(componentId, input);
    this.commandsObserver.notify('setOptions', { componentId, options });
  }

  public showModal(simpleApi) {
    const input = _.cloneDeep(simpleApi);
    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);

    const result = this.nativeCommandsSender.showModal(layout);
    this.commandsObserver.notify('showModal', { layout });
    return result;
  }

  public dismissModal(componentId) {
    const result = this.nativeCommandsSender.dismissModal(componentId);
    this.commandsObserver.notify('dismissModal', { componentId });
    return result;
  }

  public dismissAllModals() {
    const result = this.nativeCommandsSender.dismissAllModals();
    this.commandsObserver.notify('dismissAllModals', {});
    return result;
  }

  public push(componentId, simpleApi) {
    const input = _.cloneDeep(simpleApi);
    this.optionsProcessor.processOptions(input);
    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);

    const result = this.nativeCommandsSender.push(componentId, layout);
    this.commandsObserver.notify('push', { componentId, layout });
    return result;
  }

  public pop(componentId, options) {
    const result = this.nativeCommandsSender.pop(componentId, options);
    this.commandsObserver.notify('pop', { componentId, options });
    return result;
  }

  public popTo(componentId) {
    const result = this.nativeCommandsSender.popTo(componentId);
    this.commandsObserver.notify('popTo', { componentId });
    return result;
  }

  public popToRoot(componentId) {
    const result = this.nativeCommandsSender.popToRoot(componentId);
    this.commandsObserver.notify('popToRoot', { componentId });
    return result;
  }

  public showOverlay(simpleApi) {
    const input = _.cloneDeep(simpleApi);
    this.optionsProcessor.processOptions(input);

    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);

    const result = this.nativeCommandsSender.showOverlay(layout);
    this.commandsObserver.notify('showOverlay', { layout });
    return result;
  }

  public dismissOverlay(componentId) {
    const result = this.nativeCommandsSender.dismissOverlay(componentId);
    this.commandsObserver.notify('dismissOverlay', { componentId });
    return result;
  }
}
