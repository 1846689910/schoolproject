# Electrode app features dev log

<a id="top"></a>

## **Contents**

### features

[**Css Module Enabled and other Css Preprocessor Configuration**](#3)

[**Server Side Bundle Selection(user configurable env target)**](#4)

[**Dynamic Import Demo**](#5)

[**entry format with archetype.webpack.enableBabelPolyfill and useBuiltIns**](#6)

[**Server Side Bundle Selection(user configurable env target) for archetypeV5**](#7)

[**entry format with archetype.webpack.enableBabelPolyfill and useBuiltIns for archetypeV5**](#8)

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
  },
  // `extendLoader` is used to override `babel-loader` only when `hasMultiTargets=true`
  extendLoader: {
    type: "json",
    default: {}
  }
};
//......
module.exports.babel.hasMultiTargets =
  Object.keys(module.exports.babel.envTargets)
    .sort()
    .join(",") !== "default,node";
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
const useBuiltIns = archetype.babel.hasMultiTargets ? { useBuiltIns: "entry", corejs: "2" } : {};
const presets = [
  //
  // webpack 4 can handle ES modules now so turn off babel module transformation
  // in production mode to allow tree shaking.
  // But keep transforming modules to commonjs when not in production mode so tests
  // can continue to stub ES modules.
  //
  [
    "@babel/preset-env",
    { modules: isProduction ? "auto" : "commonjs", loose: true, targets, ...useBuiltIns }
  ],
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
const archetype = require("electrode-archetype-react-app/config/archetype");
const { AppMode, babel } = archetype;

const inspectpack = process.env.INSPECTPACK_DEBUG === "true";

const getOutputFilename = () => {
  let filename = "[name].bundle.[hash].js";

  if (AppMode.hasSubApps) {
    filename = "[name].bundle.js";
  } else if (babel.hasMultiTargets) {
    filename = `${babel.target}.[name].bundle.js`;
  }

  return filename;
};

const getOutputPath = () => {
  if (process.env.WEBPACK_DEV === "true") {
    return "/"; // simulate the behavior of webpack-dev-server, which sets output path to /
  } else {
    return Path.resolve(babel.target !== "default" ? `dist-${babel.target}` : "dist", "js");
  }
};

module.exports = {
  output: {
    path: getOutputPath(),
    pathinfo: inspectpack, // Enable path information for inspectpack
    publicPath: "/js/",
    chunkFilename: babel.hasMultiTargets ? `${babel.target}.[hash].[name].js` : "[hash].[name].js",
    filename: getOutputFilename()
  }
};
```

`packages/electrode-archetype-react-app-dev/config/webpack/partial/babel.js`

```js
rules: [
  assign({}, babelLoaderConfig, archetype.babel.hasMultiTargets ? archetype.babel.extendLoader : {})
];
```

`packages/electrode-archetype-react-app/arch-clap.js`:

```js
const babelEnvTargetsArr = Object.keys(archetype.babel.envTargets).filter(k => k !== "node");

const buildDistDirs = babelEnvTargetsArr
  .filter(name => name !== "default")
  .map(name => `dist-${name}`);
let tasks = {
  build: {
    // ...
    task:[".build-lib", "build-dist", ".check.top.level.babelrc", "mv-to-dist"]
  }
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
    name = `${__dist}${name.substr(name.indexOf("."))}`;
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
      jsChunk: { name: "default.main.bundle.js" }
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

If the user app using `@walmart/electrode-index-page`, should refer [PR63](https://gecgithub01.walmart.com/electrode/electrode-index-page/pull/63) (**internal library, confidential code**)

- Note:
  `config/default.js` includes config of `electrode-react-webapp`:

```js
    webapp: {
    module: "electrode-react-webapp/lib/hapi",
    options: {
      pageTitle: "hapi-app",
      insertTokenIds: false,
      // bundleChunkSelector: "./{{env.APP_SRC_DIR}}/server/bundleChunkSelector",
      paths: {
        "/{args*}": {
          content: {
            module: "./{{env.APP_SRC_DIR}}/server/views/index-view"
          }
        }
      }
    }
  },
```

`bundleChunkSelector` will be used to specific use a chunk in app, if the user defines their own `src/client/entry.config.js`:

```js
module.exports = {
  foo: "./app.jsx"
};
```

could create `src/server/bundleChunkSelector.js`:

```js
module.exports = () => ({
  css: "foo",
  js: "foo"
});
```

This will enable the app use the `foo` chunks, like `es6.foo.bundle.js`

**Build**:

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

[back to top](#top)

<a id="5"></a>

## **Dynamic Import Demo**

`packages/electrode-archetype-react-app-dev/config/babel/babelrc-client.js`:

```js
const basePlugins = [
  "@babel/plugin-syntax-dynamic-import"
  // ...
];
```

`packages/electrode-archetype-react-app-dev/package.json`:

```js
{
  "@babel/plugin-syntax-dynamic-import": "^7.2.0",
  "acorn": "^6.0.5",
}
```

`packages/generator-electrode/generators/app/templates/_package.js`:

```js
{
  //...
  "@loadable/component": "^5.7.0",
  //...
}
```

`src/client/actions/index.jsx`:

```js
export const setShowFakeComp = value => {
  return {
    type: "SHOW_FAKE_COMP",
    value
  };
};
```

`src/client/components/demo-dynamic-import.jsx`:

```js
import React from "react";
import loadable from "@loadable/component";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { setShowFakeComp } from "../actions";
import Promise from "bluebird";
import demoStyle from "../styles/demo2.css"; // eslint-disable-line no-unused-vars
import custom from "../styles/custom.css"; // eslint-disable-line no-unused-vars

const Fallback = () => (
  <div styleName={"custom.dynamic-demo-fallback"}>
    <strong>Dynamic Imported Component is loading ...</strong>
  </div>
);

let Demo = loadable(() => import(/* webpackChunkName: "fake" */ "./demo-fake"), {
  fallback: <Fallback />
});

const timeout = 1000;
const load = dispatch => {
  dispatch(setShowFakeComp(false));
  Promise.try(() => loadable(() => import("./demo-fake")))
    .delay(timeout)
    .then(x => (Demo = x))
    .then(() => {
      dispatch(setShowFakeComp(true));
    });
};

const DynamicImportDemo = ({ showFakeComp, dispatch }) => {
  return (
    <div>
      <h6 styleName={"custom.docs-header"}>
        Demo Dynamic Import with&nbsp;
        <a href="https://www.smooth-code.com/open-source/loadable-components/">
          Loadable Components
        </a>
      </h6>
      <div styleName={"custom.dynamic-demo-box"}>
        {showFakeComp.value ? <Demo /> : <Fallback />}
      </div>
      <div>
        <button onClick={() => load(dispatch)}>Refresh Comp</button>
      </div>
    </div>
  );
};
DynamicImportDemo.propTypes = {
  showFakeComp: PropTypes.object,
  dispatch: PropTypes.func
};
export default connect(
  state => state,
  dispatch => ({ dispatch })
)(DynamicImportDemo);
```

`src/client/components/demo-fake.jsx`:

```js
import React from "react";
import custom from "../styles/custom.css"; // eslint-disable-line no-unused-vars
import milligram from "milligram/dist/milligram.css"; // eslint-disable-line no-unused-vars
export default () => (
  <div>
    <div styleName={"custom.docs-example"}>
      <a styleName={"milligram.button"} href="#">
        Anchor button
      </a>
      <button>Button element</button>
      <input type="submit" value="submit input" />
      <input type="button" value="button input" />
    </div>
    <div styleName={"custom.docs-example"}>
      <a styleName={"milligram.button milligram.button-outline"} href="#">
        Anchor button
      </a>
      <button styleName={"milligram.button-outline"}>Button element</button>
      <input styleName={"milligram.button-outline"} type="submit" value="submit input" />
      <input styleName={"milligram.button-outline"} type="button" value="button input" />
    </div>
  </div>
);
```

`src/client/components/home.jsx`:

```js
import DemoDynamicImport from "./demo-dynamic-import";
// ...
<div styleName={"custom.docs-section"}>
  <DemoDynamicImport />
</div>;
// ...
const mapStateToProps = state => state;
```

`src/client/reducers/index.jsx`:

```js
const showFakeComp = (store, action) => {
  if (action.type === "SHOW_FAKE_COMP") {
    return {
      value: action.value
    };
  }
  return store || { value: false };
};
export default combineReducers({
  //...
  showFakeComp
});
```

`src/server/routes/init-top.jsx`:

```js
showFakeComp: {
  value: true;
}
```

[back to top](#top)

<a id="6"></a>

## **entry format with archetype.webpack.enableBabelPolyfill and useBuiltIns**

1. fix entry format when `archetype.webpack.enableBabelPolyfill` is `true`.

`packages/electrode-archetype-react-app-dev/config/webpack/partial/entry.js`:

```js
function shouldPolyfill() {
  if (archetype.webpack.enableBabelPolyfill) {
    const hasMultipleTarget =
      Object.keys(archetype.babel.envTargets)
        .sort()
        .join(",") !== "default,node";
    if (hasMultipleTarget) {
      return archetype.babel.target === "default";
      // for all other targets, disable polyfill
    } else {
      return true;
    }
  }
  return false;
}

function makeEntry() {
  let entry = appEntry();
  const polyfill = shouldPolyfill();
  if (polyfill) {
    const babelPolyfill = "@babel/polyfill";
    if (_.isArray(entry)) {
      entry = { main: [babelPolyfill, ...entry] };
    } else if (_.isObject(entry)) {
      entry = Object.entries(entry).reduce((prev, [k, v]) => {
        prev[k] = [babelPolyfill].concat(v);
        return prev;
      }, {});
    } else {
      entry = { main: [babelPolyfill, entry] };
    }
  }
  return entry;
}
module.exports = {
  context,
  entry: makeEntry()
};
```

2. add `useBuiltIns` and `corejs` to `env` options, when user import `babel-polyfill` or `@babel/polyfill` will dynamically load the polyfills that needed according to targeted browser version

`packages/electrode-archetype-react-app-dev/config/babel/babelrc-client.js`:

```js
const hasOtherTargets =
  Object.keys(archetype.babel.envTargets)
    .sort()
    .join(",") !== "default,node";
const useBuiltIns = hasOtherTargets ? { useBuiltIns: "entry", corejs: "2" } : {};
const presets = [
  // ...
  [
    "@babel/preset-env",
    { modules: isProduction ? "auto" : "commonjs", loose: true, targets, ...useBuiltIns }
  ]
  // ...
];
```

[back to top](#top)

<a id="7"></a>

## Server Side Bundle Selection(user configurable env target) for archetypeV5

archetype v5 use webpack3 + babel6

`packages/electrode-archetype-react-app-dev/config/webpack/partial/uglify.js`:
webpack 3 does not have `mode: production/development`

```js
"use strict";

const Uglify = require("uglifyjs-webpack-plugin");
const optimize = require("webpack").optimize;
const archetype = require("electrode-archetype-react-app/config/archetype");

module.exports = function() {
  // Allow env var to disable minifcation for inspectpack usage.
  if (process.env.INSPECTPACK_DEBUG === "true") {
    return {};
  }

  const uglifyOpts = archetype.babel.hasMultiTargets
    ? {
        sourceMap: true,
        uglifyOptions: {
          compress: {
            warnings: false
          }
        }
      }
    : {
        sourceMap: true,
        compress: {
          warnings: false
        }
      };

  // preserve module ID comment in bundle output for optimizeStats
  if (process.env.OPTIMIZE_STATS === "true") {
    const comments = archetype.babel.hasMultiTargets ? "extractComments" : "comments";
    uglifyOpts[comments] = /^\**!|^ [0-9]+ $|@preserve|@license/;
  }

  const uglifyPlugin = archetype.babel.hasMultiTargets
    ? new Uglify(uglifyOpts)
    : new optimize.UglifyJsPlugin(uglifyOpts);

  return { plugins: [uglifyPlugin] };
};
```

`packages/electrode-archetype-react-app-dev/config/webpack/partial/babel.js`

```js
module.exports = function(options) {
  const { options: babelLoaderOptions = {}, ...rest } = archetype.babel.extendLoader;

  const getBabelrcClient = () => {
    const babelrcClient = JSON.parse(
      Fs.readFileSync(require.resolve("../../babel/babelrc-client-multitargets"))
    );
    const { target, envTargets } = archetype.babel;
    const { presets = [], plugins = [], ...restOptions } = babelLoaderOptions;
    const targets = envTargets[target];
    babelrcClient.presets.unshift([
      "env",
      { loose: true, targets, useBuiltIns: "entry", corejs: "2" }
    ]);
    babelrcClient.presets = babelrcClient.presets.concat(presets);
    babelrcClient.plugins = babelrcClient.plugins.concat(plugins);
    return Object.assign(babelrcClient, { babelrc: false }, restOptions);
  };

  if (options.HotModuleReload) {
    require("react-hot-loader/patch");
  }

  const clientVendor = Path.join(AppMode.src.client, "vendor/");
  const babelExclude = x => {
    if (x.indexOf("node_modules") >= 0) return true;
    if (x.indexOf(clientVendor) >= 0) return true;
    return false;
  };

  const babelLoader = {
    _name: "babel",
    test: /\.jsx?$/,
    exclude: babelExclude,
    use: [
      {
        loader: "babel-loader",
        options: Object.assign(
          { cacheDirectory: Path.resolve(".etmp/babel-loader") },
          options.babel,
          archetype.babel.hasMultiTargets ? getBabelrcClient() : {}
        )
      }
    ].filter(_.identity)
  };

  if (options.HotModuleReload) {
    logger.info("Enabling Hot Module Reload support in webpack babel loader");
    babelLoader.include = Path.resolve(AppMode.src.client);
  }

  return {
    module: {
      rules: [_.assign({}, babelLoader, archetype.babel.hasMultiTargets ? rest : {})]
    }
  };
};
```

`packages/electrode-archetype-react-app-dev/config/archetype.js`

```js
const babelConfigSpec = {
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
  },
  // `extendLoader` is used to override `babel-loader` only when `hasMultiTargets=true`
  extendLoader: {
    type: "json",
    default: {}
  }
};
const config = {
  //...
  babel: xenvConfig(babelConfigSpec, userConfig.babel, { merge })
};
module.exports.babel.hasMultiTargets =
  Object.keys(module.exports.babel.envTargets)
    .sort()
    .join(",") !== "default,node";
```

add `packages/electrode-archetype-react-app-dev/config/babel/babelrc-client-multitargets`

`packages/electrode-archetype-react-app-dev/config/webpack/partial/extract-style.js`

```js
new ExtractTextPlugin({ filename: archetype.babel.hasMultiTargets ? "[name].style.css" : "[name].style.[hash].css" }),
```

`packages/electrode-archetype-react-app-dev/config/webpack/partial/output.js`

```js
"use strict";

const Path = require("path");
const { babel } = require("electrode-archetype-react-app/config/archetype");
const inspectpack = process.env.INSPECTPACK_DEBUG === "true";
const { target, hasMultiTargets } = babel;

module.exports = {
  output: {
    path: Path.resolve(target !== "default" ? `dist-${target}` : "dist", "js"),
    pathinfo: inspectpack, // Enable path information for inspectpack
    publicPath: "/js/",
    chunkFilename: hasMultiTargets ? `${target}.[hash].[name].js` : "[hash].[name].js",
    filename: hasMultiTargets ? `${target}.[name].bundle.js` : "[name].bundle.[hash].js"
  }
};
```

`packages/electrode-archetype-react-app-dev/package.json`

```json
{
  "uglifyjs-webpack-plugin": "^1.2.2",
  "xclap": "^0.2.30"
}
```

`packages/electrode-archetype-react-app/arch-clap.js`

```js
const scanDir = devRequire("filter-scan-dir");
function makeTasks(xclap) {
  assert(xclap.concurrent, "xclap version must be 0.2.28+");
  // ...
    const babelCliIgnore = quote(
    [`**/*.spec.js`, `**/*.spec.jsx`]
      .concat(archetype.babel.enableTypeScript && [`**/*.test.ts`, `**/*.test.tsx`])
      .filter(x => x)
      .join(",")
  );

  const babelCliExtensions = quote(
    [".js", ".jsx"]
      .concat(archetype.babel.enableTypeScript && [".ts", ".tsx"])
      .filter(x => x)
      .join(",")
  );
  const babelEnvTargetsArr = Object.keys(archetype.babel.envTargets).filter(k => k !== "node");

  const buildDistDirs = babelEnvTargetsArr
    .filter(name => name !== "default")
    .map(name => `dist-${name}`);
  let tasks = {
    // ...
    build: {
      //...
      task: ["build-dist", ".build-lib", ".check.top.level.babelrc", "mv-to-dist"]
    }
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
    "build-lib:client":{
            task: () => {
        const dirs = AppMode.hasSubApps
          ? []
              .concat(
                scanDir.sync({
                  dir: AppMode.src.dir,
                  includeDir: true,
                  grouping: true,
                  filterDir: x => !x.startsWith("server") && "dirs",
                  filter: () => false
                }).dirs
              )
              .filter(x => x)
          : [AppMode.client];
        return dirs.map(x =>
          mkCmd(
            `~$babel ${Path.posix.join(AppMode.src.dir, x)}`,
            `--out-dir=${Path.posix.join(AppMode.lib.dir, x)}`,
            `--extensions=${babelCliExtensions}`,
            `--source-maps=inline --copy-files`,
            `--verbose --ignore=${babelCliIgnore}`
          )
        );
      }
    },
    ".clean.dist": () => shell.rm("-rf", "dist", ...buildDistDirs),
  }
}
module.exports = function(xclap) {
  setupPath();
  createElectrodeTmpDir();
  xclap = xclap || requireAt(process.cwd())("xclap") || devRequire("xclap");
  process.env.FORCE_COLOR = "true"; // force color for chalk
  xclap.load("electrode", makeTasks(xclap));
  warnYarn();
};
```

`packages/electrode-archetype-react-app/lib/app-mode.js`

```js
{
  //...
        hasEnv: () => {
        return !!process.env[envKey];
      },
      client,
      server,
}
```

`packages/electrode-archetype-react-app/package.json`

```json
{
  "dependencies": {
    "filter-scan-dir": "^1.0.10"
  }
}
```

[back to top](#top)

<a id="8"></a>

## entry format with archetype.webpack.enableBabelPolyfill for archetypeV5

`packages/electrode-archetype-react-app-dev/config/webpack/partial/entry.js`

```js
function shouldPolyfill() {
  if (archetype.webpack.enableBabelPolyfill) {
    if (archetype.babel.hasMultipleTarget) {
      return archetype.babel.target === "default";
      // for all other targets, disable polyfill
    } else {
      return true;
    }
  }
  return false;
}

function makeEntry() {
  let entry = appEntry();
  const polyfill = shouldPolyfill();
  if (polyfill) {
    const babelPolyfill = "babel-polyfill";
    if (_.isArray(entry)) {
      entry = { main: [babelPolyfill, ...entry] };
    } else if (_.isObject(entry)) {
      entry = Object.entries(entry).reduce((prev, [k, v]) => {
        prev[k] = [babelPolyfill].concat(v);
        return prev;
      }, {});
    } else {
      entry = { main: [babelPolyfill, entry] };
    }
  }
  return entry;
}

module.exports = {
  context,
  entry: makeEntry()
};
```

[back to top](#top)
