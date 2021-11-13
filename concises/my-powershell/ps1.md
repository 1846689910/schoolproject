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

```ps1
function getColName($line){
    $foundColName = $line -match '\[(.+?)\]'
    if ($foundColName -And ...) {
        return $matches[1]
    } else {
        return ""
    }
}
function getInsertedContent($colName, $lineContent, $tableName, $simplifiedTableName){
    return "IF NOT EXISTS (SELECT Column_Name FROM DB1 WHERE Column_Name = '$colName' AND Table_Name = '$simplifiedTableName' ) `n`tBEGIN `n`t`tALTER TABLE $tableName ADD $lineContent `n`tEND"
}


$colName = getColName $line
```


[back to top](#top)


## Conditional

```ps1
$value = Get-MysteryValue
if ( 5 -eq $value )
{
    # do something
}
```

```
-eq case-insensitive equality
-ieq case-insensitive equality
-ceq case-sensitive equality

-ne case-insensitive not equal
-ine case-insensitive not equal
-cne case-sensitive not equal

-gt greater than
-igt greater than, case-insensitive
-cgt greater than, case-sensitive
-ge greater than or equal
-ige greater than or equal, case-insensitive
-cge greater than or equal, case-sensitive
-lt less than
-ilt less than, case-insensitive
-clt less than, case-sensitive
-le less than or equal
-ile less than or equal, case-insensitive
-cle less than or equal, case-sensitive

-like case-insensitive wildcard
-ilike case-insensitive wildcard
-clike case-sensitive wildcard
-notlike case-insensitive wildcard not matched
-inotlike case-insensitive wildcard not matched
-cnotlike case-sensitive wildcard not matched

-match case-insensitive regex
-imatch case-insensitive regex
-cmatch case-sensitive regex
-notmatch case-insensitive regex not matched
-inotmatch case-insensitive regex not matched
-cnotmatch case-sensitive regex not matched

-is of type
-isnot not of type

-contains case-insensitive match
-icontains case-insensitive match
-ccontains case-sensitive match
-notcontains case-insensitive not matched
-inotcontains case-insensitive not matched
-cnotcontains case-sensitive not matched

-in case-insensitive match
-iin case-insensitive match
-cin case-sensitive match
-notin case-insensitive not matched
-inotin case-insensitive not matched
-cnotin case-sensitive not matched
```

```ps1
$value = 'S-ATX-SQL01'
if ( $value -like 'S-*-SQL??') # *表示匹配多个字符, ?表示匹配一个字符
{
    # do something
}

$array = 1..6
if ( 3 -in $array )
{
    # do something
}
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

