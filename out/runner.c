#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <unistd.h>

void test_Test__clinit_();
void test_Test_main();

int main() {
  puts("Content-Type: text/plain\n");
  test_Test__clinit_();
  test_Test_main();
}

