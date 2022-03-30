import React from 'react';
import styles from './StandWithUkraine.module.css';
import Link from '@docusaurus/Link';

export default function StandWithUkraine() {
  return (
    <div className={styles.banner}>
      <Link to="https://stand-with-ukraine.pp.ua" className={styles.link}>
        🇺🇦 This project is created with substantial contributions from our Ukrainian colleagues.{' '}
        <span className={styles.hashtag}>#StandWithUkraine</span>
      </Link>
    </div>
  );
}
