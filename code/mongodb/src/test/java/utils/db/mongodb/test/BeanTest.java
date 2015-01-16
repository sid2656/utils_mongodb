/**
 * Project Name:bean
 * File Name:BeanTest.java
 * Package Name:com.hdsx.taxi.upa.bean
 * Date:2014年10月22日上午10:41:30
 * Copyright (c) 2014, sid Jenkins All Rights Reserved.
 * 
 *
*/

package utils.db.mongodb.test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName:BeanTest
 * Date:     2014年10月22日 上午10:41:30 
 * @author   sid
 * @see 	 
 */
public class BeanTest {
    private String name;
 
    private String sex;
    
    private int age;
    
    private Date birthday;
 
    public BeanTest() {
        super();
    }
 
    public BeanTest(String name, String sex, int age, Date birthday) {
		super();
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.birthday = birthday;
	}


	public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getSex() {
        return sex;
    }
 
    public void setSex(String sex) {
        this.sex = sex;
    }

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getBirthdayStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(birthday);
	}
}