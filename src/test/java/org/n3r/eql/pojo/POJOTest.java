package org.n3r.eql.pojo;


import org.junit.BeforeClass;
import org.junit.Test;
import org.n3r.eql.Eql;
import org.n3r.eql.pojo.annotations.EqlColumn;
import org.n3r.eql.pojo.annotations.EqlId;
import org.n3r.eql.pojo.annotations.EqlSkip;
import org.n3r.eql.pojo.annotations.EqlTable;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class POJOTest {
    @BeforeClass
    public static void beforeClass() {
        new Eql("mysql").execute("drop table if exists personx", "create table personx (id varchar(100), name varchar(100), age int)");
    }

    @Test
    public void test1() {
        Personx person = new Personx();
        person.setId("1002");
        person.setName("bingoo");
        person.setAge(30);

        new Pql("mysql").delete(person);

        new Pql("mysql").create(person); // insert into person（id,name,age) values(?,?,?)

        person.setName("huang");
        int effectedRows = new Pql("mysql").update(person); // update person set name = ?, age = ? where id = ?
        assertThat(effectedRows, is(1));

        effectedRows = new Pql("mysql").update(person, "name");// update person set name = ? where id = ?
        assertThat(effectedRows, is(1));

        Personx queryPerson = new Personx();
        List<Personx> resultPerson = new Pql("mysql").read(queryPerson); // select id,name,age from person where id = ?

        queryPerson.setId("1002");
        effectedRows = new Pql("mysql").delete(queryPerson); // delete from person where id = ?
        assertThat(effectedRows, is(1));
    }

    @Test
    public void testAnnotation() {
        Person2 person = new Person2();
        person.setPid("1002");
        person.setPname("bingoo");
        person.setAge(30);

        // delete from person where id = ?
        new Pql("mysql").delete(person);

        // insert into person（id,name,age) values(?,?,?)
        new Pql("mysql").create(person);

        person.setPname("huang");
        person.setAge(null);
        // update person set age = ? where id = ?
        int effectedRows = new Pql("mysql").update(person);
        assertThat(effectedRows, is(1));

        Person2 queryPerson = new Person2();
        queryPerson.setPid("1002");

        // select id,name,age from person where id = ?
        List<Person2> resultPerson = new Pql("mysql").read(queryPerson);
        assertThat(resultPerson.size(), is(1));

        effectedRows = new Pql("mysql").delete(queryPerson);
        assertThat(effectedRows, is(1));
    }

    public static class Personx {
        private String id;
        private String name;
        private int age;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    @EqlTable(name = "personx")
    public static class Person2 {
        @EqlId
        @EqlColumn(name = "id")
        private String pid;
        @EqlColumn(name = "name")
        private String pname;
        private Integer age;

        @EqlSkip
        private String remark;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
