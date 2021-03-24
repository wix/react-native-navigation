import React from 'react';

import Image from '@theme/IdealImage';
import Layout from '@theme/Layout';

// import clsx from 'clsx';
import styles from './styles.module.css';
import apps from '../../data/apps';

const TITLE = 'Showcase';
const DESCRIPTION = 'See the awesome apps people are building with React Native Navigation';
const EDIT_URL = 'https://forms.gle/pyP8w73hNUhoReQ3A';

function Showcase() {
  return (
    <Layout title={TITLE} description={DESCRIPTION}>
      <main className="margin-vert--lg">
        <div className="text--center margin-bottom--xl">
          <h1>{TITLE}</h1>
          <p>{DESCRIPTION}</p>
          <p>
            <a className={'button button--primary'} href={EDIT_URL} target={'_blank'}>
              Add your app!
            </a>
          </p>
        </div>
        <div className={styles.parent}>
          <div className={styles.root}>
            {apps.map((app) => {
              return (
                <div className={styles.cardContainer}>
                  <div className={styles.appIcon}>
                    <Image img={app.image} />
                  </div>
                  <div className={styles.content}>
                    <h4 className={styles.appName}>{app.title}</h4>
                    <p className={styles.appDescription}>{app.description}</p>
                    <div className={styles.cardFooter}>
                      <div className="button-group button-group--block">
                        {app.appStore && (
                          <a
                            className="button button--small button--secondary button--block"
                            href={app.appStore}
                            target="_blank"
                            rel="noreferrer noopener"
                          >
                            App Store
                          </a>
                        )}
                        {app.playStore && (
                          <a
                            className="button button--small button--secondary button--block"
                            href={app.playStore}
                            target="_blank"
                            rel="noreferrer noopener"
                          >
                            Play Store
                          </a>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </main>
    </Layout>
  );
}

export default Showcase;
