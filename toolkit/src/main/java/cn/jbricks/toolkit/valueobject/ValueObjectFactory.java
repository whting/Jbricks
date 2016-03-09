package cn.jbricks.toolkit.valueobject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kuiyuexiang
 * Date: 2012-11-10
 * Time: 下午12:07
 */
public class ValueObjectFactory {

    private List<String> valueObjectlist = new ArrayList<String>();

    private List list = new ArrayList();

    public void setValueObjectlist(List valueObjectlist) {
        this.valueObjectlist = valueObjectlist;
    }

    public void init() {
        String className = null;
        for (int i = 0; i < this.valueObjectlist.size(); i++) {
            className = this.valueObjectlist.get(i);
            try {
                Class clazz = Class.forName(className);

                Object object = clazz.newInstance();

                if (object instanceof Initilize) {
                    ((Initilize) object).init();
                }
                list.add(object);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        Object object = null;
        for (int i = 0; i < this.list.size(); i++) {
            object = this.valueObjectlist.get(i);
            if (object instanceof Initilize) {
                ((Initilize) object).init();
            }
        }
    }

}
