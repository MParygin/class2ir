	.file	"runner.bc"
	.text
	.globl	main
	.align	16, 0x90
	.type	main,@function
main:                                   # @main
	.cfi_startproc
# BB#0:
	pushq	%rax
.Ltmp1:
	.cfi_def_cfa_offset 16
	movl	$.L.str, %edi
	callq	puts
	xorb	%al, %al
	callq	test_Test__clinit_
	xorb	%al, %al
	callq	test_Test_main
	xorl	%eax, %eax
	popq	%rdx
	ret
.Ltmp2:
	.size	main, .Ltmp2-main
	.cfi_endproc

	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	 "Content-Type: text/plain\n"
	.size	.L.str, 26


	.section	".note.GNU-stack","",@progbits
