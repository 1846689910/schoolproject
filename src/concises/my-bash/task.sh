#!/bin/bash

# refer to: https://github.com/LeCoupa/awesome-cheatsheets/blob/master/languages/bash.sh
# CTRL+C  # halts the current command
# exit    # logs out of current session

env                 # displays all environment variables
echo $SHELL         # displays the shell you're using
echo $BASH_VERSION  # displays bash version

#bash                # if you want to use bash (type exit to go back to your previously opened shell)
whereis bash        # finds out where bash is on your system
which bash          # finds out which program is executed as 'bash' (default: /bin/bash, can change across environments)
clear               # clears content on window (hide displayed lines)
# MAC: command + K  # completely clear terminal

echo $PATH          # displays shell
$(PATH=$PATH:/Users) # Temporarily append add another path to shell path. valid for current terminal session

# Define colors:
# Reset
Reset="\033[0m"       # no color
# Regular Colors
Black="\033[0;30m"        # Black
Red="\033[0;31m"          # Red
Green="\033[0;32m"        # Green
Yellow="\033[0;33m"       # Yellow
Blue="\033[0;34m"         # Blue
Purple="\033[0;35m"       # Purple
Cyan="\033[0;36m"         # Cyan
White="\033[0;37m"        # White


# use `which bash > hello.sh` to generate a file
greeting="welcome" # variable, do not include space around `=`
greeting1=welcome
user=$(whoami) # `whoami` will give the local username,
# execute bash command or bash function: $(whoami), $(fn),
# refer to bash variable: $VAR or ${VAR}
    echo -e ${Green}I am $(whoami), I am ${user}, I am $user
    echo -e "I am $(whoami), I am ${user}, I am $user $Reset" # equal to above
day=$(date +%A) # use self defined variable as $var or ${var}
hello=hello_${day}_123
myDir=${user}_tmp
txtFile=${myDir}/tmp.txt

# The function total_files reports a total number of files for a given directory.
function total_files {
        find $1 -type f | wc -l
}
# The function total_directories reports a total number of directories
# for a given directory.
function total_directories {
        find $1 -type d | wc -l
}

echo "$greeting back $user! Today is $day, which is the best day of the entire week!"
echo "Your Bash shell version is: $BASH_VERSION. Enjoy!"
rm -rf ${user}_tmp

echo "we are in $(pwd)"
mkdir $myDir
mkdir ${myDir}/tar_tmp

touch $txtFile # create tmp.txt
chmod 777 $txtFile # user group other all able to read/write/execute
echo "write some data to $txtFile via bash echo" >> $txtFile

# tar -fcz A B # compress B to A in gzip format
tar -fcz ${myDir}/tar_tmp/tmp.tar.gz $txtFile
rm -rf $txtFile
# tar -fxz A B # decompress A to B
tar -fxz ${myDir}/tar_tmp/tmp.tar.gz ${myDir}

# tar -fcj A B # compress B to A in bzip2 format
tar -fcj ${myDir}/tar_tmp/tmp.tar.gz $txtFile
rm -rf $txtFile
# tar -fxj A B # decompress A to B
tar -fxj ${myDir}/tar_tmp/tmp.tar.gz ${myDir}

echo -n "Files to be included:"
total_files ./
echo -n "Directories to be included:"
total_directories ./

echo "numeric comparison:"
num1=100
num2=30
[ $num1 -gt $num2 ]
echo $? # 0 true, 1 false
[ 100 -gt 30 ]
echo $?

# conditional statment
function conditions {
    local num_a=100
    local num_b=200
    if [ $num_a -lt $num_b ]; then # if [...]; then
        echo "$num_a is less than $num_b!"
        echo "I am also a part of result"
        ls ./
    elif [ $num_a -gt $num_b ]; then
        echo "$num_a is greater than $num_b"
    else
        echo "$num_a is equal to $num_b!"
    fi

    if [ -e ${txtFile} ]; then
        echo "$txtFile exists"
    else
        echo "$txtFile does not exists"
    fi
}

result=$(conditions)
echo $result
echo $(conditions)

# positional arguments
function args_test {
    echo "$1, $2, $3"
    echo "you passed in $# arguments"
    echo "alternative way to print all arguments $*"
}
echo $(args_test hello world good)

# loops
function for_loops {
    echo -e "$Green for loops: $Reset"
    for i in 1 2 3; do
        echo $i
    done
    for i in $(ls); do
        echo $i
    done
}
for_loops

function while_loops {
    echo -e "$Green while loops: $Reset"
    local counter=0
    while [ $counter -lt 3 ]; do
        let counter+=1
        echo $counter
    done
}
while_loops

function until_loops {
    echo -e "$Green until loops: $Reset"
    local counter=6
    until [ $counter -lt 3 ]; do
        let counter-=1
        echo $counter
    done
}
until_loops

# expr command
echo -e "$Green expr command: $Reset"
echo $(expr 1 + 1)
echo $(expr 3 \* 2) # should escape * for multiply

# let command: evaluates a mathematical expression and stores its result into a variable
function let_command {
    echo -e "$Green let command: $Reset"
    local c=0
    let a=c++
    echo a
}
let_command

# bc command
function bc_command {
    echo -e "$Green bc command: $Reset"
    echo "8.5/2.3"|bc
    echo "scale=2;8.5/2.3"|bc # 2 digits kept
}
bc_command
echo $(bc_command)

# src: src/a and src/b
# cp -R src target  # will make target/src/...
# cp -R src/ target  # will make target/a + target/b
cp -R ${myDir}/tar_tmp ${myDir}/tar_tmp1
cp -R ${myDir}/tar_tmp/ ${myDir}/tar_tmp1

cp $txtFile ${myDir}/tmp1.txt
mv ${myDir}/tmp1.txt ${myDir}/tmp2.txt  # move or rename file
diff $txtFile ${myDir}/tmp2.txt  # check two files difference

echo -e "$Green get dir size: $Reset"
echo $(du -h ${myDir}/tar_tmp1) # get dir size
echo $(du -hcs ${myDir}/tar_tmp1) # get dir size

echo -e "$Green get file size: $Reset"
echo $(wc -c $txtFile) # get file size/Byte

# find dir and file that match the name pattern
echo -e "$Green ls -d ~/D* $Reset"
echo $(ls -d ~/D*) # find all dir starts with src
echo -e "$Green ls ~/Downloads/*.zip $Reset"
echo $(ls ~/Downloads/*.zip)

echo -e "$Green find ~/Downloads -name *.zip $Reset"
echo $(find ~/Downloads -type f -name "*.zip")
echo -e "$Green find ~/Downloads -type d -name ccm* $Reset"
echo $(find ~/Downloads -type d -name "ccm*")

# find file by content
# r recursive, n: line number, w: whole word, --include=filename*.ext, --exclude=*.java, --exclude-dir
echo -e "$Green grep -rn . --exclude-dir={node_modules} --include=*.sh -e 'hello world' $Reset"
echo $(grep -rn . --exclude-dir={node_modules} --include=*.sh -e "hello world")
echo $(grep -rn .. --exclude-dir={node_modules} -e "hello world")

# find content in a specific file
echo -e "$Green grep hello <PATH> $Reset"
echo $(grep "hello" ./task.sh)

rm -rf ${myDir}
