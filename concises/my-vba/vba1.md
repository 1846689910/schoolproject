# **VBA Concise**

<a id="top"></a>

Contents

- [**VBA Concise**](#vba-concise)
  - [Row](#row)
    - [**Find Last Row**](#find-last-row)
    - [**插入行**](#插入行)
    - [**moveRow from `fromRow` to `destRow`**](#moverow-from-fromrow-to-destrow)
    - [**swapRow 交换 i 行和 j 行**](#swaprow-交换-i-行和-j-行)
    - [**Delete Rows**](#delete-rows)
    - [**Delete Rows in Specific Condition**](#delete-rows-in-specific-condition)
  - [Column](#column)
    - [**字母列号转数字 ColLetterToNum**](#字母列号转数字-collettertonum)
    - [**数字列号转字母 ColNumToLetter**](#数字列号转字母-colnumtoletter)
    - [**Find Last Column**](#find-last-column)
    - [**Columns.AutoFit 展开列(使列宽自适应)**](#columnsautofit-展开列使列宽自适应)
    - [**插入列**](#插入列)
    - [**Delete Columns**](#delete-columns)
  - [Range](#range)
    - [**range remove duplicates 去重**](#range-remove-duplicates-去重)
    - [**循环查找，找到 worksheet 里第一个内容为...或者内容不空的 cell**](#循环查找找到-worksheet-里第一个内容为或者内容不空的-cell)
    - [**Clear Range**](#clear-range)
  - [Worksheet](#worksheet)
    - [**hasWorksheet 判断该 workbook 有无该 worksheets(可选择创建)**](#hasworksheet-判断该-workbook-有无该-worksheets可选择创建)
    - [**在worksheet的row行查找target返回列号 getColsList / getFirstCol**](#在worksheet的row行查找target返回列号-getcolslist--getfirstcol)
    - [**在worksheet的col列查找target返回行号 getRowList / getFirstRow**](#在worksheet的col列查找target返回行号-getrowlist--getfirstrow)
    - [**Worksheet copy with range: copy a range from ws to wsTarget, need range name**](#worksheet-copy-with-range-copy-a-range-from-ws-to-wstarget-need-range-name)
    - [**Worksheet copy format**](#worksheet-copy-format)
    - [**Copy Worksheet**](#copy-worksheet)
      - [copy worksheet and paste before or after](#copy-worksheet-and-paste-before-or-after)
      - [copy worksheet and will create new workbook to pastes](#copy-worksheet-and-will-create-new-workbook-to-pastes)
      - [copy worksheet and paste with specification](#copy-worksheet-and-paste-with-specification)
    - [**scroll to top of the worksheet**](#scroll-to-top-of-the-worksheet)
    - [**number of worksheets**](#number-of-worksheets)
  - [Workbook](#workbook)
    - [mkDirs: 创建路径，即使中间路径不存在](#mkdirs-创建路径即使中间路径不存在)
    - [**Open Workbook(or create then open)**](#open-workbookor-create-then-open)
    - [**遍历 directory 下的所有直属workbook**](#遍历-directory-下的所有直属workbook)
    - [**遍历 directory 下的所有 subDirectory and file**](#遍历-directory-下的所有-subdirectory-and-file)
    - [**获取directory下所有的file paths as array**](#获取directory下所有的file-paths-as-array)
    - [**保存 workbook, 并给出名字**](#保存-workbook-并给出名字)
    - [**退出 workbook (不)保存**](#退出-workbook-不保存)
    - [**Copy a file and rename**](#copy-a-file-and-rename)
  - [Utils](#utils)
    - [**Main()函数和应用提速**](#main函数和应用提速)
    - [**OCR Find**](#ocr-find)
    - [**下拉列表框**](#下拉列表框)
    - [**format Number: format data of worksheet ws**](#format-number-format-data-of-worksheet-ws)
    - [**Get Next Non-Empty Row in Column intCol, start search from intStartRow**](#get-next-non-empty-row-in-column-intcol-start-search-from-intstartrow)
    - [**Open select file dialog (only see the excel type file) and return the complete path of the file**](#open-select-file-dialog-only-see-the-excel-type-file-and-return-the-complete-path-of-the-file)
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

### **Find Last Row**

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

### **插入行**

```vb
'以下ActiveCell可以是ws.cells(行, 列)形式
'Insert row above active cell
ActiveCell.EntireRow.Insert

'Insert row below active cell
ActiveCell.Offset(1).EntireRow.Insert
```

[back to top](#top)



### **moveRow from `fromRow` to `destRow`**

let the `fromRow` moved to the position of the current `destRow`. The original `destRow` will be pushed to one row down and become `destRow + 1`

```vb
Sub moveRow(ByRef ws As Worksheet, ByVal fromRow As Long, ByVal destRow As Long)
    Dim f As Long
    If fromRow <= destRow Then
        f = fromRow
    Else
        f = fromRow + 1
    End If
    ws.Range("A" & destRow).EntireRow.Insert
    ws.Range("A" & f).EntireRow.Cut ws.Range("A" & destRow)
    ws.Range("A" & f).EntireRow.Delete
End Sub
```

[back to top](#top)

### **swapRow 交换 i 行和 j 行**

```vb
Sub swapRow(ByRef ws As Worksheet, ByVal i As Long, ByVal j As Long)
    Dim small As Long
    Dim large As Long
    If i <= j Then
        small = i
        large = j
    Else
        small = j
        large = i
    End If
    ws.Range("A" & small).EntireRow.Insert
    ws.Range("A" & (large + 1)).EntireRow.Cut ws.Range("A" & small)
    ws.Range("A" & (small + 1)).EntireRow.Cut ws.Range("A" & (large + 1))
    ws.Range("A" & (small + 1)).EntireRow.Delete
End Sub
```

[back to top](#top)

### **Delete Rows**

```vb
ws.Rows("1 : 10").EntireRow.Delete
```
[back to top](#top)

### **Delete Rows in Specific Condition**

```vb
ws.Select
ws.UsedRange.Select
For i = Selection.Rows.Count To 1 Step -1
    If IsEmpty(ws.Range("A" & i).Value) Then
        Selection.Rows(i).EntireRow.Delete
    End If
Next i
```

[back to top](#top)

## Column

### **字母列号转数字 ColLetterToNum**

```vb
Function ColLetterToNum(ByVal sColLetter As String) As Integer
' Convert column letter to numeric
    ColLetterToNum = ActiveWorkbook.Worksheets(1).Columns(sColLetter).column
End Function
```

[back to top](#top)

### **数字列号转字母 ColNumToLetter**

```vb
Function ColNumToLetter(lColNum As Integer) As String
' Convert numeric to column letter
    ColNumToLetter = Split(Cells(1, lColNum).Address, "$")(1)
End Function
```

[back to top](#top)

### **Find Last Column**

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

### **Columns.AutoFit 展开列(使列宽自适应)**

```vb
wb.Sheets(1).Columns("A:D").AutoFit  ‘ 将A和D列之间的所有列都展开自适应
wb.Sheets(1).Columns.AutoFit  ‘ 将所有用到的列展开自适应
```

[back to top](#top)

### **插入列**

```vb
'以下ActiveCell可以是ws.cells(行, 列)形式
'Insert column to the left of the active cell
ActiveCell.EntireColumn.Insert

'Insert column to the right of the active cell
ActiveCell.EntireColumn.Offset(0, 1).Insert
```

[back to top](#top)

### **Delete Columns**

```vb
ws.Columns(j).EntireColumn.Delete
```

删除这一列之后，后边的列会自动移过来，所以不能用 for 来遍历删除符合条件的列，可以用 while 循环

```vb
i = 1
iLast = 100
while i <= iLast
      if 条件满足 then
            ws.Columns(i).EntireColumn.Delete
            iLast = 更新iLast为iLast - 1  ' 删掉一列，马上更新总列数，明确循环的边界
            ' 不要在这里前移指针，因为删掉一列后，会有后边的列补到当前指针位置，所以应该接着判断
      else
            i = i + 1  ' 如果不符合那么指针前移
      end if
wend
```

[back to top](#top)

## Range

### **range remove duplicates 去重**

对前两列去重

```vb
ActiveSheet.Range("A1:C100").RemoveDuplicates Columns:=Array(1,2), Header:=xlYes
```

[back to top](#top)

### **循环查找，找到 worksheet 里第一个内容为...或者内容不空的 cell**

sheet1 为要查找的 worksheet，text 为查找内容，blurredMatch 模糊匹配：True 表示内容含有 text 就算找到，False 表示内容完全相等才算找到

```vb
Public Function getPos(sheet1 As Worksheet, text As String, blurredMatch As Boolean) As Variant
    Dim i As Integer
    Dim j As Integer
    Dim found As Boolean
    Dim arr As Variant
    arr = Array(-1, -1)
    For i = 1 To sheet1.UsedRange.Rows(sheet1.UsedRange.Rows.Count).Row
        For j = 1 To sheet1.UsedRange.Columns(sheet1.UsedRange.Columns.Count).Column
            If (blurredMatch And InStr(sheet1.Cells(i, j).Value, text) <> 0) Or ((Not blurredMatch) And sheet1.Cells(i, j).Value = text) Then
                found = True
                arr = Array(i, j)
                Exit For
            End If
        Next j
        If found Then Exit For
    Next i
    getPos = arr
End Function
```

[back to top](#top)

### **Clear Range**

```vb
ws.cells.clearContents
ws.cells.clearFormat
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

### **Worksheet copy with range: copy a range from ws to wsTarget, need range name**

```vb
Sub copy(ws As Worksheet, wsTarget As Worksheet, strOriginCellName As String, strTargetCellName As String, intColorIdx As Integer)
    ' the helper function for copy and paste value and format
    ws.Range(strOriginCellName).Copy
    With ws.Range(strOriginCellName)
        .Interior.ColorIndex = intColorIdx
        .Copy
    End With
    With wsTarget.Range(strTargetCellName)
        .Interior.ColorIndex = intColorIdx
        .PasteSpecial xlPasteFormats
        .PasteSpecial xlPasteValues
    End With
End Sub
```

or

```vb
wsSrc.Range("A2:A" & intSrcLastRow).Copy Destination:=wsTarget.Range("A" & intBegRow)
```

or copy the entire `wsSrc` to `wsTarget`

```vb
wsSrc.Cells.Copy Destination:=wsTarget.Cells
```

or copy the `wsSrc` and place it after `wsPrev`

```vb
wsSrc.Copy After:=wsPrev
```

[back to top](#top)

### **Worksheet copy format**

```vb
ws.Range("B1").Copy
ws.Range("C1:D1").PasteSpecial (xlPasteFormats)  ‘ 将B1 copy之后，对C1:D1范围应用format
```

[back to top](#top)

### **Copy Worksheet**

#### copy worksheet and paste before or after

```vb
ThisWorkbook.Worksheets("Sheet1").Copy After:=ThisWorkbook.Worksheets("Sheet3")
```
[back to top](#top)

#### copy worksheet and will create new workbook to pastes

```vb
ThisWorkbook.Worksheets("Sheet1").Copy
ActiveWorksheet.name = "Sheet1-copy"
' or multiple worksheets together
ThisWorkbook.WorkSheets(Array("Sheet1", "Sheet2", "Sheet4")).Copy
ActiveWorkbook.Sheets("Sheet1").name = "Sheet1-copy"
With ActiveWorkbook 
     .SaveAs Filename:=ThisWorkbook.path & "\New1.xlsx", FileFormat:=xlOpenXMLWorkbook
     .Close SaveChanges:=False
End With
```
[back to top](#top)

#### copy worksheet and paste with specification

```vb
Sheets("Sheet1").cells.Copy
Sheets("Sheet2").cells.PasteSpecial xlPasteValues
```
or copy to itself
```vb
Sheets("Sheet1").cells.Copy
Sheets("Sheet1").cells.PasteSpecial xlPasteValues
```

[back to top](#top)


### **scroll to top of the worksheet**

```vb
Application.Goto Reference:=wsCurrent.Range("a1"), Scroll:=True
```

[back to top](#top)

### **number of worksheets**

```vb
ActiveWorkbook.Worksheets.Count
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


### **Copy a file and rename**

```vb
FileCopy(ToolFilePath,DestFilePath)
FileCopy "C:\local files\tester.xlsx", "C:\local files\__TMP\tester_copy.xlsx"
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

### **下拉列表框**

Set the drop down list: strCellName represents the position you need to put a drop down list. formula1 := “=sht!A1:A6” represents that the content in drop down list are in A1 : A6

```vb
Sub setList(strCellName As String, val As String)
    ' set the drop down list for the blanks in wsUserInput
    With Range(strCellName).Validation
        .Delete
        .Add Type:=xlValidateList, AlertStyle:=xlValidAlertStop, _
            Operator:=xlEqual, Formula1:=val
        .IgnoreBlank = True
        .InCellDropdown = True
        .InputTitle = ""
        .ErrorTitle = ""
        .InputMessage = ""
        .ErrorMessage = ""
        .ShowInput = True
        .ShowError = True
    End With
End Sub
```

[back to top](#top)

### **format Number: format data of worksheet ws**

```vb
Private Sub formatNumber(wsTar As Worksheet, i As Integer)
    ' format digits of some number and date in wsTarget(tax pipeline report)
    wsTar.Range("AU" & i).NumberFormat = "00000"  ‘ 精确数字到5位
    wsTar.Range("AV" & i).NumberFormat = "00000"
    wsTar.Range("AW" & i).NumberFormat = "0000000"
    wsTar.Range("BO" & i).NumberFormat = "dd-mmm-yyyy"  ‘ 格式化日期
    wsTar.Range("AW" & i).NumberFormat = "$#,###"
    ws.Range("B" & i).NumberFormat = "_(* #,##0_);_(* (#,##0);_(* "" - ""_);_(@_)"转换成字符形式
End Sub
```

[back to top](#top)

### **Get Next Non-Empty Row in Column intCol, start search from intStartRow**

```vb
Private Function getNENextRow(ws As Worksheet, intCol As Integer, intStartRow As Integer, intFileLastRow As Integer) As Integer
    ' get the next non empty row number. starting from the current cell and find the next cell whose value is nonempty
    Dim idx As Integer
    idx = intStartRow
    While idx < intFileLastRow And IsEmpty(ws.Cells(idx, intCol).Value) = True
        idx = idx + 1
    Wend
    getNENextRow = idx
End Function
```

[back to top](#top)

### **Open select file dialog (only see the excel type file) and return the complete path of the file**

```vb
Public Function SelectFile() As String
    Dim vPath As Variant
    vPath = Application.GetOpenFilename(FileFilter:="Excel Workbooks (*.xlsx;*.xls;*.xlsm;*.xlsb), *.xlsx;*.xls;*.xlsm;*x.lsb", TITLE:="Please Select EYU Path report")
    If vPath = False Then
        SelectFile = ""
    Else
        SelectFile = vPath
    End If
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
