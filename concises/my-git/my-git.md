<h1>Git Concise</h1>

<a id="top"></a>

<h2>Contents</h2>

- [**创建 git repository**](#创建-git-repository)
  - [Git Config Setting](#git-config-setting)
- [**git clone**](#git-clone)
- [**Fork Others' Repo**](#fork-others-repo)
- [**Branches**](#branches)
- [**Commits**](#commits)
  - [remove files in commit before push](#remove-files-in-commit-before-push)
  - [clean untracked files](#clean-untracked-files)
- [**Pull From a PR**](#pull-from-a-pr)
  - [fetch a remote branch](#fetch-a-remote-branch)
- [**Rebase**](#rebase)
  - [**merge serveral history commits**](#merge-serveral-history-commits)
  - [change commit author](#change-commit-author)
  - [**Failure fix:**](#failure-fix)
- [SSH Key](#ssh-key)
  - [generate ssh key](#generate-ssh-key)
  - [add ssh key](#add-ssh-key)
  - [try connect](#try-connect)

<a id="1"></a>

## **创建 git repository**

先在 GitHub 上创建一个 repository，然后进入到 project 的根目录

```bash
git init
git add .
git commit -m 'First commit'
git remote add origin <remote repository URL> # now your remote is origin/master
git remote -v
git push origin master
```

<a id="1-1"></a>

### Git Config Setting

needs to check your git `name` and `email` before doing any commit, if you work on multiple accounts

show the `name` and `email`

```bash
git config user.name
git config user.email
```

set `name` and `email` for one repository

```bash
git config user.name "NAME"
git config user.email "EMAIL"
```

set `name` and `email` globally

```bash
git config --global user.name "NAME"
git config --global user.email "EMAIL"
```

[back to top](#top)

<a id="2"></a>

## **git clone**

查看 remote 的 repo 的 https 的 url

```bash
git clone <URL>
```

[back to top](#top)

<a id="3"></a>

## **Fork Others' Repo**

1. 先在 github 上 fork 别人的 repo
2. clone the forked repo to local:

```bash
git clone <forked_your_repo_URL>
```

3. set others' repo as your repo's `upstream` repo(Your repo is `origin`, while others' repo is `upstream`)

```bash
git remote add upstream https://github.com/ORIGINAL-DEV-USERNAME/REPO-YOU-FORKED-FROM.git
git branch -r
git fetch upstream  # fetch 远端的所有分支和commit, git fetch upstream master只获取远端的master分支
```

4. keep your `origin` updated with `upstream`.

```bash
git fetch upstream
git reset --hard upstream/master # 放弃本地的修改，将branch与upstream/master的HEAD同步
# git pull upstream master # 从upstream/master里pull changes
git push origin master --force  # 强制将upstream/master的代码放入remote的repo
```

5. check remote branch and their urls

```bash
git remote -v
```

6. change remote branch url

the following example change the remote branch `upstream` url. this method could be used to change to other people's forked branch from source repo

```bash
git remote set-url upstream https://github.com/ORIGINAL-DEV-USERNAME/REPO-YOU-FORKED-FROM.git
```

7. rename a remote repo

```bash
git remote rename OLD_REMOTE_NAME NEW_NAME
```

8. remove a remote repo

```bash
git remote rm REMOTE_NAME # or use `git remote remove`
```

9. set this branch's tracking upstream

```bash
git branch --set-upstream-to=ORIGIN/<REMOTE_BRANCH> CURRENT_BRANCH
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

修改分支名字

```bash
git branch -m old-name new-name
git branch -m new-name # only for current branch
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

提交 local commits

```bash
git add .
git commit -m 'Second commit'
git push
' git push --set-upstream origin eric-debug
```

在 commit 前查看改变的内容

```bash
git diff
git diff <File> # check a specific file
```

查看有 conflict 的部分

```bash
git diff --check
```

Abort Changes

1. 放弃本地的修改

```bash
git stash # 可以之后被恢复的, 等同于git stash push
# git stash pop  # recover the changes in last `git stash`
git checkout FILE  # 放弃某个指定文件的修改
git reset --hard  # 强制放弃当前的所有修改
```

2. 回退到之前的 commit

```bash
git reset --hard COMMIT_HASH # 回退到该commit，并清除中间的commits
# git push origin BRANCH --force  # 并将该状态提交到origin。注意:这样将强行将origin的代码替换，谨慎使用
```

3. 返回之前的某个 commit()

```bash
git checkout COMMIT_HASH # 返回该commit,并未清除任何commits
# git checkout CURRENT_BRANCH # 可以再回到该branch的头部
```

4. stash untracked changes

```bash
git stash # stash untracked changes
git stash pop # pop up the changes to current branch
```

[back to top](#top)

<a id="5-1"></a>

### remove files in commit before push

- list files in commit

```bash
git diff-tree --no-commit-id --name-only -r <commit-Hash>
```

- remove files in commit

```bash
git rm --cached name_of_a_giant_file # full path
git rm --cached name_of_another_giant_file
git rm --cached folder_name -r
git commit --amend -CHEAD
git push
```

[back to top](#top)

### clean untracked files

```bash
git clean -f
```

[back to top](#top)

<a id="6"></a>

## **Pull From a PR**

1. fork branch
2. clone 到 local
3. 设置 upstream 上游 repo
4. git pull from a pull request with ID and create a new branch(BRANCHNAME)

```bash
git fetch upstream pull/<ID>/head:<BRANCHNAME>
git checkout <BRANCHNAME>
```

5. 如果 PR 有更新，需要 pull 来与 PR 同步

```bash
git pull upstream pull/<ID>/head
```

[back to top](#top)

<a id="6-1"></a>

### fetch a remote branch

```bash
git fetch origin <BRANCH>
git checkout <BRANCH>
```

```bash
git fetch upstream <BRANCH>
git checkout <BRANCH>
```

[back to top](#top)

<a id="7"></a>

## **Rebase**

### **merge serveral history commits**

```bash
git log
```

then you will get the recent commits history from `latest` to `oldest`:

```
    acd13f  --- latest
    57d8ba
    16fc2d
    77q0bs  --- oldest
```

if you want to merge **`16fc2d`**, **`57d8ba`** and **`acd13f`** together, use

```bash
git rebase -i 7710bs # one commit before the ones you want to merge together
# or `git rebase -i HEAD~[3]`
# git rebase --abort  # abort rebasing
```

then, a tip will show how to do with these commits(**from `oldest` to `latest`**), change them to `s` or `squash` **except for the first one**, because you want to merge all the rest to the first one.

```
    pick 16fc2d
    s 57d8ba
    s acd13f
```

then `ESC` followed by `:wq`. your squash finished.
Then, replace the remote by forcely push

```bash
git push origin BRANCH --force
```

### change commit author

```bash
git rebase -i <earlier_commit>
git rebase -i --root # change from the root/first commit
```

choose several commits that you need to change author, then change them to `e` or `edit`

```bash
git commit --amend --author="1846689910 <email@address.com>"
```

then

```bash
git rebase --continue
```

finally

```bash
git push origin <branch> --force
```

**Exit rebase**

```bash
git rebase --abort
```

### **Failure fix:**

1. `Cannot 'squash' without a previous commit`

```bash
git rebase --edit-todo
# change the first one from squash to `r`
git reset --soft HEAD^
git commit --amend
git rebase --continue
```

[back to top](#top)

<a id="8"></a>

## SSH Key

After install git, generate a ssh key on your personal machine, keep in touch with remote repo. [detailed guide](https://help.github.com/en/enterprise/2.15/user/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)

### generate ssh key

1. open terminal
2. input command with your github email

```bash
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

3. As the following request for `directory` and `passphrase`

### add ssh key

copy the content of generated `.ssh/id_rsa.pub`

click `New SSH Key` in github -> settings -> `SSH and GPG keys`

put a title and paste the key content

### try connect

in terminal, use command

```bash
ssh -T git@github.com
```

when you see `The authenticity of host 'github.com (192.30.253.112)' can't be established.` and ask for connection, type `yes`. Then reconnect.

when you see `You’ve successfully authenticated, but GitHub does not provide shell access`, it means successful connection to github repo.

or try

```bash
ssh-add ~/.ssh/id_rsa
```
- if you rename the `is_ras` file, you need to add that file by this command

[back to top](#top)
