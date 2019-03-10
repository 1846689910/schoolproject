# **Git Concise**

<a id="top"></a>

## **Contents**

[**创建git repository**](#1)

[**git clone**](#2)

[**Fork Others' Repo**](#3)

[**Branches**](#4)

[**Commits**](#5)

[**Pull from a PR**](#6)

<a id="1"></a>

## **创建git repository**
先在GitHub上创建一个repository，然后进入到project的根目录
```bash
git init
git add .
git commit -m 'First commit'
git remote add origin <remote repository URL> # now your remote is origin/master
git remote -v
git push origin master
```
[back to top](#top)

<a id="2"></a>

## **git clone**
查看remote的repo的https的url
```bash
git clone <URL>
```
[back to top](#top)
