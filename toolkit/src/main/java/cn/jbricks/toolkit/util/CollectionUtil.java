/**
 *
 */
package cn.jbricks.toolkit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * Collection��صĹ��߷���
 * @author afei
 * @version 2007-7-5
 */
public class CollectionUtil {
    public static final Logger logger = LoggerFactory.getLogger(CollectionUtil.class);
    public static final Long[] EMPTY_ARRAY_LONG = new Long[0];

//    /**
//     * �ж�Collection�Ƿ�null����sizeΪ0
//     * @param c
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static final boolean isEmpty(Collection c) {
//        return null == c || 0 == c.size()? true : false;
//    }
//
//    /**
//     * ʹ��logger.debug()��ӡ����Ķ���
//     * һ�����ڵ�Ԫ����
//     * @param c
//     */
//    @SuppressWarnings("unchecked")
//    public static final void print(Collection c) {
//        if(isEmpty(c)) {
//            return;
//        }
//        for (Object object : c) {
//            logger.debug(object);
//        }
//    }
//
//    /**
//     * ��List��ÿ�������г�ȡidֵ, List�ж��������getId()����
//     * @param list
//     * @return String[] ids
//     */
//    @SuppressWarnings("unchecked")
//    public static final String[] getIdsFromListToStringArray(List list) {
//        if(null == list || 0 == list.size()) {
//            return ArrayUtil.EMPTY_STRING_ARRAY;
//        }
//
//        List<String> ids = new ArrayList<String>();
//        for (Object object : list) {
//            try {
//                Object id = BeanUtils.getProperty(object, Constants.KEY_ID);
//                if(null != id) {
//                    ids.add(String.valueOf(id));
//                }
//            } catch (Exception ex) {
//                logger.info("[CollectionUtil.getIdsFromListToStringArray] cant get ID from " + object);
//                continue;
//            }
//        }
//
//        return ids.toArray(new String[ids.size()]);
//    }
//
//    /**
//     * ��List��ÿ�������г�ȡidֵ, List�ж��������getId()����
//     * @param list
//     * @return Integer[] ids
//     */
//    @SuppressWarnings("unchecked")
//    public static final Integer[] getIdsFromListToIntegerArray(List list) {
//        if(null == list || 0 == list.size()) {
//            return ArrayUtil.EMPTY_INTEGER_OBJECT_ARRAY;
//        }
//
//        List<Integer> ids = new ArrayList<Integer>();
//        for (Object object : list) {
//            try {
//                Object id = BeanUtils.getProperty(object, Constants.KEY_ID);
//                if(null != id) {
//                    ids.add(Integer.valueOf(String.valueOf(id)));
//                }
//            } catch (Exception ex) {
//                logger.info("[CollectionUtil.getIdsFromListToIntegerArray] cant get ID from " + object);
//                continue;
//            }
//        }
//
//        return ids.toArray(new Integer[ids.size()]);
//    }
//
//    /**
//     * ��List��ÿ�������г�ȡidֵ, List�ж��������getId()����
//     * @param list
//     * @return Long[] ids
//     */
//    @SuppressWarnings("unchecked")
//    public static final Long[] getIdsFromListToLongArray(List list) {
//        if(null == list || 0 == list.size()) {
//            return ArrayUtil.EMPTY_LONG_OBJECT_ARRAY;
//        }
//
//        List<Integer> ids = new ArrayList<Integer>();
//        for (Object object : list) {
//            try {
//                Object id = BeanUtils.getProperty(object, Constants.KEY_ID);
//                if(null != id) {
//                    ids.add(Integer.valueOf(String.valueOf(id)));
//                }
//            } catch (Exception ex) {
//                logger.info("[CollectionUtil.getIdsFromListToLongArray] cant get ID from " + object);
//                continue;
//            }
//        }
//
//        return ids.toArray(new Long[ids.size()]);
//    }
//
//    /**
//     * ��listת����String[]
//     * ��String[] groupids = (String[])list.toArray(new String[list.size()]);
//     * @param list
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static final String[] toStringArray(List list) {
//        if(CollectionUtil.isEmpty(list)) {
//            return Constants.EMPTY_ARRAY_STRING;
//        }
//        return (String[])list.toArray(new String[list.size()]);
//    }
//
//    /**
//     * ��listת����Integer[]
//     * ��Integer[] groupids = (Integer[])list.toArray(new Integer[list.size()]);
//     * @param list
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static final Integer[] toIntegerArray(List list) {
//        if(CollectionUtil.isEmpty(list)) {
//            return Constants.EMPTY_ARRAY_INTEGER;
//        }
//        return (Integer[])list.toArray(new Integer[list.size()]);
//    }
//
//    /**
//     * ��listת����Long[]
//     * ��Long[] groupids = (Long[])list.toArray(new Long[list.size()]);
//     * @param list
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static final Long[] toLongArray(List list) {
//        if(CollectionUtil.isEmpty(list)) {
//            return Constants.EMPTY_ARRAY_LONG;
//        }
//        return (Long[])list.toArray(new Long[list.size()]);
//    }
//
//    /**
//     * ��setת����String[]
//     * ��String[] groupids = (String[])set.toArray(new String[set.size()]);
//     * @param set
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static final String[] toStringArray(Set set) {
//        if(CollectionUtil.isEmpty(set)) {
//            return Constants.EMPTY_ARRAY_STRING;
//        }
//        return (String[])set.toArray(new String[set.size()]);
//    }
//
//    /**
//     * ��setת����Integer[]
//     * ��Integer[] groupids = (Integer[])set.toArray(new Integer[set.size()]);
//     * @param set
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static final Integer[] toIntegerArray(Set set) {
//        if(CollectionUtil.isEmpty(set)) {
//            return Constants.EMPTY_ARRAY_INTEGER;
//        }
//        return (Integer[])set.toArray(new Integer[set.size()]);
//    }
//
    /**
     * ��setת����Long[]
     * ��Long[] groupids = (Long[])set.toArray(new Long[set.size()]);
     * @param set
     * @return
     */
    @SuppressWarnings("unchecked")
    public static final Long[] toLongArray(Set set) {
        if(CollectionUtil.isEmpty(set)) {
            return EMPTY_ARRAY_LONG;
        }
        return (Long[])set.toArray(new Long[set.size()]);
    }
//
//    /**
//     * ������Strת����String[]
//     * @param str
//     * @return
//     */
//    public static final String[] toStringArray(String str) {
//        if(StringUtil.isBlank(str)) {
//            return Constants.EMPTY_ARRAY_STRING;
//        }
//        return new String[] {str};
//    }
//
//    /**
//     * ������intת����String[]
//     * @param i
//     * @return
//     */
//    public static final String[] toStringArray(int i) {
//        return new String[] {String.valueOf(i)};
//    }
//
//    /**
//     * ������longת����String[]
//     * @param i
//     * @return
//     */
//    public static final String[] toStringArray(long i) {
//        return new String[] {String.valueOf(i)};
//    }
//
//    /**
//     * ��int[]ת����String[]
//     * @param is
//     * @return
//     */
//    public static final String[] toStringArray(int[] is) {
//        if(null == is || 0 == is.length) {
//            return Constants.EMPTY_ARRAY_STRING;
//        }
//
//        int length = is.length;
//        String[] strings = new String[length];
//        for (int i = 0; i < length; i++) {
//            String s = String.valueOf(is[i]);
//            strings[i] = s;
//        }
//        return strings;
//    }
//
//    /**
//     * ��long[]ת����String[]
//     * @param is
//     * @return
//     */
//    public static final String[] toStringArray(long[] is) {
//        if(null == is || 0 == is.length) {
//            return Constants.EMPTY_ARRAY_STRING;
//        }
//
//        int length = is.length;
//        String[] strings = new String[length];
//        for (int i = 0; i < length; i++) {
//            String s = String.valueOf(is[i]);
//            strings[i] = s;
//        }
//        return strings;
//    }
//
//    /**
//     * ��List��Ϊnull��Ԫ��ɾ���
//     */
//    @SuppressWarnings("unchecked")
//    public static final List chopList(List list) {
//        if(null == list) {
//            return null;
//        } else {
//            for (int i = 0; i < list.size(); i++) {
//                if(null == list.get(i)) {
//                    list.remove(i);
//                    i--;
//                }
//            }
//            return list;
//        }
//    }
//
    /**
	 * �ж�ָ����һ����������, �Ƿ���ڿռ���
	 *
	 * @param colls �����, һ������
	 * @return �Ƿ���ڿռ���
	 * @author xiaofeng
	 */
	public static boolean isEmpty(Collection<?>... colls) {
		boolean result = false;

		for (Collection<?> coll : colls) {
			if (null == coll || coll.isEmpty()) {
				result = true;
				break;
			}
		}

		return result;
	}
//
//	/**
//	 * �ж�ָ����һ����������, �Ƿ���ڿռ���
//	 *
//	 * @param colls �����, һ������
//	 * @return �Ƿ���ڿռ���
//	 * @author xiaofeng
//	 */
//	public static boolean isEmpty(Map<?, ?>... maps) {
//		boolean result = false;
//
//		for (Map<?, ?> map : maps) {
//			if (null == map || map.isEmpty()) {
//				result = true;
//				break;
//			}
//		}
//
//		return result;
//	}
//
//	/**
//	 * �ж�ָ����һ����������, �Ƿ��Ƿǿռ���
//	 *
//	 * @param colls �����, һ������
//	 * @return �Ƿ��Ƿǿռ���
//	 * @author xiaofeng
//	 */
//	public static boolean isNotEmpty(Collection<?>... colls) {
//		return !isEmpty(colls);
//	}
//
//	/**
//	 * �ж�ָ����һ����������, �Ƿ��Ƿǿռ���
//	 *
//	 * @param colls �����, һ������
//	 * @return �Ƿ��Ƿǿռ���
//	 * @author xiaofeng
//	 */
//	public static boolean isNotEmpty(Map<?, ?>... maps) {
//		return !isEmpty(maps);
//	}
//
//	/**
//     * �ж�Collection�Ƿ�Ϊ��
//     * @param c ����
//     * @return �Ƿ�Ϊ��
//     * @author xiaofeng
//     */
//    public static final boolean isNotEmpty(Collection<?> c) {
//        return null != c && !c.isEmpty();
//    }
//
//    /**
//     * ����list֮��merge����ʱ��֧�ֶ�����Ϊkey
//     * @param toList Ŀ��list
//     * @param sourceKeyProperty Դlist�ж����ӳ���Ψһ property
//     * @param setProperty   Դlist�ж�������dest�ж���� property
//     * @param fromList Դlist
//     * @param fromKeyProperty Ŀ���ӳ������Ӧ�� property
//     */
//    public static final void merge(List<? extends Object> toList, String toKeyProperty, String setProperty,  List<? extends Object> fromList, String fromKeyProperty)
//    {
//        //��Ϊ��
//        if (!CollectionUtil.isEmpty(toList) && !CollectionUtil.isEmpty(fromList)){
//            try{
//                //���� from ��Map
//                Map<String, Object> m = new HashMap<String, Object>();
//                for (Object from : fromList){
//                    m.put(BeanUtils.getProperty(from, fromKeyProperty), from);
//                }
//
//                //����
//                for (Object to : toList){
//                    String v = BeanUtils.getProperty(to, toKeyProperty);
//                    Object from = m.get(v);
//                    if (from != null){
//                        BeanUtils.setProperty(to, setProperty, from);
//                    }
//                }
//            }
//            catch (Exception e){
//                logger.error(e, e);
//            }
//        }
//    }
//
//    /**
//     * ����list֮��merge����ʱֻ֧�� Number, Long, Integer, Float
//     * @param toList Ŀ��list
//     * @param sourceKeyProperty Դlist�ж����ӳ���Ψһ property
//     * @param setProperty   Դlist�ж�������dest�ж���� property
//     * @param fromList Դlist
//     * @param destKeyProperty Ŀ���ӳ������Ӧ�� property
//     * @throws ManagerException
//     */
//    public static final void merge(List<? extends Object> toList, String toKeyProperty, String setProperty,
//            Map<? extends Object, ? extends Object> fromMap)
//    {
//        //��Ϊ��
//        if (!CollectionUtil.isEmpty(toList) && !MapUtil.isEmpty(fromMap)){
//            Map<String, Object> tmpMap = new HashMap<String, Object>();
//            for (Map.Entry<? extends Object, ? extends Object> e : fromMap.entrySet()){
//                tmpMap.put(String.valueOf(e.getKey()), e.getValue());
//            }
//
//            //����
//            for (Object to : toList){
//                try{
//                    String v = BeanUtils.getProperty(to, toKeyProperty);
//                    Object o = tmpMap.get(v);
//                    if (o != null){
//                        BeanUtils.setProperty(to, setProperty, o);
//                    }
//                }
//                catch (Exception e){
//                    logger.error(e, e);
//                }
//            }
//        }
//    }
//
//    /**
//     * ��list�еĶ���� property ��װ�� ���� "1,2,3,4"����ʽ
//     * @param source
//     * @param sourceKeyProperty
//     * @return String
//     */
//    public static final String list2String(List<? extends Object> source, String sourceKeyProperty){
//        return list2String(source, sourceKeyProperty, Constants.DELIMITER_COMMA, false);
//    }
//
//
//    /**
//     * ��list�еĶ���� property ��װ�� ���� "1,2,3,4"����ʽ
//     * @param source
//     * @param sourceKeyProperty
//     * @param omitDuplicate
//     * @return String
//     */
//    public static final String list2String(List<? extends Object> source, String sourceKeyProperty, boolean omitDuplicate){
//        return list2String(source, sourceKeyProperty, Constants.DELIMITER_COMMA, omitDuplicate);
//    }
//
//    /**
//     * ��list�еĶ���� property ��װ�� ���� "1,2,3,4"����ʽ
//     * @param source Դlist
//     * @param sourceKeyProperty Դlist�ж���� property
//     * @param separator �ָ���
//     * @param omitDuplicate �Ƿ�ȥ�� �ظ���id
//     * @return  ��װ�õ� String
//     */
//    public static final String list2String(List<? extends Object> source,
//            String sourceKeyProperty, String separator, boolean omitDuplicate) {
//        return list2String(source, sourceKeyProperty, separator, omitDuplicate, -1);
//    }
//
//    /**
//     * ��list�еĶ���� property ��װ�� ���� "1,2,3,4"����ʽ
//     * @param source Դlist
//     * @param sourceKeyProperty Դlist�ж���� property
//     * @param separator �ָ���
//     * @param omitDuplicate �Ƿ�ȥ�� �ظ���id
//     * @param size ���ص�idֵ���� ���sizeС�ڵ���0��ʾ�������� ���size����id�����򷵻�����
//     * @return  ��װ�õ� String
//     */
//    public static final String list2String(List<? extends Object> source,
//            String sourceKeyProperty, String separator, boolean omitDuplicate, int size)
//    {
//        if (separator == null){
//            separator = Constants.DELIMITER_COMMA;
//        }
//        String s = null;
//        if (!CollectionUtil.isEmpty(source)){
//            Collection<Object> c = getObjectCollection(source, sourceKeyProperty, omitDuplicate);
//            StringBuilder sBuilder = new StringBuilder();
//            if (c != null){
//                int count = 0;
//                for (Object value : c){
//                    if (0 < size && count >= size) {
//                        break;
//                    }
//                    count ++;
//                    sBuilder.append(value).append(separator);
//                }
//                s = sBuilder.toString();
//                if (s.endsWith(separator)){
//                    s = s.substring(0, s.length() - separator.length());
//                }
//            }
//        }
//        return s;
//    }
//
//    /**
//     * @param source
//     * @param sourceKeyProperty
//     * @param omitDuplicate
//     * @return Collection<Object>
//     */
//    public static final Collection<Object> getObjectCollection(List<? extends Object> source, String sourceKeyProperty, boolean filterDuplicate){
//        Collection<Object> c = null;
//        if (filterDuplicate){
//            c = new HashSet<Object>();
//        }else{
//            c = new ArrayList<Object>();
//        }
//
//        for (Object o : source){
//            try{
//                c.add(BeanUtils.getProperty(o,sourceKeyProperty));
//            }
//            catch (Exception e){
//                logger.error(e, e);
//            }
//        }
//        return c;
//    }
//
//    /**
//     * ��Collectionת����string
//     * @param collection
//     * @return String
//     */
//    public static String collection2String(Collection<? extends Object> collection){
//        return collection2String(collection, Constants.DELIMITER_COMMA);
//    }
//
//    /**
//     * ���� string
//     * @param set
//     * @return String
//     */
//    public static String collection2String(Collection<? extends Object> collection, String separator){
//        if (separator == null){
//            separator = Constants.DELIMITER_COMMA;
//        }
//        StringBuilder sBuilder = new StringBuilder();
//        for (Object o : collection){
//            sBuilder.append(String.valueOf(o)).append(separator);
//        }
//        String ret = sBuilder.toString();
//        if (ret.endsWith(separator)){
//            ret = ret.substring(0, ret.lastIndexOf(separator));
//        }
//        return ret;
//    }
//
//    /**
//     * ����long ����
//     * @param source
//     * @param sourceKeyProperty
//     * @return list2longArray
//     */
//    public static final long[] list2longArray(List<? extends Object> source, String sourceKeyProperty){
//        return list2longArray(source, sourceKeyProperty, false);
//    }
//
//    /**
//     * ��list�еĶ����property ��װ�� long ����
//     * @param source Դlist
//     * @param sourceKeyProperty Դlist�ж���� property
//     * @return long[]
//     */
//    public static final long[] list2longArray(List<? extends Object> source, String sourceKeyProperty, boolean filterDuplicate)
//    {
//        long[] array = null;
//        if (!CollectionUtil.isEmpty(source)){
//            Collection<Object> c = getObjectCollection(source, sourceKeyProperty, filterDuplicate);
//            if (c != null){
//                array = collection2longArray(c);
//            }
//        }
//        return array;
//    }
//
//    public static final long[] collection2longArray(Collection<Object> collection){
//        long[] array =  new long[collection.size()];
//        int index = 0;
//        for (Object value : collection){
//            try{
//                array[index] = Long.parseLong(String.valueOf(value));
//                index ++;
//            }
//            catch (Exception e){
//                logger.error(e, e);
//            }
//        }
//        return array;
//    }
}
