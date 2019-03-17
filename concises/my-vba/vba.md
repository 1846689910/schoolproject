# **VBA Concise**

<a id="top"></a>

## **Contents**

[**Main()函数和应用提速**](#1)

[**展开列(使列宽自适应)**](#2)

[**OCR Find**](#3)

[**remove duplicates去重**](#4)

[**Create Folder**](#5)

[**Create directory: create all directory according to the given path**](#6)

[**Open Workbook(优先)**](#7)

[**遍历directory下的所有workbook**](#8)

[**遍历directory下的所有subDirectory and file**](#9)

[**保存workbook, 并给出名字**](#10)

[**退出workbook不保存**](#11)

[**hasWorksheet 判断该workbook有无该worksheets(可选择创建)**](#12)

[**查找列号getColsList / getFirstCol**](#13)

[**查找行号getRowList / getFirstRow**](#14)

[**字母列号转数字 ColLetterToNum**](#15)

[**数字列号转字母 ColNumToLetter**](#16)

[**Find Last Row**](#17)

[**Find Last Column**](#18)

[**插入行/列**](#19)

<a id="1"></a>

## **Main()函数和应用提速**

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


<a id="2"></a>

## **展开列(使列宽自适应)**

```vb
wb.Sheets(1).Columns("A:D").AutoFit  ‘ 将A和D列之间的所有列都展开自适应
wb.Sheets(1).Columns.AutoFit  ‘ 将所有用到的列展开自适应
```
[back to top](#top)


<a id="3"></a>

## **OCR Find**

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

<a id="4"></a>

## **remove duplicates去重**

对前两列去重
```vb
ActiveSheet.Range("A1:C100").RemoveDuplicates Columns:=Array(1,2), Header:=xlYes
```

[back to top](#top)

<a id="5"></a>

## **Create Folder**

If the path has already existed, return the path; if the path does not exist, create the folder and return the path
```vb
Function CreateFolder(strPath As String, strFolderName As String) As String
    ' create a separate folder to save all the region reports
    Dim path As String
    path = strPath & "\" & Replace(Date, "/", "-") & " " & strFolderName
    If Dir(path, vbDirectory) = "" Then
        MkDir path
        CreateFolder = path
    Else
        CreateFolder = path
    End If
End Function
```

[back to top](#top)

<a id="6"></a>

## **Create directory: create all directory according to the given path**

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
[back to top](#top)

<a id="7"></a>

## **Open Workbook(优先)**

如果path 不存在就会创建，如果workbook存在就直接打开，如果不存在，先创建后打开. 如果传入一个template_path的文件路径，会以这个文件为模板创建相同的文件再操作
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


<a id="8"></a>

## **遍历directory下的所有workbook**

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

<a id="9"></a>

## **遍历directory下的所有subDirectory and file**

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

<a id="10"></a>

## **保存workbook, 并给出名字**

```vb
wb.SaveAs 路径&文件名
wb.SaveAs strPath & "\TB_Upload " & Replace(Date, "/", "_")

'也可以直接保存成.csv文件
wb.SaveAs 路径 & 文件名 & ”.csv”
```

[back to top](#top)

<a id="11"></a>

## **退出workbook不保存**

```vb
Workbooks("BOOK1.XLS").Close SaveChanges:=False
```

[back to top](#top)

<a id="12"></a>

## **hasWorksheet 判断该workbook有无该worksheets(可选择创建)**

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

<a id="13"></a>

## **查找列号getColsList / getFirstCol**

在ws的row行找target字符串,可以完全匹配或者模糊包含，将所有的列号返回到一个ArrayList中,可以用list.count获取size以及list.item(idx)来获取某个元素

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


<a id="14"></a>

## **查找行号getRowList / getFirstRow**

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

<a id="15"></a>

## **字母列号转数字 ColLetterToNum**

```vb
Function ColLetterToNum(ByVal sColLetter As String) As Integer
' Convert column letter to numeric
    ColLetterToNum = ActiveWorkbook.Worksheets(1).Columns(sColLetter).column
End Function
```

[back to top](#top)

<a id="16"></a>

## **数字列号转字母 ColNumToLetter**

```vb
Function ColNumToLetter(lColNum As Integer) As String
' Convert numeric to column letter
    ColNumToLetter = Split(Cells(1, lColNum).Address, "$")(1)
End Function
```

[back to top](#top)

<a id="17"></a>

## **Find Last Row**

```vb
'Ctrl + Shift + End
  LastRow = sht.Cells(sht.Rows.Count, "A").End(xlUp).Row

'Using UsedRange
  sht.UsedRange 'Refresh UsedRange
  LastRow = sht.UsedRange.Rows(sht.UsedRange.Rows.Count).Row

'Using Table Range
  LastRow = sht.ListObjects("Table1").Range.Rows.Count

'Using Named Range
  LastRow = sht.Range("MyNamedRange").Rows.Count

'Ctrl + Shift + Down (Range should be first cell in data set)
  LastRow = sht.Range("A1").CurrentRegion.Rows.Count
```

[back to top](#top)

<a id="18"></a>

## **Find Last Column**

```vb
'Ctrl + Shift + End
  LastColumn = sht.Cells(7, sht.Columns.Count).End(xlToLeft).Column

'Using UsedRange
  sht.UsedRange 'Refresh UsedRange
  LastColumn = sht.UsedRange.Columns(sht.UsedRange.Columns.Count).Column

'Using Table Range
  LastColumn = sht.ListObjects("Table1").Range.Columns.Count

'Using Named Range
  LastColumn = sht.Range("MyNamedRange").Columns.Count

'Ctrl + Shift + Right (Range should be first cell in data set)
  LastColumn = sht.Range("A1").CurrentRegion.Columns.Count
```

[back to top](#top)

<a id="19"></a>

## **插入行/列**

```vb
'以下ActiveCell可以是ws.cells(行, 列)形式
'Insert row above active cell
ActiveCell.EntireRow.Insert

'Insert row below active cell
ActiveCell.Offset(1).EntireRow.Insert

'Insert column to the left of the active cell
ActiveCell.EntireColumn.Insert

'Insert column to the right of the active cell
ActiveCell.EntireColumn.Offset(0, 1).Insert
```

[back to top](#top)