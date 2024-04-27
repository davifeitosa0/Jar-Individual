#!/bin/bash

yes | sudo adduser cliente_medtech

echo "cliente_medtech:medtech123" | sudo chpasswd


# Atualizar
echo "\n\nATUALIZANDO O SISTEMA\n\n"
#yes | sudo apt update
#yes | sudo apt upgrade

# NMON
echo "\n\nINSTALANDO NMON\n\n"
yes | sudo apt install nmon
sudo nmon -f -s 5 -c 10

# JDK
java --version

if [ $? = 0 ]
        then
                echo "\n\nJAVA JÁ ESTÁ INSTALADO\n\n"
        else
                echo "\n\nJAVA NÃO ENCONTRADO! INSTALANDO...\n\n"
                yes | sudo apt install openjdk-17-jre-headless
fi

# GitHub
echo "\n\nBUSCANDO DIRETÓRIO DA APLICAÇÃO...\n\n"
sudo ls  ../cliente_medtech/Jar-Individual

if [ $? = 0 ]
        then
                echo "\n\nDIRETÓRIO ENCONTRADO.\n\n"
        else
                echo "\n\nDIRETÓRIO NÃO ENCONTRADO. INSTALANDO APLICAÇÃO...\n\n"
                sudo git clone https://github.com/davifeitosa0/Jar-Individual.git ../cliente_medtech/Jar-Individual
fi
sudo chmod 111 script.sh
sudo chmod 111 ../cliente_medtech/Jar-Individual

# MYSQL
echo "\n\nCONFIGURANDO BANCO DE DADOS\n\n"

yes | sudo apt install mysql-server
yes | sudo systemctl start mysql
yes | sudo systemctl enable mysql

SQL_SCRIPT="../cliente_medtech/Jar-Individual/script.sql"

sudo mysql < "$SQL_SCRIPT"

echo "\n\nTUDO CONFIGURADO! EXECUTE OS SEGUINTES COMANDOS PARA RODAR A APLICAÇÃO:\n 1- cd ../cliente_medtech/Jar-Individual \n 2- java -jar JarIndividualDavi.jar"
sudo su cliente_medtech