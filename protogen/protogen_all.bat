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

echo ��������proto�ļ���java�ļ���cs�ļ�...

call protogen_java.bat
call protogen_csharp.bat

copy javafiles\com\alan\proto\*.java ..\server\src\main\java\com\alan\proto\
copy csharp\*.cs ..\client\XClient\Assets\scripts\Net\protoes\

echo .
echo �������������...
echo .

pause