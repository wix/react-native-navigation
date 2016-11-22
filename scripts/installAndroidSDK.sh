#!/bin/sh -e

# fix for https://code.google.com/p/android/issues/detail?id=223424
mkdir -p ~/.android

# download android SDK
echo "Downloading Android SDK"
curl --location http://dl.google.com/android/android-sdk_r24.4.1-linux.tgz | tar -x -z -C $HOME

# copy licenses
echo "Copying Android licenses"
scriptdir=`dirname $0`
cp "${scriptdir}"/android-licenses/* "${ANDROID_HOME}"/licenses
