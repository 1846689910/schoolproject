# Electrode app issues fix

<a id="top"></a>

## **Contents**

[**App Initialization**](#1)

[**Electrode App API**](#2)

[**Css Module Enabled and other Css Preprocessor Configuration**](#3)

[**Server Side Bundle Selection(user configurable env target)**](#4)

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

<a id="3"></a>

## **Css Module Enabled and other Css Preprocessor Configuration**

`electrode/packages/electrode-archetype-react-app-dev/config/babel/babelrc-client.js` :

set `babel-plugin-react-css-modules` and give special css preprocessors syntax configuration

```js
const plugins = basePlugins.concat(
  ......
  // css module support
  enableCssModule && [
    [
      "babel-plugin-react-css-modules",
      {
        context: "./src",
        generateScopedName: `${isProduction ? "" : "[name]__[local]___"}[hash:base64:5]`,
        filetypes: {
          ".scss": {
            syntax: "postcss-scss",
            plugins: ["postcss-nested"]
          },
          ".styl": {
            syntax: "sugarss"
          },
          ".less": {
            syntax: "postcss-less"
          }
        }
      }
    ]
  ],
......
);
```

`electrode/packages/electrode-archetype-react-app-dev/config/webpack/partial/extract-style.js`:

import the specific preprocessor loader, like less, needs

```bash
fyn a less less-loader postcss-less
```

```js
const styleLoader = require.resolve("style-loader");
const styleLoader = require.resolve("style-loader");
const stylusLoader = require.resolve("stylus-relative-loader");
const stylusLoader = require.resolve("stylus-relative-loader");
const postcssLoader = require.resolve("postcss-loader");
const postcssLoader = require.resolve("postcss-loader");
const lessLoader = require.resolve("less-loader");

const stylusRules = {
  _name: `extract${cssModuleSupport ? "-css" : ""}-stylus`,
  test: /\.styl$/,
  use: ExtractTextPlugin.extract({
    fallback: styleLoader,
    use: cssModuleSupport
      ? [cssModuleQuery, postcssQuery, stylusQuery]
      : [cssQuery, postcssQuery, stylusQuery],
    publicPath: ""
  })
};

const lessQuery = {
  loader: lessLoader
};

const lessRules = {
  _name: `extract${cssModuleSupport ? "-css" : ""}-less`,
  test: /\.less$/,
  use: ExtractTextPlugin.extract({
    fallback: styleLoader,
    use: cssModuleSupport
      ? [cssModuleQuery, postcssQuery, lessQuery]
      : [cssQuery, postcssQuery, lessQuery],
    publicPath: ""
  })
};
```

`packages/electrode-archetype-react-app-dev/config/webpack/util/detect-css-module.js`:

```js
"use strict";

const Path = require("path");
const glob = require("glob");
const archetype = require("electrode-archetype-react-app/config/archetype");
const AppMode = archetype.AppMode;

function detectCSSModule() {
  const cssExists = glob.sync(Path.resolve(AppMode.src.client, "**", "*.css")).length > 0;
  const stylusExists = glob.sync(Path.resolve(AppMode.src.client, "**", "*.styl")).length > 0;
  const scssExists = glob.sync(Path.resolve(AppMode.src.client, "**", "*.scss")).length > 0;
  const lessExists = glob.sync(Path.resolve(AppMode.src.client, "**", "*.less")).length > 0;

  /*
   * cssModuleSupport default to undefined
   *
   * when cssModuleSupport not specified:
   * *only* *.css, cssModuleSupport sets to true
   * *only* *.styl, cssModuleSupport sets to false
   * *only* *.scss, cssModuleSupport sets to false
   */

  return cssExists && !stylusExists && !scssExists && !lessExists;
}

module.exports = detectCSSModule;
```

`packages/electrode-archetype-react-app-dev/test/spec/extract.style.spec.js`:

need to take caution on test case:

```js
...
    it("Should enable both stylus & css modules when cssModuleStylusSupport is true", () => {
      archetype.webpack.cssModuleSupport = false;
      archetype.webpack.cssModuleStylusSupport = true;
      const moduleConfig = require(moduleName)().module;
      expect(moduleConfig.rules[0]._name).to.equal("extract-css");
      expect(moduleConfig.rules[1]._name).to.equal("extract-scss");
      expect(moduleConfig.rules[2]._name).to.equal("extract-stylus");
      expect(moduleConfig.rules[3]._name).to.equal("extract-css-stylus");
      expect(moduleConfig.rules[4]._name).to.equal("extract-less");
    });
...
```

To use a specific css preprocessor, set environment target `CSS_MODULE_SUPPORT` as true:

- set `process.env.CSS_MODULE_SUPPORT = true` in `xclap.js`
- use `CSS_MODULE_SUPPORT=true clap dev`

Also review the [Doc support](https://github.com/electrode-io/electrode/blob/master/docs/chapter1/intermediate/app-archetype/extract-styles.md#flags)

[back to top](#top)

<a id="4"></a>

## **Server Side Bundle Selection(user configurable env target)**

`packages/electrode-archetype-react-app-dev/config/archetype.js`:

```js
const babelConfigSpec = {
  ......
  envTargets: {
    env: "BABEL_ENV_TARGETS",
    type: "json",
    default: {
      //`default` and `node` targets object is required
      default: {
        ie: "8"
      },
      node: process.versions.node.split(".")[0]
    }
  },
  target: {
    env: "ENV_TARGET",
    type: "string",
    default: "default"
  }
};
```

`packages/electrode-archetype-react-app-dev/config/webpack/partial/output.js`:

```js
"use strict";

const Path = require("path");
const { AppMode, babel } = require("electrode-archetype-react-app/config/archetype");
const inspectpack = process.env.INSPECTPACK_DEBUG === "true";
const { target } = babel;

module.exports = {
  output: {
    path: Path.resolve(target !== "default" ? `dist-${target}` : "dist", "js"),
    pathinfo: inspectpack, // Enable path information for inspectpack
    publicPath: "/js/",
    chunkFilename: `${target}.[hash].[name].js`,
    filename: AppMode.hasSubApps ? "[name].bundle.js" : `${target}-[name].bundle.js`
  }
};
```

`packages/electrode-archetype-react-app/arch-clap.js`:

```js
const babelEnvTargetsArr = Object.keys(archetype.babel.envTargets).filter(k => k !== "node");

const buildDistDirs = babelEnvTargetsArr
  .filter(name => name !== "default")
  .map(name => `dist-${name}`);
let tasks = {
  //...,
  "mv-to-dist": ["mv-to-dist:clean", "mv-to-dist:mv-dirs", "mv-to-dist:keep-targets"],
  "build-dist-min": {
    dep: [".production-env"],
    desc: "build dist for production",
    task: xclap.concurrent(
      babelEnvTargetsArr.map((name, index) =>
        xclap.exec(
          [
            `webpack --config`,
            quote(webpackConfig("webpack.config.js")),
            `--colors --display-error-details`
          ],
          {
            xclap: { delayRunMs: index * 2000 },
            execOptions: { env: { ENV_TARGET: name } }
          }
        )
      )
    )
  },

  "mv-to-dist:clean": {
    desc: `clean static resources within ${buildDistDirs}`,
    task: () => {
      buildDistDirs.forEach(dir => {
        // clean static resources within `dist-X` built by user specified env targets
        // and leave [.js, .map, .json] files only
        const removedFiles = scanDir.sync({
          dir: Path.resolve(dir),
          includeRoot: true,
          ignoreExt: [".js", ".map", ".json"]
        });
        shell.rm("-rf", ...removedFiles);
      });
      return;
    }
  },

  "mv-to-dist:mv-dirs": {
    desc: `move ${buildDistDirs} to dist`,
    task: () => {
      buildDistDirs.forEach(dir => {
        scanDir
          .sync({
            dir,
            includeRoot: true,
            filterExt: [".js", ".json", ".map"]
            // the regex above matches all the sw-registration.js, sw-registration.js.map,
            // main.bundle.js and main.bundle.js.map and stats.json
          })
          .forEach(file => {
            if (file.endsWith(".js")) {
              shell.cp("-r", file, "dist/js");
            } else if (file.endsWith(".map")) {
              shell.cp("-r", file, "dist/map");
            } else {
              shell.cp("-r", file, `dist/server/${dir.split("-")[1]}-${Path.basename(file)}`);
            }
          });
      });
      return;
    }
  },

  "mv-to-dist:keep-targets": {
    desc: `write each targets to respective isomorphic-assets.json`,
    task: () => {
      buildDistDirs.forEach(dir => {
        const isomorphicPath = Path.resolve(dir, "isomorphic-assets.json"); // add `targets` field to `dist-X/isomorphic-assets.json`
        if (Fs.existsSync(isomorphicPath)) {
          Fs.readFile(isomorphicPath, { encoding: "utf8" }, (err, data) => {
            if (err) throw err;
            const assetsJson = JSON.parse(data);
            const { envTargets } = archetype.babel;
            assetsJson.targets = envTargets[dir.split("-")[1]];
            Fs.writeFile(isomorphicPath, JSON.stringify(assetsJson, null, 2), err => {
              if (err) throw err;
            });
          });
        }
      });
      return;
    }
  },
  ".clean.dist": () => shell.rm("-rf", "dist", ...buildDistDirs)
  //...
};
```

`packages/electrode-react-webapp/lib/react-webapp.js`:

```js
const Fs = require("fs");
const otherStats = {};
if (Fs.existsSync("dist/server")) {
  Fs.readdirSync("dist/server")
    .filter(x => x.endsWith("-stats.json"))
    .reduce((prev, x) => {
      const k = Path.basename(x).split("-")[0];
      prev[k] = `dist/server/${x}`;
      return prev;
    }, otherStats);
}

const setupOptions = options => {
  const pluginOptionsDefaults = {
    // ...
    otherStats
    // ...
  };
  const otherAssets = Object.entries(pluginOptions.otherStats).reduce((prev, [k, v]) => {
    prev[k] = loadAssetsFromStats(getStatsPath(v, pluginOptions.buildArtifacts));
    return prev;
  }, {});
  pluginOptions.__internals = _.defaultsDeep({}, pluginOptions.__internals, {
    //...
    otherAssets
    //...
  });
  // ...
};
```

`packages/electrode-react-webapp/lib/react/token-handlers.js`:

```js
module.exports = function setup(handlerContext /*, asyncTemplate*/) {

}
```

[back to top](#4)
