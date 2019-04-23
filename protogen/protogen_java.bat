del ..\src\main\java\com\alan\proto\*.java
del javafiles\com\alan\proto\*.*
for %%a in (protoes\cmds\*.proto) do protogenbin\protoc.exe --java_out=javafiles %%a
for %%a in (protoes\msgs\*.proto) do protogenbin\protoc.exe --java_out=javafiles %%a
copy javafiles\com\alan\proto\*.* ..\src\main\java\com\alan\proto\
pause
