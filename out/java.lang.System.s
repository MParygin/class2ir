	.file	"java.lang.System.bc"
	.text
	.globl	java_lang_System_currentTimeMillis
	.align	16, 0x90
	.type	java_lang_System_currentTimeMillis,@function
java_lang_System_currentTimeMillis:     # @java_lang_System_currentTimeMillis
	.cfi_startproc
# BB#0:
	subq	$24, %rsp
.Ltmp1:
	.cfi_def_cfa_offset 32
	leaq	8(%rsp), %rdi
	xorl	%esi, %esi
	callq	gettimeofday
	movabsq	$2361183241434822607, %rax # imm = 0x20C49BA5E353F7CF
	imulq	16(%rsp)
	movq	%rdx, %rax
	shrq	$63, %rax
	sarq	$7, %rdx
	addq	%rax, %rdx
	imulq	$1000, 8(%rsp), %rax    # imm = 0x3E8
	addq	%rdx, %rax
	addq	$24, %rsp
	ret
.Ltmp2:
	.size	java_lang_System_currentTimeMillis, .Ltmp2-java_lang_System_currentTimeMillis
	.cfi_endproc

	.globl	java_lang_System_nanoTime
	.align	16, 0x90
	.type	java_lang_System_nanoTime,@function
java_lang_System_nanoTime:              # @java_lang_System_nanoTime
	.cfi_startproc
# BB#0:
	subq	$24, %rsp
.Ltmp4:
	.cfi_def_cfa_offset 32
	leaq	8(%rsp), %rdi
	xorl	%esi, %esi
	callq	gettimeofday
	imulq	$1000000, 8(%rsp), %rax # imm = 0xF4240
	addq	16(%rsp), %rax
	imulq	$1000, %rax, %rax       # imm = 0x3E8
	addq	$24, %rsp
	ret
.Ltmp5:
	.size	java_lang_System_nanoTime, .Ltmp5-java_lang_System_nanoTime
	.cfi_endproc


	.section	".note.GNU-stack","",@progbits
