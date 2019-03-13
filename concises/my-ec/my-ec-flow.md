# Electrode app simplified running flow

<a id="top"></a>

## **Contents**

[**Express**](#1)
  + [express server start flow](#1-1)
  + [express server coming request flow](#1-2)

[**Hapi**](#2)
  + [hapi server start flow](#2-1)
  + [hapi server coming request flow](#2-2)


<a id="1"></a>

## Express

<a id="1-1"></a>

### express server start flow

`src/server/index.js:`
```js
require(“electrode-confippet”); // confippet requires a lot of config and defines the .config will refer to electrode-confippet/config.js

/** the config will load the `project/config/default.js`: { port: 3000, ui.demo: “Hello from ui config”, webapp.module: “electrode-react-webapp/lib/express”} */
startServer = require(“express-server”);
```
&rarr;
`src/server/express-server.js:`
{

  `startServer(confippet.config)` &rarr; the config somehow will load the `project/config/default.js: `

    {
      port: 3000,
      ui.demo: “Hello from ui config”,
      webapp.module: “electrode-react-webapp/lib/express”
    }
} &rarr; `electrode-react-webapp/lib/express/index.js:` {
  ```js
registerRoutes(app, options{insertTokenIds: true, path: *, pageTitle: express-app}, next){
  const registerOptions = ReactWebapp.setupOptions(options); // registerOptions.paths={[path]: {method,...},...}
  const handleRoute = opitons.handleRoute || DefaultHandleRoute // generally will use DefaultHandleRoute
  app[method.toLowerCase()](path, (req, res) => {/*register routes and methods*/});
}
  ```
} &rarr; `electrode-archetype-react-app-dev/lib/webpack-dev-express: ` {
  ```js
  devSetup(app, “http”, config.port)
  ```
} &rarr; `src/server/express-server.js: ` {
  ```js
  logger.log(“App listening on port: 3000”);
  ```
}

[back to top](#top)

<a id="1-2"></a>

### express server coming request flow

send request / or visit http://localhost:3000
`electrode-react-webapp/lib/express/index.js: ` {
```js
/*middleware within `registerRoutes` get the request */
app.METHOD(path, (req, res) => {
  ReactWebapp.getContentResolver(registerOptions, ...); // will call `ReactWebapp.resolveContent`
  handleRoute(req, res, routeHandler, content, routeOptions); // generally `handleRoute` is `DefaultHandleRoute`
})
```
&rarr; `electrode-react-webapp/lib/react-webapp.js: ` {
  ```js
  ReactWebapp.resolveContent("src/server/views/index-views.jsx");
  ```
The method `ReactWebapp.resolveContent` will &rarr; `require("src/server/views/index-views.jsx"):` {
  ```js
  /*
  require("src/server/routes/init-top.jsx");
  require("electrode-redux-router-engine");
  */
  ```
}
}
} &rarr; `electrode-react-webapp/lib/express/index.js: ` {
  ```js
  handleRoute(req, res, routeHandler, content, routeOptions);
  ```
} &rarr; `src/server/views/index-views.jsx: ` {
  ```js
  routesEngine = new ReduxRouterEngine({routes});
  routesEngine.render(req);
  // within `electrode-redux-router-engine/lib/redux-router-engine.js`, the `routesEngine.render` will check and match route, prepReduxStore and return built html content, _renderToString method will replace content in html template
  ```
} &rarr; `electrode-react-webapp/lib/express/index.js: ` {
  `DefaultHandleRoute` respond back in `handler(...).then()`
}


[back to top](#top)


<a id="2"></a>

## Hapi

<a id="2-1"></a>

### hapi server start flow

......

[back to top](#top)

<a id="2-2"></a>

### hapi server start flow

......

[back to top](#top)
