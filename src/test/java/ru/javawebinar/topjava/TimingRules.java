package ru.javawebinar.topjava;

import org.junit.rules.*;
import org.junit.runner.*;
import org.slf4j.*;

import java.util.concurrent.*;

public class TimingRules {
    private static final Logger log = LoggerFactory.getLogger("result");

    private static StringBuilder results = new StringBuilder();

    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public static final Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-95s %7d", description.getDisplayName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };

    public static final ExternalResource SUMMARY = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            results.setLength(0);
        }

        @Override
        protected void after() {
            log.info("\n-------------------------------------------------------------------------------------------------------" +
                    "\nTest                                                                                       Duration, ms" +
                    "\n-------------------------------------------------------------------------------------------------------\n" +
                    results +
                    "-------------------------------------------------------------------------------------------------------\n");
        }
    };
}
