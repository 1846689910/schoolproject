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

`packages/electrode-archetype-react-app-dev/config/babel/babelrc-client.js`:

```js
const basePlugins = [
  /* removed all covered plugins by @babel/preset-env
  "@babel/plugin-transform-template-literals",
  "@babel/plugin-transform-function-name",
  "@babel/plugin-transform-arrow-functions",
  "@babel/plugin-transform-block-scoped-functions",
  "@babel/plugin-transform-object-super",
  "@babel/plugin-transform-shorthand-properties",
  "@babel/plugin-transform-computed-properties",
  "@babel/plugin-transform-for-of",
  "@babel/plugin-transform-sticky-regex",
  "@babel/plugin-transform-unicode-regex",
  "@babel/plugin-transform-spread",
  "@babel/plugin-transform-parameters",
  "@babel/plugin-transform-destructuring",
  "@babel/plugin-transform-block-scoping",
  "@babel/plugin-transform-typeof-symbol",
  [
    "@babel/plugin-transform-regenerator",
    {
      async: false,
      asyncGenerators: false
    }
  ],
  "@babel/plugin-proposal-object-rest-spread",
  */
  "@babel/plugin-syntax-dynamic-import",
  //
  // allow class properties. loose option compile to assignment expression instead
  // of Object.defineProperty.
  // Note: This must go before @babel/plugin-transform-classes
  //
  (enableTypeScript || transformClassProps) && [
    "@babel/plugin-proposal-class-properties",
    { loose: looseClassProps }
  ],
  [
    "babel-plugin-i18n-id-hashing",
    {
      varsContainingMessages: ["defaultMessages", "translations"]
    }
  ],
  [
    "babel-plugin-react-intl",
    {
      messagesDir: "./tmp/messages/",
      enforceDescriptions: true
    }
  ],
  "transform-node-env-inline",
  "babel-plugin-lodash",
  "@babel/plugin-transform-runtime",
  enableFlow && [
    "@babel/plugin-transform-flow-strip-types",
    { requireDirective: flowRequireDirective }
  ]
];
//...
const targets = archetype.babel.envTargets[archetype.babel.target];

const presets = [
  //
  // webpack 4 can handle ES modules now so turn off babel module transformation
  // in production mode to allow tree shaking.
  // But keep transforming modules to commonjs when not in production mode so tests
  // can continue to stub ES modules.
  //
  ["@babel/preset-env", { modules: isProduction ? "auto" : "commonjs", loose: true, targets }],
  enableTypeScript && "@babel/preset-typescript",
  "@babel/preset-react"
];
//...
```

`/packages/electrode-archetype-react-app-dev/config/babel/babelrc-server.js`:

```js
const {
  enableTypeScript,
  flowRequireDirective,
  enableFlow,
  transformClassProps,
  looseClassProps,
  envTargets
} = archetype.babel;

const { node } = envTargets;

module.exports = {
  presets: [
    [
      "@babel/preset-env",
      {
        targets: { node }
      }
    ],
    enableTypeScript && "@babel/preset-typescript"
  ].filter(x => x)
  //...
};
```

`packages/electrode-archetype-react-app-dev/config/webpack/partial/output.js`:

```js
"use strict";

const Path = require("path");
const { AppMode, babel } = require("electrode-archetype-react-app/config/archetype");
const inspectpack = process.env.INSPECTPACK_DEBUG === "true";
const { target, envTargets } = babel;
const hasOtherTargets =
  Object.keys(envTargets).filter(x => x !== "default" && x !== "node").length > 0;

const filename = (() => {
  let _filename = "[name].bundle.[hash].js";
  if (AppMode.hasSubApps) {
    _filename = "[name].bundle.js";
  } else if (hasOtherTargets) {
    _filename = `${target}.[name].bundle.js`;
  }
  return _filename;
})();

module.exports = {
  output: {
    path: Path.resolve(target !== "default" ? `dist-${target}` : "dist", "js"),
    pathinfo: inspectpack, // Enable path information for inspectpack
    publicPath: "/js/",
    chunkFilename: hasOtherTargets ? `${target}.[hash].[name].js` : "[hash].[name].js",
    filename
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
const { getOtherStats, getOtherAssets } = require("./utils");

const otherStats = getOtherStats();

const setupOptions = options => {
  const pluginOptionsDefaults = {
    // ...
    otherStats
    // ...
  };
  const otherAssets = getOtherAssets(pluginOptions);
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
const { getBundleJsNameByQuery } = require("../utils");
module.exports = function setup(handlerContext /*, asyncTemplate*/) {
  // ...
  const otherAssets = routeOptions.__internals.otherAssets;
  const routeData = {
    otherAssets
    // ...
  };
  const bundleJs = data => {
    if (!data.renderJs) {
      return "";
    }
    if (WEBPACK_DEV) {
      return data.devJSBundle;
    } else if (data.jsChunk) {
      const bundleJsName = getBundleJsNameByQuery(data, otherAssets);
      return `${prodBundleBase}${bundleJsName}`;
    } else {
      return "";
    }
  };
  const tokenHandlers = {
    [BODY_BUNDLE_MARKER]: context => {
      context.user.query = context.user.request.query;
      // ...
    }
  };
  //...
};
```

