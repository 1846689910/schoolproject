# Electrode app simplified running flow

<a id="top"></a>

## **Contents**

[**Express**](#1)

- [express server start flow](#1-1)
- [express server coming request flow](#1-2)

[**Hapi**](#2)

- [hapi server start flow](#2-1)
- [hapi server coming request flow](#2-2)

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

`startServer(confippet.config)` &rarr; the config somehow will load the `project/config/default.js:`

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

} &rarr; `electrode-archetype-react-app-dev/lib/webpack-dev-express:` {

```js
devSetup(app, “http”, config.port)
```

} &rarr; `src/server/express-server.js:` {

```js
logger.log(“App listening on port: 3000”);
```

}

[back to top](#top)

<a id="1-2"></a>

### express server coming request flow

send request / or visit http://localhost:3000
`electrode-react-webapp/lib/express/index.js:` {

```js
/*middleware within `registerRoutes` get the request */
app.METHOD(path, (req, res) => {
  ReactWebapp.getContentResolver(registerOptions, ...); // will call `ReactWebapp.resolveContent`
  handleRoute(req, res, routeHandler, content, routeOptions); // generally `handleRoute` is `DefaultHandleRoute`
})
```

&rarr; `electrode-react-webapp/lib/react-webapp.js:` {

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
} &rarr; `electrode-react-webapp/lib/express/index.js:` {

```js
handleRoute(req, res, routeHandler, content, routeOptions);
```

} &rarr; `src/server/views/index-views.jsx:` {

```js
routesEngine = new ReduxRouterEngine({ routes });
routesEngine.render(req);
// within `electrode-redux-router-engine/lib/redux-router-engine.js`, the `routesEngine.render` will check and match route, prepReduxStore and return built html content, _renderToString method will replace content in html template
```

} &rarr; `electrode-react-webapp/lib/express/index.js:` {
`DefaultHandleRoute` respond back in `handler(...).then()`
}

[back to top](#top)

<a id="2"></a>

## Hapi

<a id="2-1"></a>

### hapi server start flow

`src/server/index.js` {

```js
require("electrode-confippet"); // somehow import config/default.js and register plugin “electrode-react-webapp/lib/hapi”
```

} &rarr; `electrode-server/lib/electrode-server.js` {

return promise chain, new Hapi.server, register listener, server starts

} &rarr; `electrode-react-webapp/lib/hapi/index.js` {

```js
exports = { register, registerRoutes: register };
```

} &rarr; `electrode-react-webapp/lib/hapi/hpai17.js` {

```js
registerRoutes = require("./register-routes");
```

}

[back to top](#top)

<a id="2-2"></a>

### hapi server start flow

`electrode-react-webapp/lib/hapi/register-routes.js` {

```js
server.route({method: …, handler(){
  return handleRoute(..., routeHandler)}
})
// routeHandler refer to electrode-react-webapp/lib/react-webapp.js makeRouteHandler
```

} &rarr; `electrode-react-webapp/lib/hapi/plugin17.js` {

    handler above is DefaultHandleRoute,
    DefaultHandleRoute return routeHandler({...}) in before step

} &rarr; `electrode-react-webapp/renderer.js` {

```js
render(context);
```

} &rarr; `electrode-react-webapp/lib/react/content.js` {

```js
userContent = userContent(......);
```

} &rarr; `server/views/index-view.jsx` {

```js
routesEngine.render(req);
```

} &rarr; `electrode-redux-router-engine/lib/redux-router-engine.js` {

```js
class class ReduxRouterEngine {
  async render(){
    // call `startMatch`, `checkMatch`, `prepReduxStore`
  }
}
```

} &rarr; `electrode-react-webapp/lib/hapi/plugin17.js` {

respond back data with status

}

[back to top](#top)
