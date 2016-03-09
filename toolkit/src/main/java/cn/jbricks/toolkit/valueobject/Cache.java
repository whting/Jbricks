package cn.jbricks.toolkit.valueobject;

import java.util.*;

/**
 * User: kuiyuexiang
 * Date: 2012-11-24
 * Time: 下午12:26
 */
public class Cache<T> {

    private final Map<Long, T> _allMap = new HashMap<Long, T>();
    private final List<Option> _parentList = new ArrayList<Option>();
    private final Map<Integer, ArrayList<Option>> _childMap = new HashMap<Integer, ArrayList<Option>>();
    private static final Map<String, ArrayList<Option>> _grandsonMap = new HashMap<String, ArrayList<Option>>();

    public List<T> getAllList() {
        List list = new ArrayList(_allMap.size());
        Set<Long> keySet = _allMap.keySet();
        Iterator iter = keySet.iterator();
        while (iter.hasNext()) {
            list.add(_allMap.get(iter.next()));
        }

        return list;
    }

    public List<Option> getParentList() {
        return _parentList;
    }

    public List<Option> getChildList(Integer parentValue) {
        return _childMap.get(parentValue);
    }

    public List<Option> getGrandsonList(Integer parentValue, Integer childValue) {
        return _grandsonMap.get(parentValue.toString() + childValue.toString());
    }

    public T getValueObject(Long uid) {
        return _allMap.get(uid);
    }

    public void toCache(Integer parentValue, String parentName,
                        Integer childValue, String childName,
                        Integer grandsonValue, String grandsonName, Long uid, T object) {
        _allMap.put(uid, object);

        Option option = new Option(parentValue, parentName);
        if (!_parentList.contains(option)) {
            _parentList.add(option);
        }

        if (childValue != null) {
            ArrayList<Option> list = _childMap.get(parentValue);
            if (list == null) {
                list = new ArrayList<Option>();
                _childMap.put(parentValue, list);
            }

            option = new Option(childValue, childName);
            if (!list.contains(option)) {
                list.add(option);
            }

            if (grandsonValue != null) {
                list = _grandsonMap.get(parentValue.toString() + childValue.toString());
                if (list == null) {
                    list = new ArrayList<Option>();
                    _grandsonMap.put(parentValue.toString() + childValue.toString(), list);
                }

                option = new Option(grandsonValue, grandsonName);
                if (!list.contains(option)) {
                    list.add(option);
                }
            }
        }

    }

}

