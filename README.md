# compilers-2015

Компилятор в ассемблер

в target лежит CW-1.0-jar-with-dependencies.jar

запустить сам компилятор:
  java -jar CW-1.0-jar-with-dependencies.jar <input_file.txt> output.asm
  
запустить ассемблерный код (NASM32)
  nasm -f elf -l output.lst output.asm && gcc -m32 -o prog output.o && ./prog
  
