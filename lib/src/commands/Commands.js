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
    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);
    return this.nativeCommandsSender.setRoot(layout);
  }

  setDefaultOptions(options) {
    const input = _.cloneDeep(options);
    OptionsProcessor.processOptions(input);
    this.nativeCommandsSender.setDefaultOptions(input);
  }

  setOptions(componentId, options) {
    const input = _.cloneDeep(options);
    OptionsProcessor.processOptions(input);
    this.nativeCommandsSender.setOptions(componentId, input);
  }

  showModal(simpleApi) {
    const input = _.cloneDeep(simpleApi);
    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);
    return this.nativeCommandsSender.showModal(layout);
  }

  dismissModal(id) {
    return this.nativeCommandsSender.dismissModal(id);
  }

  dismissAllModals() {
    return this.nativeCommandsSender.dismissAllModals();
  }

  push(onComponentId, componentData) {
    const input = _.cloneDeep(componentData);
    OptionsProcessor.processOptions(input);
    const layout = this.layoutTreeParser.parse(input);
    this.layoutTreeCrawler.crawl(layout);
    return this.nativeCommandsSender.push(onComponentId, layout);
  }

  pop(componentId, options) {
    return this.nativeCommandsSender.pop(componentId, options);
  }

  popTo(componentId) {
    return this.nativeCommandsSender.popTo(componentId);
  }

  popToRoot(componentId) {
    return this.nativeCommandsSender.popToRoot(componentId);
  }

  // showOverlay(type, options) {
    // let promise;
    // if (type === 'custom') {
    //   const layout = this.layoutTreeParser.createDialogComponent({ name: options });
    //   this.layoutTreeCrawler.crawl(layout);
    //   promise = this.nativeCommandsSender.showOverlay(type, layout);
    // } else {
    //   const input = _.cloneDeep(options);
    //   OptionsProcessor.processOptions(input);
    //   promise = this.nativeCommandsSender.showOverlay(type, input);
    // }
    // return promise;
  // }

  dismissOverlay() {
    return this.nativeCommandsSender.dismissOverlay();
  }
}

module.exports = Commands;
