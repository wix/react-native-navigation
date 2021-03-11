import React from 'react';

import Image from '@theme/IdealImage';
import Layout from '@theme/Layout';

import clsx from 'clsx';
import styles from './styles.module.css';
import apps from '../../data/apps';

const TITLE = 'Showcase';
const DESCRIPTION = 'See the awesome websites people are building with Docusaurus';
const EDIT_URL = 'https://github.com/facebook/docusaurus/edit/master/website/src/data/users.js';

function Showcase() {
  return (
    <Layout title={TITLE} description={DESCRIPTION}>
      <main className="container margin-vert--lg">
        <div className="text--center margin-bottom--xl">
          <h1>{TITLE}</h1>
          <p>{DESCRIPTION}</p>
          <p>
            <a className={'button button--primary'} href={EDIT_URL} target={'_blank'}>
              Add your site!
            </a>
          </p>
        </div>
        <div className="row">
          {apps.map((app) => {
            return (
              <div key={app.title} className="col col--3 margin-bottom--lg card__container">
                <Image img={app.image} />
                <div className={'card__body'}>
                  <div className="avatar">
                    <div className="avatar__intro">
                      <h4 className="avatar__name">{app.title}</h4>
                      <small className="avatar__subtitle">{app.description}</small>
                    </div>
                  </div>
                  <div className="card__footer">
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
              // <div key={app.title} className="col col--4 margin-bottom--lg">
              //   <div className={clsx('card', styles.showcaseUser)}>
              //     <div className="card__image">
              //       <Image img={app.image} />
              //     </div>
              //     <div className="card__body">
              //       <div className="avatar">
              //         <div className="avatar__intro margin-left--none">
              //           <h4 className="avatar__name">{app.title}</h4>
              //           <small className="avatar__subtitle">{app.description}</small>
              //         </div>
              //       </div>
              //     </div>
              //     <div className="card__footer">
              //       <div className="button-group button-group--block">
              //         {app.appStore && (
              //           <a
              //             className="button button--small button--secondary button--block"
              //             href={app.appStore}
              //             target="_blank"
              //             rel="noreferrer noopener"
              //           >
              //             App Store
              //           </a>
              //         )}
              //         {app.playStore && (
              //           <a
              //             className="button button--small button--secondary button--block"
              //             href={app.playStore}
              //             target="_blank"
              //             rel="noreferrer noopener"
              //           >
              //             Play Store
              //           </a>
              //         )}
              //       </div>
              //     </div>
              //   </div>
              // </div>
            );
          })}
        </div>
      </main>
    </Layout>
  );
}

export default Showcase;
