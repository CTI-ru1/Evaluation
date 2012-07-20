package eu.uberdust.evaluation.parser.test;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * TestClass.
 */
public class TimingAppTest {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TimingAppTest.class);

    private Map<String, Map<Long, Long>> timings = new HashMap<String, Map<Long, Long>>();

    public TimingAppTest() {
        URL url = null;
        try {
            url = new URL("http://qopbot.dyndns.org/hudson/job/uberdustTests/ws/testTimes");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.exit(1);
        }


        try {
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = in.readLine();
            while (str != null) {
                String[] parts = str.split(",");
                if (parts.length == 3) {
                    if (!timings.containsKey(parts[1])) {
                        LOGGER.debug(parts[1]);
                        timings.put(parts[1], new HashMap<Long, Long>());
                    }
                    timings.get(parts[1]).put(Long.parseLong(parts[0]), Long.parseLong(parts[2]));
                }
                str = in.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void plottingTest() {
        LOGGER.info("plottingTest");

        //LOAD HOME PAGE
        listTestbedTests("Home Page-http://uberdust.cti.gr", "home.png");

        //LOAD TESTBEDS
        listTestbedTests("TBed1-http://uberdust.cti.gr/rest/testbed/1,TBed2-http://uberdust.cti.gr/rest/testbed/2,TBed3-http://uberdust.cti.gr/rest/testbed/3", "testbeds.png");

        //LOAD NODES
        listTestbedTests("Virtual Node-http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:virtual:0.I.9/,Hardware Node-http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:0x9979/", "nodes.png");

        //LOAD SINGLE READING
        listTestbedTests("Virtual Node-http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:virtual:0.I.9/capability/urn:wisebed:node:capability:pir/latestreading,Hardware Node-http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:0x9979/capability/urn:wisebed:node:capability:pir/latestreading", "single.png");

        //LOAD MULTIPLE READINGS
        listTestbedTests("Virtual Node-http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:virtual:0.I.9/capability/urn:wisebed:node:capability:pir/tabdelimited/limit/10,Hardware Node-http://uberdust.cti.gr/rest/testbed/1/node/urn:wisebed:ctitestbed:0x9979/capability/urn:wisebed:node:capability:pir/tabdelimited/limit/10", "multiple.png");

    }

    @Test
    public void averageTimeTest() {
        LOGGER.info("averageTimeTest");
        for (String s : timings.keySet()) {
            double average = 0;
            int counter = 0;
            for (Long s1 : timings.get(s).keySet()) {
                average += timings.get(s).get(s1);
                counter++;
            }
            LOGGER.info((average / counter) + "msec @ " + s);
        }
    }

    private void listTestbedTests(String urls, String title) {
        String[] parts = urls.split(",");
        final XYSeries[] lines = new XYSeries[parts.length];
        for (int i = 0; i < parts.length; i++) {
            lines[i] = new XYSeries(parts[i].split("-")[0]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < parts.length; i++) {
            final String item = parts[i].split("-")[1];
            Map<Long, Long> ao = timings.get(item);
            for (Long aLong : ao.keySet()) {
                lines[i].addOrUpdate(aLong, ao.get(aLong));
            }

            dataset.addSeries(lines[i]);

        }
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "",
                "Time of Experiment",
                "Response Time (in msec)",
                dataset,
                true, true, false);


        chart.getPlot().setBackgroundPaint(Color.white);
        chart.getXYPlot().setDomainGridlinePaint(Color.white);
        chart.getXYPlot().setRangeGridlinePaint(Color.gray);
        for (int i = 0; i < lines.length; i++) {
            chart.getXYPlot().getRenderer().setSeriesStroke(i, new BasicStroke(3));
        }
        DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();

        axis.setDateFormatOverride(new SimpleDateFormat("d-M HH:mm"));
        try {
            ChartUtilities.saveChartAsPNG(new File(title), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}


