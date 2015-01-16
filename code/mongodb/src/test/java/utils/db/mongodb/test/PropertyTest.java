/**
 * Project Name:bean
 * File Name:PropertyTest.java
 * Package Name:com.hdsx.taxi.upa.bean
 * Date:2014年10月22日上午10:42:10
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.db.mongodb.test;
/**
 * ClassName:PropertyTest
 * Date:     2014年10月22日 上午10:42:10 
 * @author   sid
 * @see 	 
 */
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
 
public class PropertyTest {
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
    	HashMap<String, Object> map = new HashMap<String, Object>();
        Method metd = null;
        String fdname = null;
        // 添加两个测试数据。。。
        List list = new ArrayList();
        list.add(new BeanTest("张三", "女",12,new Date()));
        list.add(new BeanTest("李四", "男",13,new Date()));
        try {
            // 遍历集合
            for (Object object : list) {
                Class<?> clazz = object.getClass();// 获取集合中的对象类型
                Field[] fds = clazz.getDeclaredFields();// 获取他的字段数组
                for (Field field : fds) {// 遍历该数组
                    fdname = field.getName();// 得到字段名，
 
                    metd = clazz.getMethod("get" + change(fdname));// 根据字段名找到对应的get方法，null表示无参数
 
                    Object value = metd.invoke(object);
                    System.out.println(fdname+":"+value);
                    map.put(fdname, value);
//                    if ("name".equals(fdname) && metd != null) {// 比较是否在字段数组中存在name字段，如果不存在短路，如果存在继续判断该字段的get方法是否存在，同时存在继续执行
//                        Object name = metd.invoke(object, null);// 调用该字段的get方法
//                        System.out.print("姓名:" + name);// 输出结果
//                    }
//                    if ("sex".equals(fdname) && metd != null) {// 同上
//                        Object sex = metd.invoke(object, null);
//                        System.out.println("\t性别:" + sex);
//                    }
                }
            }
            
            Set<String> keySet = map.keySet();
            for (String string : keySet) {
				System.out.println(string+"--"+map.get(string));
			}
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * @param src
     *            源字符串
     * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
     */
    public static String change(String src) {
        if (src != null) {
            StringBuffer sb = new StringBuffer(src);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        } else {
            return null;
        }
    }
}