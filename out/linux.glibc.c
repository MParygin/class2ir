#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <unistd.h>

void linux_glibc_put_I(int value) {
  printf("int %d\n", value);
}

void linux_glibc_put_J(long value) {
  printf("long %ld\n", value);
}

