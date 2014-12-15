; ModuleID = './test.Test.ll'

%test_Test = type { i32, i32, i32, i32, i64, float, double, i64, %test_Test* }

@test_Test_sl.b = internal unnamed_addr global i1 false
@test_Test_singleton = internal unnamed_addr global %test_Test* null

declare noalias i8* @malloc(i32) nounwind

declare void @linux_glibc_put_I(i32)

declare i64 @java_lang_System_currentTimeMillis()

declare void @java_lang_Object__init_(%test_Test*)

declare void @linux_glibc_put_J(i64)

define void @test_Test__init_(%test_Test* %s0) {
  call void @java_lang_Object__init_(%test_Test* %s0)
  %__tmp0 = getelementptr %test_Test* %s0, i32 0, i32 0
  store i32 1, i32* %__tmp0
  %__tmp1 = getelementptr %test_Test* %s0, i32 0, i32 1
  store i32 127, i32* %__tmp1
  %__tmp2 = getelementptr %test_Test* %s0, i32 0, i32 2
  store i32 100, i32* %__tmp2
  %__tmp3 = getelementptr %test_Test* %s0, i32 0, i32 3
  store i32 142, i32* %__tmp3
  %__tmp4 = getelementptr %test_Test* %s0, i32 0, i32 4
  store i64 42, i64* %__tmp4
  %__tmp5 = getelementptr %test_Test* %s0, i32 0, i32 5
  store float 0x42F6A8F600000000, float* %__tmp5
  %__tmp6 = getelementptr %test_Test* %s0, i32 0, i32 6
  store double 5.300000e-01, double* %__tmp6
  %stack16 = load i32* %__tmp3
  call void @linux_glibc_put_I(i32 %stack16)
  ret void
}

define void @test_Test_main() {
  %stack0 = call i64 @java_lang_System_currentTimeMillis()
  %__tmp0 = call i8* @malloc(i32 ptrtoint (%test_Test* getelementptr (%test_Test* null, i32 1) to i32))
  %stack1 = bitcast i8* %__tmp0 to %test_Test*
  call void @java_lang_Object__init_(%test_Test* %stack1)
  %__tmp0.i = getelementptr %test_Test* %stack1, i32 0, i32 0
  store i32 1, i32* %__tmp0.i
  %__tmp1.i = getelementptr %test_Test* %stack1, i32 0, i32 1
  store i32 127, i32* %__tmp1.i
  %__tmp2.i = getelementptr %test_Test* %stack1, i32 0, i32 2
  store i32 100, i32* %__tmp2.i
  %__tmp3.i = getelementptr %test_Test* %stack1, i32 0, i32 3
  store i32 142, i32* %__tmp3.i
  %__tmp4.i = getelementptr %test_Test* %stack1, i32 0, i32 4
  store i64 42, i64* %__tmp4.i
  %__tmp5.i = getelementptr %test_Test* %stack1, i32 0, i32 5
  store float 0x42F6A8F600000000, float* %__tmp5.i
  %__tmp6.i = getelementptr %test_Test* %stack1, i32 0, i32 6
  store double 5.300000e-01, double* %__tmp6.i
  %stack16.i = load i32* %__tmp3.i
  call void @linux_glibc_put_I(i32 %stack16.i)
  store i32 9, i32* %__tmp3.i
  call void @linux_glibc_put_I(i32 9)
  %stack8 = call i64 @java_lang_System_currentTimeMillis()
  %stack10 = sub i64 %stack8, %stack0
  call void @linux_glibc_put_J(i64 %stack10)
  %stack11.b = load i1* @test_Test_sl.b
  %stack11 = select i1 %stack11.b, i64 1111, i64 0
  call void @linux_glibc_put_J(i64 %stack11)
  %stack12 = load %test_Test** @test_Test_singleton
  %__tmp5 = getelementptr %test_Test* %stack12, i32 0, i32 4
  %stack13 = load i64* %__tmp5
  call void @linux_glibc_put_J(i64 %stack13)
  ret void
}

define void @test_Test__clinit_() {
  store i1 true, i1* @test_Test_sl.b
  %__tmp1 = tail call i8* @malloc(i32 ptrtoint (%test_Test* getelementptr (%test_Test* null, i32 1) to i32))
  %stack1 = bitcast i8* %__tmp1 to %test_Test*
  tail call void @java_lang_Object__init_(%test_Test* %stack1)
  %__tmp0.i = getelementptr %test_Test* %stack1, i32 0, i32 0
  store i32 1, i32* %__tmp0.i
  %__tmp1.i = getelementptr %test_Test* %stack1, i32 0, i32 1
  store i32 127, i32* %__tmp1.i
  %__tmp2.i = getelementptr %test_Test* %stack1, i32 0, i32 2
  store i32 100, i32* %__tmp2.i
  %__tmp3.i = getelementptr %test_Test* %stack1, i32 0, i32 3
  store i32 142, i32* %__tmp3.i
  %__tmp4.i = getelementptr %test_Test* %stack1, i32 0, i32 4
  store i64 42, i64* %__tmp4.i
  %__tmp5.i = getelementptr %test_Test* %stack1, i32 0, i32 5
  store float 0x42F6A8F600000000, float* %__tmp5.i
  %__tmp6.i = getelementptr %test_Test* %stack1, i32 0, i32 6
  store double 5.300000e-01, double* %__tmp6.i
  %stack16.i = load i32* %__tmp3.i
  tail call void @linux_glibc_put_I(i32 %stack16.i)
  store %test_Test* %stack1, %test_Test** @test_Test_singleton
  ret void
}
