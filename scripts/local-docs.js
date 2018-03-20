/* tslint:disable: no-console */
const http = require('http');
const fs = require('fs');

const PORT = 3000;
const ROOT = `${__dirname}/../docs`;

run();

function run() {
  http.createServer((req, res) => {
    console.log(req.url);
    const path = `${ROOT}${req.url === '/' ? '/index.html' : req.url}`;
    fs.readFile(path, (err, data) => {
      if (err) {
        console.error(err);
        res.writeHead(404, err.message);
      } else {
        res.writeHead(200, {
          'Content-Type': 'text/html',
          'Cache-Control': 'private, no-cache, no-store, must-revalidate',
          'Expires': '-1',
          'Pragma': 'no-cache'
        });
        res.write(data);
      }
      res.end();
    });
  }).listen(PORT, () => {
    console.log(`Listening for ${ROOT} on localhost:${PORT}`);
  });
}
