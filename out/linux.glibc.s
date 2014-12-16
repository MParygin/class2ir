	.file	"linux.glibc.bc"
	.text
	.globl	linux_glibc_put_I
	.align	16, 0x90
	.type	linux_glibc_put_I,@function
linux_glibc_put_I:                      # @linux_glibc_put_I
	.cfi_startproc
# BB#0:
	pushq	%rax
.Ltmp1:
	.cfi_def_cfa_offset 16
	movl	%edi, %eax
	movl	%eax, 4(%rsp)
	movl	$.L.str, %edi
	movl	%eax, %esi
	xorb	%al, %al
	callq	printf
	popq	%rax
	ret
.Ltmp2:
	.size	linux_glibc_put_I, .Ltmp2-linux_glibc_put_I
	.cfi_endproc

	.globl	linux_glibc_put_J
	.align	16, 0x90
	.type	linux_glibc_put_J,@function
linux_glibc_put_J:                      # @linux_glibc_put_J
	.cfi_startproc
# BB#0:
	pushq	%rax
.Ltmp4:
	.cfi_def_cfa_offset 16
	movq	%rdi, %rax
	movq	%rax, (%rsp)
	movl	$.L.str1, %edi
	movq	%rax, %rsi
	xorb	%al, %al
	callq	printf
	popq	%rax
	ret
.Ltmp5:
	.size	linux_glibc_put_J, .Ltmp5-linux_glibc_put_J
	.cfi_endproc

	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	 "int %d\n"
	.size	.L.str, 8

	.type	.L.str1,@object         # @.str1
.L.str1:
	.asciz	 "long %ld\n"
	.size	.L.str1, 10


	.section	".note.GNU-stack","",@progbits
