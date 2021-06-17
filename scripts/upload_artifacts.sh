#!/bin/bash -e

export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY
export AWS_SECRET_ACCESS_KEY=$AWS_ACCESS_SECRET

DATE=`date '+%Y-%m-%d_%H-%M-%S'`

if [ $JENKINS_CI ]; then
  ARTIFACTS_NAME="artifacts_${BUILD_ID}_${DATE}.tar.gz"
else
  ARTIFACTS_NAME="artifacts_${TRAVIS_BUILD_ID}_${DATE}.tar.gz"
fi

echo ${ARTIFACTS_NAME}

if [ -d "artifacts" ]; then
  echo "Creating ${ARTIFACTS_NAME}"
  tar cvzf ${ARTIFACTS_NAME} ./artifacts/

  if [ $JENKINS_CI ]; then
      echo "Jenkins has build in plugin"
  else
      aws s3 cp ${ARTIFACTS_NAME} s3://detox-artifacts/ --region=us-east-1
  fi

  echo "The artifacts archive is available for download at:"
  echo "https://detox-artifacts.s3.amazonaws.com/${ARTIFACTS_NAME}"
fi
