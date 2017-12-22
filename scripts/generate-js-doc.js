const jsdoc2md = require('jsdoc-to-markdown');
const fs = require('fs');
const path = require('path');

const PARAMS_DIR = './lib/src/params/';
const OPTIONS_DIR = './lib/src/params/options/';
const OUTPUT_DIR = './docs/docs/';
const OPTION_PARTIALS = ['./docs/templates/header.hbs', './docs/templates/sig-name.hbs'];
const PARTIALS = ['./docs/templates/scope.hbs', './docs/templates/docs.hbs'];

const generateMarkdownForFile = ({ file, outputDir, partial }) => {
  const templateData = jsdoc2md.getTemplateDataSync({ files: file });
  const classNames = getClassesInFile(templateData);
  classNames.forEach((className) => createDocFileForClass({ className, templateData, outputDir, partial }));
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

function createDocFileForClass({ className, templateData, outputDir, partial = [] }) {
  const template = `{{#class name="${className}"}}{{>docs}}{{/class}}`;
  const options = {
    data: templateData,
    template,
    separators: true,
    partial: [...PARTIALS, ...partial]
  };
  console.log(`rendering ${className}`);
  const output = jsdoc2md.renderSync(options);
  fs.writeFileSync(path.resolve(outputDir, `${className}.md`), output);
}

function inputFiles() {
  return [
    { file: './lib/src/Navigation.js', outputDir: OUTPUT_DIR },
    ...fs.readdirSync(OPTIONS_DIR).map((file) => {
      return {
        file: OPTIONS_DIR + file,
        outputDir: OUTPUT_DIR + 'options/',
        partial: OPTION_PARTIALS
      };
    }),
    ...fs.readdirSync(PARAMS_DIR)
        .filter((file) => fs.statSync(PARAMS_DIR + file).isFile())
        .map((file) => {
          return {
            file: PARAMS_DIR + file,
            outputDir: OUTPUT_DIR
          };
        })
  ];
}

inputFiles().forEach((inputFile) => generateMarkdownForFile(inputFile));
