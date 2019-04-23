for %%i in (protoes\cmds\*.proto) do protogenbin\protogen -i:%%i -o:csharp\%%~ni.cs
for %%i in (protoes\msgs\*.proto) do protogenbin\protogen -i:%%i -o:csharp\%%~ni.cs
