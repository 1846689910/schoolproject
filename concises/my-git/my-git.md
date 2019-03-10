# **Git Concise**

<a id="top"></a>

## **Contents**

[**创建git repository**](#1)

[**git clone**](#2)

[**Fork Others' Repo**](#3)

[**Branches**](#4)

[**Commits**](#5)

[**Pull From a PR**](#6)

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

<a id="3"></a>

## **Fork Others' Repo**

1. 先在github上fork别人的repo
2. clone the forked repo to local:
```bash
git clone <forked_your_repo_URL>
```
3. set others' repo as your repo's `upstream` repo(Your repo is `origin`, while others' repo is `upstream`)
```bash
git remote add upstream git://github.com/ORIGINAL-DEV-USERNAME/REPO-YOU-FORKED-FROM.git
git branch -r
git fetch upstream  # fetch 远端的所有分支和commit, git fetch upstream master只获取远端的master分支
```
4. keep your `origin` updated with `upstream`.
```bash
git fetch upstream
git reset --hard upstream/master # 放弃本地的修改，将branch与upstream/master的HEAD同步
git pull upstream master # 从upstream/master里pull changes
git push origin master --force  # 强制将upstream/master的代码放入remote的repo
```

[back to top](#top)

<a id="4"></a>

## **Branches**

创建分支
```bash
git branch -b debug
```
查看所有分支
```bash
git branch
git branch -a
git branch -r # 查看远端的分支，可以用git fetch <BRNACH>来获取远端分支内容
```
删除分支
```bash
git branch -d debug
git branch -D debug # 强制删除debug分支，即使其还有uncommitted的内容
```
切换分支
```bash
git checkout master
```
合并分支
```bash
git status # 查看分支当前状况,如当前在debug分支
git merge master # 与master尝试合并，如果不能自行合并可能会有conflict，需要手动solve.合并后两个分支代码同步
# 然后再commit
```

[back to top](#top)

<a id="5"></a>

## **Commits**

提交local commits
```bash
git add .
git commit -m 'Second commit'
git push origin eric-debug
```
Abort Changes
1. 放弃本地的修改
```bash
git stash # 可以之后被恢复的
git checkout FILE  # 放弃某个指定文件的修改
git reset --hard  # 强制放弃当前的所有修改
```
2. 回退到之前的commit
```bash
git reset --hard COMMIT_HASH # 回退到该commit，并清除中间的commits
# git push origin BRANCH --force  # 并将该状态提交到origin。注意:这样将强行将origin的代码替换，谨慎使用
```
[back to top](#top)

<a id="6"></a>

## **Pull From a PR**

1. fork branch
2. clone到local
3. 设置upstream上游repo
4. git pull from a pull request with ID and create a new branch(BRANCHNAME)
```bash
git fetch upstream pull/<ID>/head:<BRANCHNAME>
git checkout <BRANCHNAME>
```
5. 如果PR有更新，需要pull来与PR同步
```bash
git pull upstream pull/<ID>/head
```

[back to top](#top)
