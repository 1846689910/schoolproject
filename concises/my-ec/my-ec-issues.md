# Electrode app issues fix

<a id="top"></a>

## **Contents**

[**App Initialization**](#1)

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
