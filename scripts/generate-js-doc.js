const jsdoc2md = require('jsdoc-to-markdown');
const fs = require('fs');
const path = require('path');

const paramsDir = './lib/src/params/';
const optionsDir = './lib/src/params/options/';
const OUTPUT_DIR = './docs/docs/';
const partial = ['./docs/templates/scope.hbs', './docs/templates/docs.hbs'];

const generateMarkdownForFile = ({ file, outputDir }) => {
  const templateData = jsdoc2md.getTemplateDataSync({ files: file });
  const classNames = getClassesInFile(templateData);
  classNames.forEach((className) => createDocFileForClass(className, templateData, outputDir));
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

function createDocFileForClass(className, templateData, outputDir) {
  const template = `{{#class name="${className}"}}{{>docs}}{{/class}}`;
  const options = {
    data: templateData,
    template,
    separators: true,
    partial
  };
  console.log(`rendering ${className}, template: ${template} ${outputDir}`);
  const output = jsdoc2md.renderSync(options);
  fs.writeFileSync(path.resolve(outputDir, `${className}.md`), output);
}

function inputFiles() {
  return [
    { file: './lib/src/Navigation.js', outputDir: OUTPUT_DIR },
    ...fs.readdirSync(optionsDir).map((file) => {
      return {
        file: optionsDir + file,
        outputDir: OUTPUT_DIR + 'options/'
      };
    }),
    ...fs.readdirSync(paramsDir)
        .filter((file) => fs.statSync(paramsDir + file).isFile())
        .map((file) => {
          return {
            file: paramsDir + file,
            outputDir: OUTPUT_DIR
          };
        })
  ];
}

inputFiles().forEach((inputFile) => generateMarkdownForFile(inputFile));
