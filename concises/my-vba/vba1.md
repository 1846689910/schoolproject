# **VBA Concise**

<a id="top"></a>

Contents

- [**VBA Concise**](#vba-concise)
  - [Row](#row)
  - [Column](#column)
    - [**Columns.AutoFit 展开列(使列宽自适应)**](#columnsautofit-展开列使列宽自适应)
  - [Range](#range)
    - [**range remove duplicates 去重**](#range-remove-duplicates-去重)
  - [Worksheet](#worksheet)
    - [**hasWorksheet 判断该 workbook 有无该 worksheets(可选择创建)**](#hasworksheet-判断该-workbook-有无该-worksheets可选择创建)
    - [**在worksheet的row行查找target返回列号 getColsList / getFirstCol**](#在worksheet的row行查找target返回列号-getcolslist--getfirstcol)
    - [**在worksheet的col列查找target返回行号 getRowList / getFirstRow**](#在worksheet的col列查找target返回行号-getrowlist--getfirstrow)
  - [Workbook](#workbook)
    - [mkDirs: 创建路径，即使中间路径不存在](#mkdirs-创建路径即使中间路径不存在)
    - [**Open Workbook(or create then open)**](#open-workbookor-create-then-open)
    - [**遍历 directory 下的所有直属workbook**](#遍历-directory-下的所有直属workbook)
    - [**遍历 directory 下的所有 subDirectory and file**](#遍历-directory-下的所有-subdirectory-and-file)
    - [**获取directory下所有的file paths as array**](#获取directory下所有的file-paths-as-array)
    - [**保存 workbook, 并给出名字**](#保存-workbook-并给出名字)
    - [**退出 workbook (不)保存**](#退出-workbook-不保存)
  - [Utils](#utils)
    - [**Main()函数和应用提速**](#main函数和应用提速)
    - [**OCR Find**](#ocr-find)
  - [Methods](#methods)
    - [**instr**](#instr)
    - [**Round**](#round)
    - [**Dir**](#dir)
    - [**Replace**](#replace)
    - [**Split**](#split)
    - [**Join**](#join)
    - [**Mid**](#mid)
    - [**Left & Right**](#left--right)
    - [**isNumeric & isDate & isArray**](#isnumeric--isdate--isarray)
    - [**Trim, LTrim & RTrim**](#trim-ltrim--rtrim)
    - [**类型转换(字符串转数字,遇到空字符串转 0)**](#类型转换字符串转数字遇到空字符串转-0)
    - [**isMissing**](#ismissing)
    - [**WorksheetFunction.Max**](#worksheetfunctionmax)

## Row

## Column

### **Columns.AutoFit 展开列(使列宽自适应)**

```vb
wb.Sheets(1).Columns("A:D").AutoFit  ‘ 将A和D列之间的所有列都展开自适应
wb.Sheets(1).Columns.AutoFit  ‘ 将所有用到的列展开自适应
```

[back to top](#top)

## Range

### **range remove duplicates 去重**

对前两列去重

```vb
ActiveSheet.Range("A1:C100").RemoveDuplicates Columns:=Array(1,2), Header:=xlYes
```

[back to top](#top)

## Worksheet

### **hasWorksheet 判断该 workbook 有无该 worksheets(可选择创建)**

`toBuild`: default `False` not to create if non-exists

```vb
Function hasWorksheet(ByRef wb As Workbook, ByVal name As String, Optional ByVal toBuild As Boolean = False) As Boolean
    Dim found As Boolean
    found = False
    Dim i As Integer
    For i = 1 To wb.Worksheets.count
        If wb.Worksheets(i).name = name Then
            found = True
        End If
    Next i
    If (Not found) And toBuild Then
        wb.Sheets.Add(After:=wb.Sheets(wb.Sheets.count)).name = name
    End If
    hasWorksheet = found
End Function
```

[back to top](#top)

### **在worksheet的row行查找target返回列号 getColsList / getFirstCol**

在 ws 的 row 行找 target 字符串,可以完全匹配或者模糊包含，将所有的列号返回到一个 ArrayList 中,可以用 list.count 获取 size 以及 list.item(idx)来获取某个元素

```vb
Function getColsList(ByRef ws As Worksheet, ByVal row As Integer, ByVal target As String, Optional ByVal mustEqual As Boolean = True) As Object
    Dim list As Object
    Dim i As Integer, iLastCol As Integer
    Set list = CreateObject("System.Collections.ArrayList")
    iLastCol = ws.Cells(row, ws.Columns.Count).End(xlToLeft).Column
    For i = 1 To iLastCol
        If mustEqual Then
            If ws.Cells(row, i).Value = target Then list.Add i
        Else
            If InStr(ws.Cells(row, i).Value, target) > 0 Then list.Add i
        End If
    Next i
    Set getColsList = list
End Function
```

```vb
'找到返回列号，找不到返回-1, 默认从第一列开始找，可以设置开始列
Function getFirstCol(ByRef ws As Worksheet, ByVal row As Integer, ByVal target As String, Optional ByVal start As Integer = 1, Optional ByVal mustEqual As Boolean = True) As Integer
    Dim i As Integer, iLastCol As Integer, col As Integer
    iLastCol = ws.Cells(row, ws.Columns.Count).End(xlToLeft).Column
    col = -1
    For i = start To iLastCol
        If mustEqual Then
            If ws.Cells(row, i).Value = target Then
                col = i
                Exit For
            End If
        Else
            If InStr(ws.Cells(row, i).Value, target) > 0 Then
                col = i
                Exit For
            End If
        End If
    Next i
    getFirstCol = col
End Function
```

[back to top](#top)

### **在worksheet的col列查找target返回行号 getRowList / getFirstRow**

```vb
Function getRowsList(ByRef ws As Worksheet, ByVal col As Integer, ByVal target As String, Optional ByVal mustEqual As Boolean = True) As Object
    Dim list As Object
    Dim i As Integer, iLastRow As Integer
    Set list = CreateObject("System.Collections.ArrayList")
    iLastRow = ws.Cells(ws.Rows.Count, col).End(xlUp).row
    For i = 1 To iLastRow
        If mustEqual Then
            If ws.Cells(i, col).Value = target Then list.Add i
        Else
            If InStr(ws.Cells(i, col).Value, target) > 0 Then list.Add i
        End If
    Next i
    Set getRowsList = list
End Function
```

```vb
'找到返回行号，找不到返回-1, 默认从第一行开始找，可以设置开始行
Function getFirstRow(ByRef ws As Worksheet, ByVal col As Integer, ByVal target As String, Optional ByVal start As Integer = 1, Optional ByVal mustEqual As Boolean = True) As Integer
    Dim i As Integer, iLastRow As Integer, row As Integer
    iLastRow = ws.Cells(ws.Rows.Count, col).End(xlUp).row
    row = -1
    For i = start To iLastRow
        If mustEqual Then
            If ws.Cells(i, col).Value = target Then
                row = i
                Exit For
            End If
        Else
            If InStr(ws.Cells(i, col).Value, target) > 0 Then
                row = i
                Exit For
            End If
        End If
    Next i
    getFirstRow = row
End Function
```

[back to top](#top)

## Workbook

### mkDirs: 创建路径，即使中间路径不存在

```vb
Sub mkDirs(ByVal path As String)
    Dim arr() As String
    Dim midPath As String
    Dim i As Integer, j As Integer
    arr = Split(path, "\")
    For i = LBound(arr) To UBound(arr)
        midPath = ""
        For j = LBound(arr) To i
            midPath = midPath & arr(j) & "\"
        Next j
        If Dir(midPath, vbDirectory) = "" Then MkDir midPath
    Next i
End Sub
```














### **Open Workbook(or create then open)**

if path
- exists, then open
- not exists, create workbook and all intermediate directories, then open

```vb
' directory \ filename(including extension)
Function openWb(ByVal directory As String, ByVal filename As String, Optional ByVal TEMPLATE_PATH As String) As Workbook
    ' open a workbook, if not existing then create and open, if existing then open
    ' wb.close savechanges:=TRUE
    Dim wbNewBook As Workbook
    Dim path As String
    Dim directoryExists As Boolean, fileExists As Boolean
    path = directory & "\" & filename
    If Len(Dir(path)) > 0 Then
        Set wbNewBook = Workbooks.Open(filename:=path)
    Else
        If Len(Dir(directory, vbDirectory)) = 0 Then mkDirs directory
        If IsMissing(TEMPLATE_PATH) Then
            Set wbNewBook = Workbooks.Add(xlWBATWorksheet)
        Else
            Set wbNewBook = Workbooks.Add(TEMPLATE_PATH)
        End If
        With wbNewBook
            .Title = filename
            .Subject = filename
            .SaveAs filename:=path
        End With
    End If
    Set openWb = wbNewBook
End Function
Sub mkDirs(ByVal path As String)
    Dim arr() As String
    Dim midPath As String
    Dim i As Integer, j As Integer
    arr = Split(path, "\")
    For i = LBound(arr) To UBound(arr)
        midPath = ""
        For j = LBound(arr) To i
            midPath = midPath & arr(j) & "\"
        Next j
        If Dir(midPath, vbDirectory) = "" Then MkDir midPath
    Next i
End Sub
```

[back to top](#top)

### **遍历 directory 下的所有直属workbook**

```vb
Sub LoopThroughFiles(directory As String)
    Dim fileName As String
    Dim fileDir As String
    Dim wb As Workbook
    Dim ws As Worksheet
    fileName = dir(directory & "\*.xlsx") ' loop through all the .xlsx files
    Do While Len(fileName) > 0
        Set wb = Workbooks.Open(fileName:=directory & "\" & fileName)
        Set ws = wb.Sheets(1)
        Debug.Print ws.Name
        wb.Close savechanges:=False
        fileName = dir
    Loop
End Sub
```

[back to top](#top)

### **遍历 directory 下的所有 subDirectory and file**

```vb
Sub loopDir(sDir As String)
    Dim FileSystem As Object
    Set FileSystem = CreateObject("Scripting.FileSystemObject")
    DoFolder FileSystem.GetFolder(sDir)
End Sub
Private Sub DoFolder(Folder)
    Dim SubFolder
    For Each SubFolder In Folder.SubFolders
        DoFolder SubFolder
    Next
    Dim File
    For Each File In Folder.Files
        ' Operate on each file
        Debug.Print File.Name ' also File.path, Folder.Name, Folder.Path
    Next
End Sub
```

[back to top](#top)

### **获取directory下所有的file paths as array**

```vb
Function getAllFilepaths(sDir As String) As Variant
    Dim FileSystem As Object
    Dim list As Object
    Set FileSystem = CreateObject("Scripting.FileSystemObject")
    Set list = CreateObject("System.Collections.ArrayList")
    DoFolder FileSystem.GetFolder(sDir), list
    getAllFilepaths = list.toArray
End Function
Private Sub DoFolder(Folder, list As Object)
    Dim SubFolder
    For Each SubFolder In Folder.SubFolders
        DoFolder SubFolder, list
    Next
    Dim File
    For Each File In Folder.Files
        list.Add File.Path
    Next
End Sub
```

[back to top](#top)

### **保存 workbook, 并给出名字**

```vb
wb.SaveAs 路径&文件名
wb.SaveAs strPath & "\TB_Upload " & Replace(Date, "/", "_")

'也可以直接保存成.csv文件
wb.SaveAs 路径 & 文件名 & ”.csv”
```

[back to top](#top)

### **退出 workbook (不)保存**

```vb
Workbooks("BOOK1.XLS").Close SaveChanges:=False
```

[back to top](#top)

## Utils

### **Main()函数和应用提速**

1. Option Explicit: ensure all the variable are defined and used later
2. Use explicit type: avoid using many Variant type, using explicit type, like Integer, String
3. Speed up vba code by add following code lines:

```vb
'    Application.Calculation = xlCalculationManual  ' Take Caution本条谨慎
Option Explicit
Sub main()
Application.ScreenUpdating = False
Application.DisplayStatusBar = False
Application.EnableEvents = False
Application.DisplayAlerts = False
Application.AskToUpdateLinks = False

	' Write your VBA code here ......

'get LastRow Ctrl + Shift + End
'  LastRow = sht.Cells(sht.Rows.Count, "A").End(xlUp).Row
'  LastRow = sht.UsedRange.Rows(sht.UsedRange.Rows.Count).Row
'get Last Column Ctrl + Shift + End
'  LastColumn = sht.Cells(7, sht.Columns.Count).End(xlToLeft).Column
'  LastColumn = sht.UsedRange.Columns(sht.UsedRange.Columns.Count).Column
'WORKSHEET.columns.AutoFit
Application.ScreenUpdating = True
Application.DisplayStatusBar = True
Application.EnableEvents = True
Application.DisplayAlerts = True
Application.AskToUpdateLinks = True
End Sub
```

[back to top](#top)

### **OCR Find**

```vb
' ocrFind:
' 在ws的col列，从fr行到tr行查找target内容是否存在，如存在返回行号，否则返回-1
' 如果在查找过程中在breakCol列发现了breakTarget存在则停止查找返回-1
' in ws worksheet, column col, from fr row to tr row searching for target. if found, return the row number
' if in column breakCol found breakTarget, then stop search early, return -1
Function ocrFind(ByRef ws As Worksheet, ByVal col As String, ByVal fr As Integer, ByVal tr As Integer, ByVal target As String, ByVal breakCol As String, ByVal breakTarget As String) As Integer
    Dim i As Integer
    Dim row As Integer
    row = -1
    For i = fr To tr
        If InStr(ws.Range(col & i).Value, target) <> 0 Then
            row = i
            Exit For
        End If
        If InStr(ws.Range(breakCol & i).Value, breakTarget) <> 0 Then
            row = -1
            Exit For
        End If
    Next i
    ocrFind = row
End Function
```

[back to top](#top)

## Methods

### **instr**

`instr([intStart], strString, strTarget)`, 从 intStart 开始搜索(可省，默认为 1，从首字符开始 i), 从`strString`中找`strTarget`。如果找到返回位置，找不到返回 0
search `strTarget` in `strString` from `intStart` position. if found, return intPosition where the strTarget first show up. if not found, return 0. intStart is default 1
### **Round**

`Round(dict(key), 2)` 将一个数保留小数点后 2 位

### **Dir**

`Dir(“C:\ aaa.docx”)`: 如果路径存在返回字符串 aaa.docx, 如果不存在返回空字符串””. 你可以使用\*匹配 0 个或多个未知字符，或者使用?匹配一个未知字符
`Dir(“C:\aaa”,vbDirectory)`如果是检测路径而不是文件，需要加`vbDirecotry`

### **Replace**

`Replace(Date, “/”, “-”)`: 将字符串中的(/)替换为-

### **Split**

`Split(str, “,”)`: 将字符串按, 分割开来，成一个数组，每一部分是数组的一个元素

### **Join**

`Join(arr, ",")`: 将 s 数组按,来连接，成一个字符串. 如果不提供 splitor, 默认按照**一个空格**来连接

### **Mid**

`MID(text, start, K )`: 从字符串 text 的 start 位置开始，取 K 个字符出来, 注意首字符索引是 1

### **Left & Right**

`LEFT或RIGHT(text, K )`: 从字符串 text 的左边或右边开始，取 K 个字符出来

### **isNumeric & isDate & isArray**

`IsNumeric`或~~IsEmpty~~或`IsDate(text)`: 检测表达式 text
IsNumeric 是否是数字类型, 比如整数 12, 浮点 1.2, 百分数 12%都对，带了字母就错

~~IsEmpty 是否是空~~ 尽量不用，只是部分适用，请使用 trim(text) = ""

    IsDate 是否是日期格式，常用的日期写法都可以检测到

```vb
dim arr as variant
arr = Array(1, 2, 3)
debug.print isArray(arr)
```

### **Trim, LTrim & RTrim**

`Trim, LTrim, RTrim(text)`: 将字符串 text 的所有, 左边或右边的空格都去除

### **类型转换(字符串转数字,遇到空字符串转 0)**

```vb
    Dim s As String
    Dim d As Double
    s = ""
    If IsNumeric(Trim(s)) Then
        d = CDbl(s)
    ElseIf Trim(s) = "" Then
        d = CDbl("0")
    End If
    Debug.Print d
```

### **isMissing**

检测函数中 optional 的 value 是否有传递, 如果 optional X as boolean = False，那么就不算 missing 了，就会产生 false

[back to top](#top)

### **WorksheetFunction.Max**

求最大值

```vb
WorksheetFunction.Max(arr)
WorksheetFunction.Max(arrayList.toArray())
WorksheetFunction.Max(arr, 1, 2)
```
