package eu.uberdust.evaluation.selenium.test;

import com.thoughtworks.selenium.DefaultSelenium;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeleniumExampleTest
        extends TestCase {
    private DefaultSelenium selenium;

    protected DefaultSelenium createSeleniumClient(String url) throws Exception {
        return new DefaultSelenium("localhost", 4444, "*firefox", url);
    }

    public void testSomethingSimple() throws Exception {
        selenium = createSeleniumClient("http://uberdust.cti.gr");
        selenium.start();

        List<String> pagesToTest = new ArrayList<String>();
        HashMap<String, Long> pagesTime = new HashMap<String, Long>();
        pagesToTest.add("http://uberdust.cti.gr");
        pagesToTest.add("http://uberdust.cti.gr");
        pagesToTest.add("http://uberdust.cti.gr/rest/testbed/1");
        pagesToTest.add("http://uberdust.cti.gr/rest/testbed/2");
        pagesToTest.add("http://uberdust.cti.gr/rest/testbed/3");
        String single = "http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:0x9979/";
        pagesToTest.add(single);
        pagesToTest.add(single + "capability/urn:wisebed:node:capability:pir/latestreading");
//        pagesToTest.add(single+"capability/urn:wisebed:node:capability:pir/latestreading/json");
        pagesToTest.add(single + "capability/urn:wisebed:node:capability:pir/html/limit/10");
        pagesToTest.add(single + "capability/urn:wisebed:node:capability:pir/tabdelimited/limit/10");
//        pagesToTest.add(single+"capability/urn:wisebed:node:capability:pir/json/limit/10");

        String virtual = "http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:virtual:0.I.9/";
        pagesToTest.add(virtual);
        pagesToTest.add(virtual + "capability/urn:wisebed:node:capability:pir/latestreading");
//        pagesToTest.add(virtual+"capability/urn:wisebed:node:capability:pir/latestreading/json");
        pagesToTest.add(virtual + "capability/urn:wisebed:node:capability:pir/html/limit/10");
        pagesToTest.add(virtual + "capability/urn:wisebed:node:capability:pir/tabdelimited/limit/10");
//        pagesToTest.add(virtual+"capability/urn:wisebed:node:capability:pir/json/limit/10");

        for (String page : pagesToTest) {
            long time = 0;
            for (int i = 0; i < 10; i++) {
                time += loadAndLog(page);
            }
            pagesTime.put(page, time / 10);
        }


        selenium.stop();

        logRun(pagesTime);
        for (String url : pagesToTest) {
            System.out.println(pagesTime.get(url) + "," + url);

        }
    }

    private void logRun(HashMap<String, Long> pagesTime) {
        File f = new File("./testTimes");
        try {
            FileWriter fw = new FileWriter(f, true);
            long time = System.currentTimeMillis();
            for (String s : pagesTime.keySet()) {
                fw.append(time + "," + s + "," + pagesTime.get(s) + "\n");
            }
            fw.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    private long loadAndLog(String url) {
        long start = System.currentTimeMillis();
        selenium.open(url);
        selenium.waitForPageToLoad("120000");
        return (System.currentTimeMillis() - start);
    }


}
