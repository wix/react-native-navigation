pipeline {
  agent any
  stages {
    stage('Install npm packages') {
      steps {
        sh '''#!/bin/bash -ex
npm install
npm run clean'''
      }
    }
    stage('Run test-js') {
      parallel {
        stage('Run test-js') {
          steps {
            sh '''#!/bin/bash

npm run test-js'''
          }
        }
        stage('Run IOS tests') {
          steps {
            sh '''#!/bin/bash

npm run test-unit-ios -- --release
npm run test-e2e-ios -- --release'''
          }
        }
        stage('Run android tests') {
          steps {
            sh '''#!/bin/bash

npm run test-unit-android -- --release
npm run test-e2e-android -- --release'''
          }
        }
      }
    }
  }
}

