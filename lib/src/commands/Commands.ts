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

  public dismissModal(id) {
    return this.nativeCommandsSender.dismissModal(id);
  }

  public dismissAllModals() {
    return this.nativeCommandsSender.dismissAllModals();
  }

  public push(onComponentId, componentData) {
    const input = _.cloneDeep(componentData);
    this.optionsProcessor.processOptions(input);
    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);
    return this.nativeCommandsSender.push(onComponentId, layout);
  }

  public pop(componentId, options) {
    return this.nativeCommandsSender.pop(componentId, options);
  }

  public popTo(componentId) {
    return this.nativeCommandsSender.popTo(componentId);
  }

  public popToRoot(componentId) {
    return this.nativeCommandsSender.popToRoot(componentId);
  }

  public showOverlay(componentData) {
    const input = _.cloneDeep(componentData);
    this.optionsProcessor.processOptions(input);

    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);

    return this.nativeCommandsSender.showOverlay(layout);
  }

  public dismissOverlay(componentId) {
    return this.nativeCommandsSender.dismissOverlay(componentId);
  }
}
