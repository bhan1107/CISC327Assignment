#!/bin/bash
var=1
for file in {1..85}
do
    value=$(sed '1d' ./input/"$file.txt")
#   echo "$value"
    java -jar $2 <<< "$value"
    output=$(cat ./tsf.txt)
    tsf=$(cat $3/"tsf$var.txt")
#   echo "$output"
#   echo "$tsf"

    if [ "$output" == "$tsf" ]
    then
       echo "!"
    else
       touch "result$var.txt"
       echo "$output" >> "result$var.txt"
       echo "$tsf" >> "result$var.txt"
    fi
    ((var++))
done
#$1 is file directory ./Input
#$2 is name of .jar file hereitis.jar
