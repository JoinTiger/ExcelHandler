package com.example.demo.util;


import com.example.demo.annotation.ExcelAttribute;
import com.example.demo.annotation.ExcelElement;
import com.example.demo.bean.ProduceInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelUtils {

    private static boolean flag = true;
    private static int rowNum = 0;
    private static XSSFRow row;

        // @描述：是否是2003的excel，返回true是2003
        public static boolean isExcel2003(String filePath) {
            return filePath.matches("^.+\\.(?i)(xls)$");
        }

        // @描述：是否是2007的excel，返回true是2007
        public static boolean isExcel2007(String filePath) {
            return filePath.matches("^.+\\.(?i)(xlsx)$");
        }

        /**
         * 验证EXCEL文件
         *
         * @param filePath
         * @return
         */
        public static boolean validateExcel(String filePath) {
            if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
                return false;
            }
            return true;
        }



    public static <T> boolean exportExcel(List<T> lists[], Class clazz,String sheetNames[], XSSFWorkbook wb, OutputStream output) {
        if (lists.length != sheetNames.length) {
            System.out.println("数组长度不一致");
            return false;
        }

        // 创建excel工作簿
        //XSSFWorkbook wb ;
        // 创建第一个sheet（页），命名为 new sheet
        for (int ii = 0; ii < lists.length; ii++) {
            List<T> list = lists[ii];
            // 产生工作表对象
            XSSFSheet sheet = wb.createSheet();
            wb.setSheetName(ii, sheetNames[ii]);
            // 创建表头
            createHeader(wb, sheet, clazz);
            for (T t : list) {
                createRow(t,sheet);
            }
        }
        try {
            output.flush();
            wb.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }


    }


    public static <T> boolean exportExcel(List<T> list, Class clazz,String sheetName, XSSFWorkbook wb,
                               OutputStream output) {
        //此处 对类型进行转换
        List<T> ilist = new ArrayList<>();
        for (T t : list) {
            ilist.add(t);
        }
        List<T>[] lists = new ArrayList[1];
        lists[0] = ilist;

        String[] sheetNames = new String[1];
        sheetNames[0] = sheetName;

        return exportExcel(lists, clazz, sheetNames, wb, output);
    }


    /**
     *
     * 设置表头
     */
    private static void createHeader(XSSFWorkbook wb, XSSFSheet sheet, Class clazz){
        // 产生一行
        XSSFRow row = sheet.createRow(0);
        XSSFCellStyle style = wb.createCellStyle();
        //得到所有的字段
        List<Field> fields = getAllField(clazz, null);
        XSSFCell cell;// 产生单元格
        for (Field field : fields) {
            ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
            int col = getExcelCol(attr.column());// 获得列号
            cell = row.createCell(col);// 创建列
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置列中写入内容为String类型
            cell.setCellValue(attr.name());// 写入列名
            cell.setCellStyle(style);
        }
    }

    private static List<Field> getAllField(Class<?> clazz, List<Field> listField) {
        if (listField == null) {
            listField = new ArrayList<>();
        }
        // 获取所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Type fieldType = field.getType();

            if (field.isAnnotationPresent(ExcelAttribute.class)) {
                listField.add(field);
                // 类名,属性名
            } else if (field.isAnnotationPresent(ExcelElement.class)) {
                /**
                 * TODO 类型判断
                 */
                switch (fieldType.getTypeName()) {
                    case "java.util.Set":
                    case "java.util.List":
                        Type genericFieldType = field.getGenericType();
                        getAllField(getClass(genericFieldType, 0), listField);
                        break;
                    case "java.util.Map":
                        listField.add(field);
                        break;
                    default:
                        getAllField(field.getClass(),null);
                        break;
                }
            }
        }

        return listField;
    }



    public static void createRow(Object t, XSSFSheet sheet) {
        // 遍历集合数据，产生数据行

        XSSFCell cell;
        if(flag){
            rowNum++;
            row = sheet.createRow(rowNum);
        }

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if(!field.isAccessible()){
                    // 设置私有属性为可访问
                    field.setAccessible(true);
                }
                if(field.isAnnotationPresent(ExcelAttribute.class)&&!field.isAnnotationPresent(ExcelElement.class)){
                    ExcelAttribute ea = field.getAnnotation(ExcelAttribute.class);
                    flag = true;
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (ea.isExport()) {
                            cell = row.createCell(getExcelCol(ea.column()));// 创建cell
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(field.get(t) == null ? ""
                                    : String.valueOf(field.get(t)));// 如果数据存在就填入,不存在填入空格.
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else if(field.isAnnotationPresent(ExcelElement.class)){
                    flag = false;

                    switch (field.getType().getTypeName()) {
                        case "java.util.Set":
                            Set<?> set = (Set<?>)field.get(t);
                            if(set!=null){
                                for (Object object : set) {
                                    createRow(object,sheet);
                                }
                            }
                            break;
                        case "java.util.List":
                            List<?> list = (List<?>)field.get(t);
                            if(list!=null){
                                for (Object object : list) {
                                    createRow(object,sheet);
                                }
                            }
                            break;
                        case "java.util.Map":
                            ExcelAttribute ea = field.getAnnotation(ExcelAttribute.class);
                            Map<?,?> map = (Map<?,?>)field.get(t);
                            if(map!=null){
                                StringBuffer strB = new StringBuffer();
                                for (Map.Entry<?, ?> entry : map.entrySet()) {
                                    strB.append(entry.getKey()+" : "+entry.getValue()+" , ");
                                }
                                if(strB.length()>0){
                                    strB.deleteCharAt(strB.length()-1);
                                    strB.deleteCharAt(strB.length()-1);
                                }

                                try {
                                    // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                                    if (ea.isExport()) {
                                        cell = row.createCell(getExcelCol(ea.column()));// 创建cell
                                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                        cell.setCellValue(strB== null ? ""
                                                : strB.toString());// 如果数据存在就填入,不存在填入空格.
                                    }
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        default:
                            createRow(field.get(t),sheet);
                            break;
                    }
                }
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        flag = true;

    }


    public static List<Object> readExcel2007(InputStream is, Object object) {
        try {
            List<Object> rowList = new ArrayList<Object>();
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            XSSFWorkbook wb = new XSSFWorkbook(is);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            for (int i = sheet.getFirstRowNum() + 1, rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows()-1; rowCount++, i++) {
                row = sheet.getRow(i);
                Object obj = clazz.newInstance();
                for (int j = row.getFirstCellNum(), k = 0; j < row.getFirstCellNum() + fields.length;k++, j++) {
                    String key = fields[k].getName();
                    PropertyDescriptor descriptor = new PropertyDescriptor(key, clazz);

                    Method method = descriptor.getWriteMethod();
                    cell = row.getCell(j);
                    if (cell == null || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                        if (j != row.getLastCellNum()) {
                            //colList.add("");
                            obj = null;
                        }
                        continue;
                    }

                    if (null != cell) {
                        switch (cell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    method.invoke(obj, HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                                    break;
                                } else {
                                    Double d = cell.getNumericCellValue();
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    method.invoke(obj, Long.parseLong(df.format(d)));
                                }
                                break;

                            case HSSFCell.CELL_TYPE_STRING:
                                method.invoke(obj, cell.getStringCellValue());
                                break;

                            case HSSFCell.CELL_TYPE_BOOLEAN:
                                method.invoke(obj, cell.getBooleanCellValue());
                                break;

                            case HSSFCell.CELL_TYPE_FORMULA:
                                method.invoke(obj, cell.getCellFormula());
                                break;

                            case HSSFCell.CELL_TYPE_BLANK:
                                method.invoke(obj,"");
                                break;

                            case HSSFCell.CELL_TYPE_ERROR:
                                method.invoke(obj, "非法字符");
                                break;

                            default:
                                method.invoke(obj, "未知类型");
                                break;
                        }
                    }
                }
                if(obj != null) {
                    rowList.add(obj);
                }
            }
            if (is != null) {
                is.close();
            }
            return rowList;
        } catch (Exception e) {
            System.out.println("exception");
            return null;
        }
    }


    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    private static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return (Class) getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class) type;
        }
    }

    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            return (Class) getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }

    public static <T> boolean generateSheet(List<T> data, XSSFWorkbook wb,
                                            String sheetname, String saveFilePath) throws Exception {
        XSSFSheet sheet = wb.getSheet(sheetname); // 获取到工作表
        XSSFRow row = null; //
        FileOutputStream out = new FileOutputStream(saveFilePath);
        // 遍历集合数据，产生数据行
        Iterator<T> it = data.iterator();
        int index = 1;
        boolean flag = true;
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-MM-dd"));//设置时间格式
        try {
            while (it.hasNext()) {
                int k = sheet.getLastRowNum();
                row = sheet.createRow(k++);//若不是在已有Excel表格后面追加数据 则使用该条语句
                // 创建单元格，并设置值
                T t = (T) it.next();
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.toString().contains("static")) {
                        continue;
                    }
                    XSSFCell cell = row.createCell((short) i);
                    String fieldName = field.getName();
                    String getMethodName = "get"
                            + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[] {});
                    Object value = getMethod.invoke(t, new Object[] {});
                    // 判断值的类型后进行强制类型转换
                    //String textValue = null;

                    if (value instanceof Date) {
                        Date date = (Date) value;
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(date);

                    } else if(value instanceof Long) {
                        cell.setCellValue(Double.parseDouble(value.toString()));
                    } else if(value instanceof String) {
                        cell.setCellValue(value.toString());
                    } else {
                        generateSheet((List)value, wb,
                                sheetname, saveFilePath);


                    }
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        } finally {
            out.flush();
            wb.write(out);
            out.close();
        }
        System.out.println("导出完毕");
        return flag;
    }
}
