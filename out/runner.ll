
@mime.text = internal global [26 x i8] c"Content-Type: text/plain\0A\00"

declare i32 @puts(i8*)


declare void @test_Test_main()
declare void @test_Test__clinit_()

define i32 @main() {
    call i32 @puts( i8* getelementptr ([26 x i8]* @mime.text, i64 0, i64 0) )
    call void @test_Test__clinit_()
    call void @test_Test_main()
    ret i32 0
}

