const jsdoc2md = require('jsdoc-to-markdown');
const fs = require('fs');
const path = require('path');

const inputFiles = ['./lib/src/params/NavigationOptions.js', './lib/src/Navigation.js'];
const outputDir = './docs/docs/';
const partial = ['./docs/templates/scope.hbs', './docs/templates/docs.hbs'];

const generateMarkdownForFile = (file) => {
  const templateData = jsdoc2md.getTemplateDataSync({ files: file });
  const classNames = getClassesInFile(templateData);
  classNames.forEach((className) => createDocFileForClass(className, templateData));
};

function getClassesInFile(templateData) {
  const classNames = templateData.reduce((classNames, identifier) => {
    if (identifier.kind === 'class') {
      classNames.push(identifier.name);
    }
    return classNames;
  }, []);
  return classNames;
}

function createDocFileForClass(className, templateData) {
  const template = `{{#class name="${className}"}}{{>docs}}{{/class}}`;
  const options = {
    data: templateData,
    template,
    separators: true,
    partial
  };
  console.log(`rendering ${className}, template: ${template}`);
  const output = jsdoc2md.renderSync(options);
  fs.writeFileSync(path.resolve(outputDir, `${className}.md`), output);
}

inputFiles.forEach((inputFile) => generateMarkdownForFile(inputFile));
