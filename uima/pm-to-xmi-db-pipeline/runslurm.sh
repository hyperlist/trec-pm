#!/bin/bash
#SBATCH --mem 35g
#SBATCH --cpus-per-task 28 
#SBATCH -J PmXmiImport 

java -jar -Xmx6g ~/bin/jcore-pipeline-runner-base*.jar run.xml

