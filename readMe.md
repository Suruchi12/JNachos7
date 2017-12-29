# Multiprogramming in JNachos

#### Operating Systems
#### CIS 657
#### Lab 1 

## Program Arguments:
Unzip the file JNachosLabLab2Solution into your workspace and use the option new and project from existing workspace to run in IntelliJ
for eclipse
For running this project, Create a new project from an existing source and uncheck the "use default location" box and find the directory where your project is.


## My program arguements used to run the project
IDE Used:IntelliJ.Eclipse
-x /Users/suruchisingh/Desktop/POS/JNachosLab2Solution/test/sort,/Users/suruchisingh/Desktop/POS/JNachosLab2Solution/test/matmult
Output can be found in output.txt

## Workings of the Project
This lab implements Multi-Programming in JNachos.
When you give two input files such as ../test/sort and ../test/matmult, the two programs will run simultaneously.

Created a new Java file called parentfork.java that would allocate address space for the forked process.
Copied the registers from the child process to the machine and ran the child process.
This enables the Machine.run to run for two processes. Hence, enabling Multiprogramming.
