#!/bin/bash

#SBATCH -p main

#SBATCH -J graph_seq24

#SBATCH -N 1
#SBATCH --ntasks 1

#SBATCH --ntasks-per-node=1

#SBATCH --cpus-per-task 20
#SBATCH --mem 10GB

#SBATCH -t 20:00:00
#SBATCH -D /gpfs/hpchome/miron/thesis/

#SBATCH --output=/gpfs/hpchome/miron/thesis/Graatsiline-Graaf-Arvjada/tulemus12_24.out

#SBATCH --mail-type=ALL
#SBATCH --mail-user=miron.storozhev96@gmail.com

module load java-1.8.0_40
module load jdk-1.8.0_25

cd Graatsiline-Graaf-Arvjada/src/

java Graatsilised_UUS3 24