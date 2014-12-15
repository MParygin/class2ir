TARGET=$(shell basename `pwd`)
LLS=$(wildcard *.ll)
CS=$(wildcard *.c)
ASMS=$(LLS:%.ll=%.s)
ASMS2=$(CS:%.c=%.s)

all: $(TARGET)

$(TARGET): $(ASMS) $(ASMS2)
	gcc -o $(TARGET) $(ASMS) $(ASMS2)
#	strip $(TARGET)

%.s: %.bc
	llc $<

%.bc: %.ll
	llvm-as $<

%.ll: %.c
	clang -S -emit-llvm -o $@ $<

clean:
	rm -f ./*.s
	rm -f $(TARGET)
