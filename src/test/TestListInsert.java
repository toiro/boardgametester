/**
 *
 */
package test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author y-kitajima
 * 
 */
public class TestListInsert {

    public static void main(String[] args) {
	List<String> list1 = getList();
	list1.addAll(0, getInsertList());
	System.out.println(list1);

	List<String> list2 = getList();
	list2.addAll(list2.size(), getInsertList());
	System.out.println(list2);

	List<String> list3 = getList();
	list3.addAll(2, getInsertList());
	System.out.println(list3);
    }

    private static List<String> getList() {
	List<String> list = new ArrayList<String>();
	list.add("A");
	list.add("B");
	list.add("C");
	return list;
    }

    private static List<String> getInsertList() {
	List<String> insertList = new ArrayList<String>();
	insertList.add("X");
	insertList.add("Y");
	return insertList;
    }
}
