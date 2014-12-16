	.file	"test.Test.bc"
	.text
	.globl	test_Test__init_
	.align	16, 0x90
	.type	test_Test__init_,@function
test_Test__init_:                       # @test_Test__init_
	.cfi_startproc
# BB#0:
	pushq	%rbx
.Ltmp2:
	.cfi_def_cfa_offset 16
.Ltmp3:
	.cfi_offset %rbx, -16
	movq	%rdi, %rbx
	callq	java_lang_Object__init_
	movl	$1, (%rbx)
	movl	$127, 4(%rbx)
	movl	$100, 8(%rbx)
	movl	$142, 12(%rbx)
	movq	$42, 16(%rbx)
	movabsq	$4602949035150289142, %rax # imm = 0x3FE0F5C28F5C28F6
	movl	$1471498160, 24(%rbx)   # imm = 0x57B547B0
	movq	%rax, 32(%rbx)
	movl	12(%rbx), %edi
	callq	linux_glibc_put_I
	popq	%rbx
	ret
.Ltmp4:
	.size	test_Test__init_, .Ltmp4-test_Test__init_
	.cfi_endproc

	.globl	test_Test_main
	.align	16, 0x90
	.type	test_Test_main,@function
test_Test_main:                         # @test_Test_main
	.cfi_startproc
# BB#0:
	pushq	%r14
.Ltmp8:
	.cfi_def_cfa_offset 16
	pushq	%rbx
.Ltmp9:
	.cfi_def_cfa_offset 24
	pushq	%rax
.Ltmp10:
	.cfi_def_cfa_offset 32
.Ltmp11:
	.cfi_offset %rbx, -24
.Ltmp12:
	.cfi_offset %r14, -16
	callq	java_lang_System_currentTimeMillis
	movq	%rax, %r14
	movl	$56, %edi
	callq	malloc
	movq	%rax, %rbx
	movq	%rbx, %rdi
	callq	java_lang_Object__init_
	movl	$1, (%rbx)
	movabsq	$4602949035150289142, %rax # imm = 0x3FE0F5C28F5C28F6
	movl	$127, 4(%rbx)
	movl	$100, 8(%rbx)
	movl	$142, 12(%rbx)
	movq	$42, 16(%rbx)
	movl	$1471498160, 24(%rbx)   # imm = 0x57B547B0
	movq	%rax, 32(%rbx)
	movl	12(%rbx), %edi
	callq	linux_glibc_put_I
	movl	$9, 12(%rbx)
	movl	$9, %edi
	callq	linux_glibc_put_I
	callq	java_lang_System_currentTimeMillis
	subq	%r14, %rax
	movq	%rax, %rdi
	callq	linux_glibc_put_J
	movl	$1111, %eax             # imm = 0x457
	xorl	%edi, %edi
	movb	test_Test_sl.b(%rip), %cl
	testb	%cl, %cl
	cmovneq	%rax, %rdi
	callq	linux_glibc_put_J
	movq	test_Test_singleton(%rip), %rax
	movq	16(%rax), %rdi
	callq	linux_glibc_put_J
	addq	$8, %rsp
	popq	%rbx
	popq	%r14
	ret
.Ltmp13:
	.size	test_Test_main, .Ltmp13-test_Test_main
	.cfi_endproc

	.globl	test_Test__clinit_
	.align	16, 0x90
	.type	test_Test__clinit_,@function
test_Test__clinit_:                     # @test_Test__clinit_
	.cfi_startproc
# BB#0:
	pushq	%rbx
.Ltmp16:
	.cfi_def_cfa_offset 16
.Ltmp17:
	.cfi_offset %rbx, -16
	movb	$1, test_Test_sl.b(%rip)
	movl	$56, %edi
	callq	malloc
	movq	%rax, %rbx
	movq	%rbx, %rdi
	callq	java_lang_Object__init_
	movl	$1, (%rbx)
	movl	$127, 4(%rbx)
	movabsq	$4602949035150289142, %rax # imm = 0x3FE0F5C28F5C28F6
	movl	$100, 8(%rbx)
	movl	$142, 12(%rbx)
	movq	$42, 16(%rbx)
	movl	$1471498160, 24(%rbx)   # imm = 0x57B547B0
	movq	%rax, 32(%rbx)
	movl	12(%rbx), %edi
	callq	linux_glibc_put_I
	movq	%rbx, test_Test_singleton(%rip)
	popq	%rbx
	ret
.Ltmp18:
	.size	test_Test__clinit_, .Ltmp18-test_Test__clinit_
	.cfi_endproc

	.type	test_Test_sl.b,@object  # @test_Test_sl.b
	.lcomm	test_Test_sl.b,1
	.type	test_Test_singleton,@object # @test_Test_singleton
	.local	test_Test_singleton
	.comm	test_Test_singleton,8,8

	.section	".note.GNU-stack","",@progbits
