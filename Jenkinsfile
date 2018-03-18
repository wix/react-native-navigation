pipeline {
  agent any
  stages {
    stage('Install npm packages') {
      steps {
      ansiColor('xterm') {
        sh '''#!/bin/bash -ex
        npm install
        npm run clean'''
        }
      }
    }
    stage('Run All Tests') {
      parallel {
        stage('Run test-js') {
          steps {
            sh '''#!/bin/bash
            npm run test-js'''
          }
        }
        stage('Run iOS tests') {
          steps {
            sh '''#!/bin/bash
            npm run test-unit-ios -- --release
            npm run test-e2e-ios -- --release'''
          }
        }
        stage('Run Android tests') {
          steps {
            sh '''#!/bin/bash
            npm run test-unit-android -- --release'''
          }
        }
      }
    }
  }
}
