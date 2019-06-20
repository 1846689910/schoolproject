# Electrode app log

<a id="top"></a>

## **Contents**

[**cloned electrode start issue**](#1)

[**inspection of app crash caused by dependencies**](#2)

[**dev brk debug in vs code debuger**](#3)

[**fyn-lock.yaml lock sub-dependencies version**](#4)

<a id="1"></a>

## cloned electrode start issue

cloned electrode cannot use `clap gen-X-app` issue

```bash
cd electrode
fyn
cd packages/generator-electrode
fyn
cd ../electrode-ui-config
fyn
clap compile
```

[back to top](#top)

<a id="2"></a>

## inspection of app crash caused by dependencies

app worked before but crash recently without code changes. Maybe crashed by dependencies update implictly.

to install packages with versions at a specific time

```bash
fyn install --lock-time "05/20/2019"
```

usage referred by [fyn lock time](https://www.npmjs.com/package/fyn#locking-dependencies-by-time)

inspection:

1. find the last working version `fyn-lock.yaml`

```bash
mv fyn-lock.yaml last.yaml
```

2. find the first bad version

```bash
diff fyn-lock.yaml last.yaml | grep tgz
```

[back to top](#top)

<a id="3"></a>

## dev brk debug in vs code debuger

visual studio code debuger add configuration:

```json
{
  "type": "node",
  "request": "attach",
  "name": "Attach to Remote",
  "address": "127.0.0.1",
  "port": 9229,
  "localRoot": "${workspaceFolder}",
  "remoteRoot": "${workspaceFolder}"
}
```

[back to top](#top)

<a id="4"></a>

## fyn-lock.yaml lock sub-dependencies version

For instance, the `caniuse-lite@1.0.30000975` breaks and bring in error for `browserslist`. Then it is necessary to limit or downgrade the version of `caniuse-lite` to `1.0.30000974`.

+ install the package with version
```bash
fyn add caniuse-lite@1.0.30000974
```
+ modify `fyn-lock.yaml`, let all the dependency reference version to point to the wanted version
```
caniuse-lite:
 _latest: 1.0.30000975
 _:
  '1.0.30000974,^1.0.0,^1.0.30000844,^1.0.30000864,^1.0.30000865,^1.0.30000971,^1.0.30000975': 1.0.30000974
......
```

[back to top](#top)
