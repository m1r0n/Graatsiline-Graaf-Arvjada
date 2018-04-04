#!/bin/bash

#SBATCH -p main

#SBATCH -J graph_9

#SBATCH -N 1
#SBATCH --ntasks 1

#SBATCH --ntasks-per-node=1

#SBATCH --cpus-per-task 5
#SBATCH --mem 10GB

#SBATCH -t 20:00:00
#SBATCH -D /gpfs/hpchome/miron/thesis/graphs12/

#SBATCH --output=/gpfs/hpchome/miron/thesis/graphs12/tulemus12_9.out

module load java-1.8.0_40
module load jdk-1.8.0_25

cd Graatsiline-Graaf-Arvjada/src/

java Graatsilised_UUS3 9