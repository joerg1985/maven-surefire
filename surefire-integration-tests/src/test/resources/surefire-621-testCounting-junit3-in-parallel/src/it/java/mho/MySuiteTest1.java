package mho;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by IntelliJ IDEA.
 * User: yyit927b
 * Date: 26.05.2010
 * Time: 11:02:06
 * To change this template use File | Settings | File Templates.
 */
public class MySuiteTest1 extends TestCase {

    public static Test suite () {
        TestSuite suite = new TestSuite();

        suite.addTest (new MySuiteTest1("testMe", 1));

        return suite;
    }

    private int number;

    public MySuiteTest1(String name, int number) {
        super (name);
        this.number = number;
    }

    public void testMe() {
        System.out.println ("### "+ this.getClass().getName()+":"+this.getName()+" - number "+number);
        assertTrue (true);
    }
}
