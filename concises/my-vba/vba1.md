# **VBA Concise**

<a id="top"></a>

Contents

- [**VBA Concise**](#vba-concise)
  - [Row](#row)
  - [Column](#column)
    - [**Columns.AutoFit 展开列(使列宽自适应)**](#columnsautofit-展开列使列宽自适应)
  - [Worksheet](#worksheet)
  - [Workbook](#workbook)
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

## Worksheet

## Workbook

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
