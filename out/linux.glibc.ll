
@format_i32 = internal global [4 x i8] c"%d\0A\00"
@format_i64 = internal global [5 x i8] c"%ld\0A\00"

declare i32 @puts(i8*)
declare i32 @printf(i8*, ...)

define void @linux_glibc_put_I(i32 %value) {
    %ptr0 = getelementptr [4 x i8]* @format_i32, i32 0, i32 0
    call i32 (i8*,...)* @printf(i8* %ptr0, i32 %value)
    ret void
}

define void @linux_glibc_put_J(i64 %value) {
    %ptr0 = getelementptr [5 x i8]* @format_i64, i32 0, i32 0
    call i32 (i8*,...)* @printf(i8* %ptr0, i64 %value)
    ret void
}

