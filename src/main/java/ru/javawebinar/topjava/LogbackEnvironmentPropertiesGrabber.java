package ru.javawebinar.topjava;

import ch.qos.logback.core.PropertyDefinerBase;

/**
 * @see <a href="https://stackoverflow.com/a/39072009/9466638"></a>
 */
public class LogbackEnvironmentPropertiesGrabber extends PropertyDefinerBase {
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String LOG_DIR = System.getProperty("user.home") + FILE_SEPARATOR + "topjava" + FILE_SEPARATOR + "kortov";

    @Override
    public String getPropertyValue() {
        return LOG_DIR;
    }
}
