import React from 'react';
import DocPage from '@theme/DocPage';
import StandWithUkraine from '@site/src/components/StandWithUkraine';

export default function CustomLayout(props) {
  return (
    <>
      <StandWithUkraine />
      <DocPage {...props} />
    </>
  );
}
