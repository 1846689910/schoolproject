l<h1>powershell-concise</h1>

<a id="top"></a>

<h2>contents</h2>

- [Basic](#basic)
  - [运行`*.ps1`文件](#运行ps1文件)
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
  - [get element and len](#get-element-and-len)
- [Dictionary](#dictionary)
- [Miscellaneous](#miscellaneous)
  - [random number](#random-number)
  - [calculation precision](#calculation-precision)
  - [Read user input as a variable](#read-user-input-as-a-variable)
- [File](#file)
  - [file content exists](#file-content-exists)
  - [list all the files or dirs in current directory](#list-all-the-files-or-dirs-in-current-directory)
  - [read file as array(each line as a element)](#read-file-as-arrayeach-line-as-a-element)
  - [read file line by line](#read-file-line-by-line)
  - [read file by lines, split each line to array](#read-file-by-lines-split-each-line-to-array)
  - [write content to a file](#write-content-to-a-file)

## Basic

### 运行`*.ps1`文件

```ps1
./a/b/c.ps1

./a/b/c.ps1 "hello" 1 2
echo $args.Length
echo $args[0]
```

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

## Array

### define array

```ps1
$fruits=@('Apple', 'Banana', 'Orange')
$fruits[0] = "hello"
echo $fruits[0]
```

### push/concatenation

```ps1
$fruits=@('Apple', 'Banana', 'Orange')
$fruits += "kiwi"
echo $fruits[3]
```

### get element and len

get element `$arr[0]`

get length of array `$arr.length`


[back to top](#top)

<a id="6"></a>

## Dictionary

define a dictionary

```bash
$map = @{ Number = 1; Shape = "Square"; Color = "Blue"}
echo $map["Number"]
echo $map["Shape"]
$map["aaa"] = "bbb"
echo $map["aaa"]
echo $hash.Number

$hash = [ordered]@{ Number = 1; Shape = "Square"; Color = "Blue"}
```

get value by key `s=$dict[a]` or `$dict.aaa`

get size of dictionary `$dict.count`

get all values arr

```ps1
$vals = $hash.values
foreach ($v in $vals) {
  echo $v
}
```

get all keys arr

```ps1
$keys = $hash.keys
foreach ($k in $keys) {
  echo $k
}
```

[back to top](#top)


## Miscellaneous

### random number

```ps1
$rand1 = Get-Random -Minimum -100 -Maximum 100
$rand2 = Get-Random -Minimum 10.7 -Maximum 29.3
echo $rand1
echo $rand2
```

### calculation precision

```ps1
$a = 111.5226

$a1 = [math]::Round($a)
echo $a1 # 112

$a2 = [math]::Round($a,2)
echo $a2 # 111.52

```

### Read user input as a variable

```ps1
$Server = Read-Host -Prompt 'Input your server  name'
$User = Read-Host -Prompt 'Input the user name'
$Date = Get-Date
Write-Host "You input server '$Server' and '$User' on '$Date'" 
```

[back to top](#top)

## File

### file content exists

```ps1
$ImportIdExists =  Select-String -Quiet -Pattern "\[ImportId\]" -LiteralPath $fileName
$TableNameExists =  Select-String -Quiet -SimpleMatch "$tableName" -LiteralPath $fileName
```

### list all the files or dirs in current directory

```ps1
$files = Get-ChildItem -Recurse -Path "C:\Users\eric\123\sub" -Include *.txt

foreach($file in $files) {
  echo $file.FullName
}
for($i = 0; $i -lt $files.Count; $i++) {
  echo $files[$i].FullName
}
```

### read file as array(each line as a element)

```ps1
$lines = Get-Content -LiteralPath "D:\schoolproject\temp\ps\AddNCIToScript.ps1"
foreach($line in $lines) {
  echo $line
}
```

### read file line by line

```ps1
foreach($line in Get-Content -LiteralPath $fileName){
  echo $line
}
```

### read file by lines, split each line to array

```ps1
foreach($line in Get-Content -LiteralPath $fileName){
  $strs = $line.split(",")
  foreach ($s in $strs) {
    echo $s
  }
}

```

### write content to a file

```ps1
Set-Content -LiteralPath $fileName -Value $fileContent
```

[back to top](#top)

