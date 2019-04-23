@echo off

del ..\client\XClient\Assets\scripts\Net\protoes\*.cs
del ..\server\src\com\alan\proto\*.java

cd csharp
del /q *.*
cd..
cd javafiles

rd /s/q com
del /q *.*
cd..

echo 正在生成proto文件的java文件和cs文件...

call protogen_java.bat
call protogen_csharp.bat

copy javafiles\com\alan\proto\*.java ..\server\src\main\java\com\alan\proto\
copy csharp\*.cs ..\client\XClient\Assets\scripts\Net\protoes\

echo .
echo 理论上生成完成...
echo .

pause