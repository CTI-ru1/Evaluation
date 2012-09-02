package eu.uberdust.evaluation.selenium.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleniumException;
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
    private List<String> pagesToTest = new ArrayList<String>();
    private HashMap<String, Long> pagesTime = new HashMap<String, Long>();
    private static final int TESTS_TO_RUN = 10;

    protected DefaultSelenium createSeleniumClient(String url) throws Exception {
        return new DefaultSelenium("localhost", 4444, "*firefox", url);
    }

    HashMap<String, String> pagesTime2 = new HashMap<String, String>();

    public void testSomethingSimple() throws Exception {
        selenium = createSeleniumClient("http://uberdust.cti.gr");
        selenium.start();
        selenium.setTimeout("60000");

        //LOAD the pages to test
        initPages();

        //Run a test for each page
        for (String page : pagesToTest) {
            long time = runTestForPage(page);
            pagesTime.put(page, time);
        }

        //stop selenium
        selenium.stop();

        //save results
        appendToTestLog(pagesTime);

        //print results
        for (String url : pagesToTest) {
            System.out.println(pagesTime.get(url) + "," + pagesTime2.get(url) + "," + url);
        }
    }

    private void initPages() {
        final String pspaceURL = "http://pspace.dyndns.org:8080/uberdust";
        final String ctiURL = "http://uberdust.cti.gr";

        pagesToTest.add(ctiURL);
        pagesToTest.add(pspaceURL);

        pagesToTest.add(ctiURL + "/rest/testbed/1");
        pagesToTest.add(ctiURL + "/rest/testbed/2");
        pagesToTest.add(ctiURL + "/rest/testbed/3");
        pagesToTest.add(pspaceURL + "/rest/testbed/2");

        String hardwareCTI = ctiURL + "/rest/testbed/1/node/urn:wisebed:ctitestbed:0x9979/";
        pagesToTest.add(hardwareCTI);
        pagesToTest.add(hardwareCTI + "capability/urn:wisebed:node:capability:pir/latestreading");
        pagesToTest.add(hardwareCTI + "capability/urn:wisebed:node:capability:pir/html/limit/10");

        String virtualCTI = "http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:virtual:0.I.9/";
        pagesToTest.add(virtualCTI);
        pagesToTest.add(virtualCTI + "capability/urn:wisebed:node:capability:pir/latestreading");
        pagesToTest.add(virtualCTI + "capability/urn:wisebed:node:capability:pir/html/limit/10");

        String hardwarePSPACE = pspaceURL + "/rest/testbed/2/node/urn:pspace:0x2eb/";

        pagesToTest.add(hardwarePSPACE);
        pagesToTest.add(hardwarePSPACE + "capability/urn:node:capability:light1/latestreading");
        pagesToTest.add(hardwarePSPACE + "capability/urn:node:capability:light1/html/limit/10");

        String virtualPSPACE = pspaceURL + "/rest/testbed/2/node/urn:pspace:virtual:ROOM1/";

        pagesToTest.add(virtualPSPACE);
        pagesToTest.add(virtualPSPACE + "capability/urn:node:capability:light1/latestreading");
        pagesToTest.add(virtualPSPACE + "capability/urn:node:capability:light1/html/limit/10");

    }

    private void appendToTestLog(final HashMap<String, Long> pagesTime) {
        File f = new File("./combinedTimes");
        try {
            FileWriter fw = new FileWriter(f, true);
            long time = System.currentTimeMillis();
            for (String s : pagesTime.keySet()) {
                fw.append(time + "," + pagesTime2.get(s) + "," + s + "," + pagesTime.get(s) + "\n");
            }
            fw.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private long runTestForPage(final String url) {
        long totalTime = 0;
        long uberdustTime = 0;


        for (int i = 0; i < TESTS_TO_RUN; i++) {

            long start = System.currentTimeMillis();
            try {
                selenium.open(url);
            } catch (SeleniumException e) {
                System.out.println("SELENIUM TIMEOUT REACHED in" + url);
                break;
            }
            selenium.waitForPageToLoad("120000");
            totalTime += (System.currentTimeMillis() - start);
            int idx = selenium.getHtmlSource().indexOf("<div id=\"footer\"");
            System.err.println("idx= " + idx);

            if (selenium.getHtmlSource().contains("Exception")) {
                System.out.println("UBERDUST EXCEPTION in " + url);
                continue;
            }
            int endidx = 0;
            if (idx > -1) {
                idx = selenium.getHtmlSource().indexOf(">", idx) + 1;
                endidx = selenium.getHtmlSource().indexOf("<", idx);
//            endidx = idx + 10;

                String val = selenium.getHtmlSource().substring(idx, endidx);
                val = val.replace("page loaded in ", "");
                val = val.replace("milliseconds", "");
                val = val.replace(" ", "");
                val = val.replace("\n", "");

                uberdustTime += Long.parseLong(val);
                pagesTime2.put(url, String.valueOf(uberdustTime / TESTS_TO_RUN));
            }

        }

        return totalTime / TESTS_TO_RUN;
    }


}
