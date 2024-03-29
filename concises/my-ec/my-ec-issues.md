# Electrode issues fix log

<a id="top"></a>

## **Contents**

### issues

[**App Initialization**](#1)

[**Electrode App API**](#2)

[**Express/Koa failing build because of process.send not a function error**](#issue-3)

[**test and spec file in `src/server` cannot be removed after build**](#issue-4)

[**add cssLoaderModules**](#5)

[**saving much more on es6 bundle size**](#6)

<a id="1"></a>

## App Initialization

React is using virtual dom to render all components within a statically designated element. All state changes, component operations and corresponding responses are all handled by react component life cycle and its render method. The static HTML page source is not changing when app state changes.

The simplie HTML source code is returned from SSR(server side rendering), which sends plain initial HTML to user while other resources are still in fetch. This is for improving the user performance.

check the source code changes from ssr, needs to revisit the page

App Initialization:

If you want to do some initialization before the app starts, you could use `async/await` in `src/server/routes/`.
For example, you wants to initialize the whole app and `Home` component.

in `src\client\routes.jsx`:

```js
const routes = [
  {
    path: "/",
    component: withRouter(Root),
    init: "./init-top",
    routes: [
      {
        path: "/",
        exact: true,
        component: Home,
        init: "./init-home"
      },
      {
        path: "/demo1",
        exact: true,
        component: Demo1
      },
      {
        path: "/demo2",
        exact: true,
        component: Demo2
      }
    ]
  }
];

export { routes };
```

**Note:**

- the `init` property refer to a node module that will do init for this component, the default path if you use `.` as beginning of the path will be `src/server/routes`, so if all your init functions are in `src/server/routes`, the path should be `./init-X`

Then, in `src/server/routes/init-top.jsx`, you could have some async calls to fetch resources then set the initialState:

```js
import reducer from "../../client/reducers";
const initNumber = async () => {
  const value = await new Promise(resolve => setTimeout(() => resolve(123), 2000));
  return { value };
};
export default async function initTop() {
  return {
    reducer,
    initialState: {
      checkBox: { checked: false },
      number: await initNumber(),
      username: { value: "" },
      textarea: { value: "" },
      selectedOption: { value: "0-13" }
    }
  };
}
```

and it is the same with `src\server\routes\init-home.jsx`

```js
import reducer from "../../client/reducers";
const initUsername = async () => {
  const value = await new Promise(resolve => setTimeout(() => resolve("alex"), 2000));
  return { value };
};
export default async () => ({
  reducer,
  initialState: {
    username: await initUsername()
  }
});
```

[back to top](#top)

<a id="2"></a>

## **Electrode App API**

Electrode app encapsulate the `express`, `hapi` and `koa` well inside. To add self-defined api:

create `src/server/plugins/my-api.js`:

```js
module.exports = {
  name: "MyApi",
  register: server =>
    server.route([
      {
        method: "GET",
        path: "/my-api1",
        handler: (request, h) => h.response("this is my-api1").code(200)
      },
      {
        method: "GET",
        path: "/my-api2",
        handler: (request, h) =>
          h
            .response(JSON.stringify({ foo: 123 }))
            .header("Content-Type", "application/json")
            .code(200)
      }
    ])
};
```

Then, in `config/default.js`, add the above plugin to the plugins array in `module.exports = {..., plugins:[...], ...}`:

```js
module.exports = {
  // ...
  "server/plugins/my-api": {
    module: "./{{env.APP_SRC_DIR}}/server/plugins/my-api"
  }
  // ...
};
```

Then you could start the app with `clap dev` and visit your api at http://localhost:3000/my-api1

As for `Express` app, it will be much easier to manipulate `src/server/views/index-view.jsx`, use `async` function and add a `req.path` check:

```js
module.exports = async req => {
  if (!routesEngine) {
    routesEngine = new ReduxRouterEngine({ routes });
  }
  if (req.path === "/my-api1") {
    const { res } = req;
    res.set("Content-Type", "application/json");
    return JSON.stringify({ foo: 123, bar: 2 });
  }
  return routesEngine.render(req);
};
```

[back to top](#top)

<a id="issue-3"></a>

## Express/Koa failing build because of process.send not a function error

`process.send` should be within process in a forked child process, used to communicate with parent process.

The error was because of `setDevMiddleware` was called in `NODE_ENV=production node lib/server`.

issue source:

```js
`packages/generator-electrode/generators/app/templates/src/server/express-server.js` -> `setDevMiddleware`

`packages/generator-electrode/generators/app/templates/src/server/koa-server.js` -> `setDevMiddleware`
```

`packages/electrode-archetype-react-app-dev/lib/webpack-dev-express.js`:

```js
"use strict";
const AppDevMiddleware = require("./app-dev-middleware");

function setup(app) {
  const isProduction = process.env.NODE_ENV === "production";
  if (!isProduction) {
    const middleware = new AppDevMiddleware({});
    middleware.setup();
    app.use((req, res, next) => {
      if (!req.app) req.app = {};
      req.app.webpackDev = middleware.webpackDev;
      next();
    });
  }
}
module.exports = setup;
```

`packages/electrode-archetype-react-app-dev/lib/webpack-dev-koa.js`:

```js
"use strict";
const AppDevMiddleware = require("./app-dev-middleware");

function setup(app) {
  const isProduction = process.env.NODE_ENV === "production";
  if (!isProduction) {
    const middleware = new AppDevMiddleware({});
    middleware.setup();
    app.use(async (ctx, next) => {
      ctx.webpackDev = middleware.webpackDev;
      return next();
    });
  }
}
module.exports = setup;
```

[back to top](#top)

<a id="issue-4"></a>

## test and spec file in `src/server` cannot be removed after build

`packages/electrode-archetype-react-app/arch-clap.js`:

```js
    ".build-lib:delete-babel-ignored-files": {
      desc: false,
      task: () => {
        const libDir = Path.resolve(AppMode.lib.dir);  // should use `AppMode.lib.dir` to match all test and spec files in both `client` and `server`
        const ignoredFiles = scanDir.sync({
          dir: libDir,
          includeRoot: true,
          filter: x => {
            return x.indexOf(".spec.") > 0 || x.indexOf(".test.") > 0;
          }
        });
        ignoredFiles.forEach(f => Fs.unlinkSync(f));
      }
    },
```

[back to top](#top)

<a id="5"></a>

## add cssLoaderModules

In order to back compatible with archetypeV3, when `cssModuleSupport` not enabled, the `localIdentName` still in `name__local__hash` format.
introduce `cssLoaderModules` in archetypeV5

`packages/electrode-archetype-react-app-dev/config/archetype.js`:

```js
const webpackConfigSpec = {
  // ...
  cssLoaderModules: { env: "CSS_LOADER_MODULES", default: false }
};
```

`packages/electrode-archetype-react-app-dev/config/webpack/partial/extract-style.js`:

```js
const cssQuery = `${cssLoader}${
  archetype.webpack.cssLoaderModules ? cssLoaderOptions : ""
}!${postcssLoader}`;
```

[back to top](#top)

<a id="6"></a>

## saving much more on es6 bundle size

use webpack to build and generate es6 bundle:

- create `archetype/config/index.js` and set `envTargets`
- use `extendLoader` to overwrite `babel-exclude`

```js
const es6Dependencies = [
  /* some module */
];
module.exports = {
  babel: {
    envTargets: {
      es6: { chrome: 73 }
    },
    extendLoader: {
      exclude: path => {
        if (process.env.ENV_TARGET !== "default") {
          for (const package of es6Dependencies) {
            if (path.includes(package)) return false; // let babel include this module and build it according to env target
          }
          return path.includes("node_module");
        }
      }
    }
  }
};
```

- set up alias in `webpack.config.js`. Generally, all packages `lib` are built and ready to use in es5 format. If use these `lib` code in components and some package provides `src` source code, user can setup `alias` to point `lib` to `src`. This could use the code after built with target

```js
const alias =
  process.env.ENV_TARGET !== "default"
    ? es6Dependencies.reduce((prev, dep) => {
        prev[`${dep}/lib`] = `${dep}/src`;
        prev[`${dep}`] = `${dep}/src`;
        return prev;
      }, {})
    : {};
module.exports = {
  resolve: {
    alias
  }
};
```

[back to top](#top)
