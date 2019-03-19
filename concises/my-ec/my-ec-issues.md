# Electrode app issues fix

<a id="top"></a>

## **Contents**

[**App Initialization**](#1)

[**Electrode App API**](#2)

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

+ the `init` property refer to a node module that will do init for this component, the default path if you use `.` as beginning of the path will be `src/server/routes`, so if all your init functions are in `src/server/routes`, the path should be `./init-X`


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
