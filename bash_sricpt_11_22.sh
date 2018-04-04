#!/bin/bash

#SBATCH -p main

#SBATCH -J graph_22

#SBATCH -N 1
#SBATCH --ntasks 1

#SBATCH --ntasks-per-node=1

#SBATCH --cpus-per-task 5
#SBATCH --mem 10GB

#SBATCH -t 05:00:00
#SBATCH -D /gpfs/hpchome/miron/thesis/graphs11/

#SBATCH --output=/gpfs/hpchome/miron/thesis/graphs11/tulemus12_22.out

#SBATCH --mail-type=ALL
#SBATCH --mail-user=miron.storozhev96@gmail.com

module load java-1.8.0_40
module load jdk-1.8.0_25

cd Graatsiline-Graaf-Arvjada/src/

java Graatsilised_UUS3 22