l<h1>powershell-concise</h1>

<a id="top"></a>

<h2>contents</h2>

- [Basic](#basic)
  - [定义变量](#定义变量)
  - [算术计算](#算术计算)
  - [Conditional Execution](#conditional-execution)
- [String](#string)
  - [字符串长度](#字符串长度)
  - [字符串片段`.substring(beg,len)`](#字符串片段substringbeglen)
  - [大小写转换](#大小写转换)
  - [字符串匹配](#字符串匹配)
  - [字符串替换](#字符串替换)
  - [String split to array](#string-split-to-array)
- [Loop](#loop)
- [Function](#function)
- [Conditional](#conditional)
- [Array](#array)
  - [define array](#define-array)
  - [push/concatenation](#pushconcatenation)
  - [remove an element](#remove-an-element)
  - [get element and len](#get-element-and-len)
  - [get slice of arr](#get-slice-of-arr)
  - [array joined as a string](#array-joined-as-a-string)
  - [read file as array(each line as a element)](#read-file-as-arrayeach-line-as-a-element)
- [Dictionary](#dictionary)
- [Miscellaneous](#miscellaneous)
  - [random number](#random-number)
  - [calculation precision](#calculation-precision)
  - [save to 5 digits after dot](#save-to-5-digits-after-dot)
  - [Read user input as a variable](#read-user-input-as-a-variable)
  - [User select from a menu](#user-select-from-a-menu)
- [File](#file)
  - [read a file first 5 lines](#read-a-file-first-5-lines)
  - [read a file last 5 lines](#read-a-file-last-5-lines)
  - [read file](#read-file)
  - [read file line by line](#read-file-line-by-line)
  - [read file by lines, split each line to array](#read-file-by-lines-split-each-line-to-array)
  - [write content to a file](#write-content-to-a-file)

## Basic

### 定义变量

```ps1
$a = 123
$b = $a
echo $b
echo "hello world"
```

### 算术计算

```ps1
$a = 123
$b = $a

$c = $a + $b
echo $c # 246
echo ((1+1))
```

### Conditional Execution

```ps1
git commit && git push # 当前一个命令成功后再执行后一个命令
git commit || echo "commit failed" # 如果前一个不成功就执行后一个命令
sleep 5; echo hi # 依次执行命令, 休眠5秒后执行echo
sleep 5& echo hi # &相当于新开一个进程, 瞬间执行了echo
```

单行注释 `# this is comment`
多行注释

```ps1
<#
This is comment block
all comment lines
#>
```

[back to top](#top)

## String

### 字符串长度

```ps1
$name="eric"
$len = $name.Length
echo $len
```

### 字符串片段`.substring(beg,len)`

```ps1
$var = 'Hello World'
$result = $var.substring(0, 5)
```

### 大小写转换

```ps1
$var = 'Hello World'
$a = $var.ToUpper()
$b = $var.ToLower()
echo $a, $b
```

### 字符串匹配

```ps1
$line = "a,b,c"
$foundContent = $line -match '([^,]+),?'
if ($foundContent) {
  echo "founded"
}
```

### 字符串替换

```ps1
$line = "a, b,  c"
$newLine = $line -replace "a","s"
echo $newLine
```

### String split to array

- to char array
```ps1
$var = "Hello,World,Welcome"
$chars = $var.toCharArray()
foreach ($char in $chars) {
  echo $char
}
```
- split by char
```ps1
$var = "Hello,World,Welcome"
$strs = $var.split(",")
foreach ($s in $strs) {
  echo $s
}
```

[back to top](#top)

## Loop

basic

list all the files or dirs in current directory

```ps1
$files = Get-ChildItem -Recurse -Path "C:\Users\eric\123\sub" -Include *.txt

foreach($file in $files) {
  echo $file.FullName
}
for($i = 0; $i -lt $files.Count; $i++) {
  echo $files[$i].FullName
}
```

from 0 to 5

```ps1
for ($i = 0; $i -lt 5; $i ++) {
  echo $i
}
for ($i = 0; $i -lt 5; $i +=2) {
  echo $i
}
```

loop array

```ps1
$arr = @("hello", "world")

for ($i = 0; $i -lt $arr.length; $i ++) {
  echo $arr[$i]
}
for ($i = 0; $i -le $arr.length - 1; $i ++) {
  echo $arr[$i]
}
```

[back to top](#top)

## Function

Basic

```bash
myfunc() {
  echo "hello $1"
}
# Same as above (alternate syntax)
function myfunc() {
  echo "hello $1"
}
myfunc "John"
```

with return values(all the `echo` in the function combined together)

```bash
myfunc() {
  local myresult="some"
  echo $myresult $1
}
result="$(myfunc hello)"
echo $result
```

within the function, there are some arguments:

- `$#`: number of arguments passed in function
- `$*`: all arguments
- `$@`: all arguments, starting from first
- `$1`: the first argument

  for example, to print all arguments or use `$*`

```bash
myfunc() {
  for i in $@; do
    echo $i
  done
}
myfunc 1 2 3
```

raise errors

```bash
myfunc() {
  return 0
}
if myfunc; then
  echo "success" # 0 -> true/success, 1 -> false/failure
else
  echo "failure"
fi
```

[back to top](#top)

<a id="4"></a>

## Conditional

`[[ is actually a command/program]]` that returns either 0 (true) or 1 (false). Any program that obeys the same logic (like all base utils, such as grep(1) or ping(1)) can be used as condition, see examples.

| Command               |       Description        |
| --------------------- | :----------------------: |
| `[[ -z str ]]`        |       empty string       |
| `[[ -n str ]]`        |     non-empty string     |
| `[[ str1 == str2 ]]`  |          equal           |
| `[[ str1 != str2 ]]`  |        not equal         |
| `[[ num1 -eq num2 ]]` |          equal           |
| `[[ num1 -ne num2 ]]` |        not equal         |
| `[[ str1 -lt str2 ]]` |        less than         |
| `[[ str1 -le str2 ]]` |    less than or equal    |
| `[[ str1 -gt str2 ]]` |       greater than       |
| `[[ str1 -ge str2 ]]` |  greater than or equal   |
| `[[ str1 =~ str2 ]]`  |          regexp          |
| `(( num1 < num2 ))`   |    numeric conditions    |
| `[[ ! EXPR ]]`        |           Not            |
| `[[ X ]] && [[Y]]`    |           And            |
| `[[ X ]] || [[Y]]`    |            Or            |
| `[[ -o noclobber ]]`  | If OPTIONNAME is enabled |

File Conditions

| Command                 |      Description       |
| ----------------------- | :--------------------: |
| `[[ -e FILE ]]`         |         exists         |
| `[[ -s FILE ]]`         |    size is > 0 byte    |
| `[[ -r FILE ]]`         |        readable        |
| `[[ -w FILE ]]`         |        writable        |
| `[[ -x FILE ]]`         |       executable       |
| `[[ -h FILE ]]`         |        symlink         |
| `[[ -d FILE ]]`         |       directory        |
| `[[ -f FILE ]]`         |          file          |
| `[[ file1 -nt file2 ]]` | file1 newer than file2 |
| `[[ file1 -ot file2 ]]` | file1 older than file2 |
| `[[ file1 -ef file2 ]]` |       same files       |

examples

```bash
if ((1 < 0)); then
  echo false
fi

if (grep -q -re "hello" ./); then
  echo found
fi

str="hello"
if [[ -z str ]]; then
  echo "str is empty"
elif [[ -n str ]]; then
  echo "str is not empty"
fi

# file existence
if [[ -e "README.md" ]]; then
  echo "file exists"
fi
```

Regex

bash does not support regex like `\w`, `\d` ... those we familiar in other languages

```bash
str="abc"
if [[ str =~ .+ ]]; then
  echo "yep"
else
  echo "no"
fi
```

[back to top](#top)

<a id="5"></a>

## Array

### define array

```bash
Fruits=('Apple' 'Banana' 'Orange')
Fruits[0]="Apple"
Fruits[1]="Banana"
Fruits[2]="Orange"
```

### push/concatenation

```bash
arr+=("kiwi")
# or
arr=("${arr[@]}" "Watermelon")
```

### remove an element

```bash
unset arr[1] # then arr[1] will be empty, other elements are still in their position
```

remove by regex match

```bash
arr=(${arr[@]/Ap*/})
```
### get element and len

get element `${arr[0]}`

get length of array `${#arr[@]}`

get the string length of ith element `${#arr[0]}`

### get slice of arr

```bash
arr=("a" "b" "c" "d" "e")
arr1=${arr[@]:1:2} # from 1th element, take length of 2

if [[ arr == arr1 ]]; then
  echo equal
else
  echo not equal
fi
```

### array joined as a string

```bash
arr=("apple" "banana")
bar=$(IFS=, ; echo "${arr[*]}")
echo $bar
```

```bash
join(){ # write as a function
  arr=$1
  echo $(IFS=$2;echo "${arr[*]}")
}
arr=("apple" "orange")
join $arr "|"
```

### read file as array(each line as a element)

```bash
arr=(`cat ".gitignore"`)
```

[back to top](#top)

<a id="6"></a>

## Dictionary

define a dictionary

```bash
declare -A dict # 定义一个dictionary为dict

dict[a]="b"
dict[c]="d"
```

get value by key `s=${dict[a]}`

get size of dictionary `len=${#dict[@]}`

get all values arr

```bash
values=${dict[@]}

for i in ${values[@]}; do
  echo $i
done
```

get all keys arr

```bash
keys=${!dict[@]}

for i in ${keys[@]}; do
  echo "$i -> ${dict[$i]}"
done
```

Iterate by keys or by values

```bash
for val in ${dict[@]} do
  echo $val
done

for key in ${!dict[@]} do
  echo $key
done
```

[back to top](#top)

<a id="7"></a>

## Miscellaneous

### random number

```bash
echo $((RANDOM%=200))
```

### calculation precision

```bash
num=$(bc -l <<< "100/3")
echo $num # 33.3333333333333333333
```

### save to 5 digits after dot

```bash
num=$(bc -l <<< "100/3")
s=$(printf "%.5f" $num)
echo $s # 33.33333
```

### Read user input as a variable

```bash
echo "What is your name?"
read name
echo the user name is $name
```

### User select from a menu

```bash
OPTIONS="option1 option2 Quit" # each option is separated by a space
# when running, user input 1 or 2 or 3
select opt in $OPTIONS; do
  if [[ $opt == "option1" ]]; then
    echo you choose option1
  elif [[ $opt == "option2" ]]; then
    echo you choose option2
  elif [[ $opt == "Quit" ]]; then
    echo "done"
    exit
  else
    clear
    echo bad option
  fi
done
```

[back to top](#top)

<a id="8"></a>

## File

### read a file first 5 lines

```bash
head -5 FILE

contents=$(head -5 README.md)
echo $contents
```

### read a file last 5 lines

```bash
tail -5 FILE

contents=$(tail -5 README.md)
echo $contents
```

### read file

```bash
contents=$(cat README.md)
# or
contents=$(< README.md)
```

### read file line by line

```bash
while read -r line; do
  echo $line
done < README.md
```

or prevent trimming of leading or trailing white spaces

```bash
readFile(){
  while IFS= read -r line; do
    echo $line
  done < $1
}

readFile README.md
```

### read file by lines, split each line to array

```bash
readLines(){
  while IFS= read -r line; do # read each line
    IFS=" " read -r -a arr <<< $line # each line splitted by space to an array
    echo ${arr[0]} ${#arr} # print first element and the length of arr
  done < $1
}

readLines README.md
```

### write content to a file

```bash
printf "Hello World" > a.txt
```

[back to top](#top)

