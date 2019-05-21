# Electrode app log

<a id="top"></a>

## **Contents**

[**cloned electrode start issue**](#1)

[**inspection of app crash caused by dependencies**](#2)

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