`packages/electrode-react-webapp/lib/utils.js`:

```js
function getOtherStats() {
  const otherStats = {};
  if (fs.existsSync("dist/server")) {
    fs.readdirSync("dist/server")
      .filter(x => x.endsWith("-stats.json"))
      .reduce((prev, x) => {
        const k = Path.basename(x).split("-")[0];
        prev[k] = `dist/server/${x}`;
        return prev;
      }, otherStats);
  }
  return otherStats;
}

function getOtherAssets(pluginOptions) {
  return Object.entries(pluginOptions.otherStats).reduce((prev, [k, v]) => {
    prev[k] = loadAssetsFromStats(getStatsPath(v, pluginOptions.buildArtifacts));
    return prev;
  }, {});
}

function getBundleJsNameByQuery(data, otherAssets) {
  let { name } = data.jsChunk;
  const { __dist } = data.query;
  if (__dist && otherAssets[__dist]) {
    name = `${__dist}.main.bundle.js`;
  }
  return name;
}
module.exports = {
  //...
  getOtherStats,
  getOtherAssets,
  getBundleJsNameByQuery
};
```

`packages/electrode-react-webapp/test/spec/utils.spec.js`:

```js
describe("getOtherStats", () => {
  it("should require stats file if dist/server exists", () => {
    const fakeExistsSync = sinon.stub(Fs, "existsSync").callsFake(() => true);
    const fakeReaddirSync = sinon
      .stub(Fs, "readdirSync")
      .callsFake(() => [Path.resolve("es5-stats.json"), Path.resolve("es6-stats.json")]);
    const otherStats = utils.getOtherStats();
    fakeExistsSync.restore();
    fakeReaddirSync.restore();
    const keys = Object.keys(otherStats);
    expect(keys.includes("es5")).be.true;
    expect(keys.includes("es6")).be.true;
  });
});

describe("getOtherAssets", () => {
  it("should generate otherAssets if otherStats is not empty", () => {
    const otherStats = {
      es5: Path.resolve("es5-stats.json"),
      es6: Path.resolve("es6-stats.json")
    };
    const buildArtifacts = ".build";
    const pluginOptions = { otherStats, buildArtifacts };
    const otherAssets = utils.getOtherAssets(pluginOptions, utils.loadAssetsFromStats);
    const keys = Object.keys(otherAssets);
    expect(keys.includes("es5")).be.true;
    expect(keys.includes("es6")).be.true;
  });
});

describe("getBundleJsNameByQuery", () => {
  it("should get file name ends with main.bundle.js", () => {
    const data = {
      jsChunk: { name: "bundle" }
    };
    const otherAssets = {
      es6: { js: [{ name: "es6.main.bundle.js" }] }
    };
    const es6 = utils.getBundleJsNameByQuery(
      Object.assign(data, {
        query: { __dist: "es6" }
      }),
      otherAssets
    );
    expect(es6).to.equal(otherAssets.es6.js[0].name);
    const es5 = utils.getBundleJsNameByQuery(
      Object.assign(data, {
        query: { __dist: "es5" }
      }),
      otherAssets
    );
    expect(es5).to.equal(data.jsChunk.name);
  });
});
```

Build:

3 ways to setup

1. inline command

```bash
BABEL_ENV_TARGETS='{"es6":{"chrome":65},"es3":{"chrome":30}}' clap build

BABEL_ENV_TARGETS='{"hello":{"ie":6}}' clap build
```

2. could set this in `xclap.js` at the project root:

```js
process.env.BABEL_ENV_TARGETS = `{"es6":{"chrome":65},"es3":{"chrome":30}}`;
```

3. create `archetype/config.js` at the project root, and include the config object in, you can also overwrite other properties within `electrode-archetype-react-app-dev/config/archetype.js` and `electrode-archetype-react-app/config/archetype`:

```js
module.exports = {
  webpack: {
    cssModuleSupport: true
  },
  babel: {
    envTargets: { es6: { chrome: 65 }, es3: { chrome: 30 } }
  }
};
```

After run by `node lib/server`:

Temporarily visit: http://localhost:3000?__dist=es6

or

http://localhost:3000?__dist=hello

...

[back to top](#4)
