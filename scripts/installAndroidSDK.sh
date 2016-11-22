#!/bin/sh -e

curl --location http://dl.google.com/android/android-sdk_r24.4.1-linux.tgz | tar -x -z -C $HOME

( sleep 5 && while [ 1 ]; do sleep 1; echo y; done ) | android update sdk --no-ui -a \
--filter \
tools,\
platform-tools,\
build-tools-25.0.0,\
android-23,\
addon-google_apis-google-23,\
extra-android-m2repository,\
extra-google-google_play_services,\
extra-google-m2repository
