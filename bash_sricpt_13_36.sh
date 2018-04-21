#!/bin/bash

#SBATCH -p main

#SBATCH -J graph_36

#SBATCH -N 1
#SBATCH --ntasks 1

#SBATCH --ntasks-per-node=1

#SBATCH --cpus-per-task 6
#SBATCH --mem 10GB

#SBATCH -t 48:00:00
#SBATCH -D /gpfs/hpchome/miron/thesis/graphs13/

#SBATCH --output=/gpfs/hpchome/miron/thesis/graphs13/tulemus13_36.out

module load java-1.8.0_40
module load jdk-1.8.0_25

cd Graatsiline-Graaf-Arvjada/src/

java Graatsilised_UUS3 36