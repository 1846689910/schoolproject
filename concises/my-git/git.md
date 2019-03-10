# **Git Concise**

<a id="top"></a>

## **Contents**

[**Git Basics**](#1)

[**Undoing Changes**](#2)

[**Rewriting Git History**](#3)

[**Git Branches**](#4)

[**Remote Repositories**](#5)

[**Git Reset**](#6)

[**Git Rebase**](#7)

[**Git Pull**](#8)

[**Git Push**](#9)

[**Git Diff**](#10)

[**Git Config**](#11)

[**Git Log**](#12)





<a id="1"></a>

## **Git Basics**


| command                     | description                                                                                                                                 |
| :-------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------ |
| `git init DIR`              | Create empty Git repo in specified directory. Run with no arguments to initialize the current directory as a git repository.                |
| `git clone REPO`            | Clone repo located at REPO onto local machine. Original repo can be located on the local filesystem or on a remote machine via HTTP or SSH. |
| `git config user.name NAME` | Define author name to be used for all commits in current repo. Devs commonly use --global flag to set config options for current user.      |
| `git add DIR`               | Stage all changes in directory for the next commit. Replace directory with a file to change a specific file.                                |
| `git commit -m "message"`   | Commit the staged snapshot, but instead of launching a text editor, use message as the commit message.                                      |
| `git status`                | List which files are staged, unstaged, and untracked.                                                                                       |
| `git log`                   | Display the entire commit history using the default format. For customization see additional options.                                       |
| `git diff`                  | Show unstaged changes between your index and working directory                                                                              |

[back to top](#top)

<a id="2"></a>

## **Undoing Changes**

| command             | description                                                                                                                         |
| :------------------ | :---------------------------------------------------------------------------------------------------------------------------------- |
| `git revert COMMIT` | Create new commit that undoes all of the changes made in commit, then apply it to the current branch.                               |
| `git reset FILE`    | Remove file from the staging area, but leave the working directory unchanged. This unstages a file without overwriting any changes. |
| `git clean -n`      | Shows which files would be removed from working directory. Use the -f flag in place of the -n flag to execute the clean.            |

[back to top](#top)

<a id="3"></a>

## **Rewriting Git History**

| command              | description                                                                                                                           |
| :------------------- | :------------------------------------------------------------------------------------------------------------------------------------ |
| `git commit --amend` | Replace the last commit with the staged changes and last commit combined. Use with nothing staged to edit the last commit’s message. |
| `git rebase <base>`  | Rebase the current branch onto base. base can be a commit ID, a branch name, a tag, or a relative reference to HEAD.                  |
| `git reflog`         | Show a log of changes to the local repository’s HEAD. Add --relative-date flag to show date info or --all to show all refs.          |

[back to top](#top)
