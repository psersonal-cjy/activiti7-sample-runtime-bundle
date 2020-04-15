package SampileActiviti7RuntimeBundle.common.utils;

import java.util.Collection;

/**
 * @ClassName: EmptyUtil
 * @Description: TODO
 * @author: yaozhenhua
 * @date: 2020/4/14 17:00
 */
public class EmptyUtil {
    /**
     * @Title: isEmpty
     * @Description: 判断字符串是否为空，长度为0被认为是空字符串.
     * @param str
     * @return
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (null != str) {
            return str.trim().length() == 0;
        } else {
            return true;
        }
    }
    /**
     * @Title: isEmpty
     * @Description: 判断字符串是否为空，字符串前后空白被截断，截断后长度为0被认为是空字符串
     * @param str
     * @param isTrimed 是否去掉前后空格
     * @return
     * @return boolean
     */
    public static boolean isEmpty(String str, boolean isTrimed) {
        if (isTrimed) {
            return null == str || str.trim().length() == 0;
        } else {
            return  null == str || str.length() == 0;
        }
    }
    /**
     * @Title: isEmpty
     * @Description: 判断列表是否为空，列表没有元素也被认为是空
     * @param collection
     * @return
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.size() == 0;
    }
    /**
     * @Title: isEmpty
     * @Description: 判断数组是否为空
     * @param array
     * @return
     * @return boolean
     */
    public static boolean isEmpty(Object[] array) {
        return null == array || array.length == 0;
    }
    /**
     * @Title: isEmpty
     * @Description: 判断对象是否为空
     * @param obj
     * @return
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }
}
