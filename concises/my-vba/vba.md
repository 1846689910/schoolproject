# **VBA Concise**

<a id="top"></a>

## **Contents**

[**Main()函数和应用提速**](#1)

[**展开列(使列宽自适应)**](#2)

[**OCR Find**](#3)

[**remove duplicates去重**](#4)

[**Create Folder**](#5)

[**Create directory: create all directory according to the given path**](#6)

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
