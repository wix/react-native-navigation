const _ = require('lodash');
const OptionsProcessor = require('./OptionsProcessor');

class Commands {
  constructor(nativeCommandsSender, layoutTreeParser, layoutTreeCrawler) {
    this.nativeCommandsSender = nativeCommandsSender;
    this.layoutTreeParser = layoutTreeParser;
    this.layoutTreeCrawler = layoutTreeCrawler;
  }

  setRoot(simpleApi) {
    const input = _.cloneDeep(simpleApi);
    const layout = this.layoutTreeParser.parseFromSimpleJSON(input);
    this.layoutTreeCrawler.crawl(layout);
    return this.nativeCommandsSender.setRoot(layout);
  }

  setDefaultOptions(options) {
    const input = _.cloneDeep(options);
    OptionsProcessor.processOptions(input);
    this.nativeCommandsSender.setDefaultOptions(input);
  }

  setOptions(containerId, options) {
    const input = _.cloneDeep(options);
    OptionsProcessor.processOptions(input);
    this.nativeCommandsSender.setOptions(containerId, input);
  }

  showModal(simpleApi) {
    const input = _.cloneDeep(simpleApi);
    const layout = this.layoutTreeParser.parseFromSimpleJSON(input);
    this.layoutTreeCrawler.crawl(layout);
    return this.nativeCommandsSender.showModal(layout);
  }

  dismissModal(id) {
    return this.nativeCommandsSender.dismissModal(id);
  }

  dismissAllModals() {
    return this.nativeCommandsSender.dismissAllModals();
  }

  push(onContainerId, containerData) {
    const input = _.cloneDeep(containerData);
    const layout = this.layoutTreeParser.createContainer(input);
    this.layoutTreeCrawler.crawl(layout);
    return this.nativeCommandsSender.push(onContainerId, layout);
  }

  pop(containerId, options) {
    return this.nativeCommandsSender.pop(containerId, options);
  }

  popTo(containerId) {
    return this.nativeCommandsSender.popTo(containerId);
  }

  popToRoot(containerId) {
    return this.nativeCommandsSender.popToRoot(containerId);
  }

  showOverlay(type, options) {
    let promise;
    if (type === 'custom') {
      const layout = this.layoutTreeParser.createDialogContainer({ name: options });
      this.layoutTreeCrawler.crawl(layout);
      promise = this.nativeCommandsSender.showOverlay(type, layout);
    } else {
      const input = _.cloneDeep(options);
      OptionsProcessor.processOptions(input);
      promise = this.nativeCommandsSender.showOverlay(type, input);
    }
    return promise;
  }

  dismissOverlay() {
    return this.nativeCommandsSender.dismissOverlay();
  }
}

module.exports = Commands;
