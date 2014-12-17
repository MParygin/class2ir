#Compiler Java Byte Code to LLVM IR assembler

This project is the compiler from class files (Java byte code) to LL files (LLVM IR assembler).
Result files can be compiled by llvm-as to standalone binary ELF files.

Features:

* No JDK, no JVM
* Linux x86_64 target arch
* Extreme small size (~10-20 kB ordinary program)
* Use glibc
* Use clang for system object

At this moment project in active development, many things does not work.
