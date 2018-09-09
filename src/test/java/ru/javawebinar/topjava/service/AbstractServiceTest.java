package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.slf4j.bridge.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.env.*;
import org.springframework.test.context.*;
import org.springframework.test.context.jdbc.*;
import org.springframework.test.context.junit4.*;
import ru.javawebinar.topjava.*;

import static org.hamcrest.CoreMatchers.*;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
abstract public class AbstractServiceTest {
    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;

    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;

    @Autowired
    public Environment env;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    static {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }

    public boolean isJpaBased() {
//        return Arrays.stream(env.getActiveProfiles()).noneMatch(Profiles.JDBC::equals);
        return env.acceptsProfiles(Profiles.JPA, Profiles.DATAJPA);
    }

    //  Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    public <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        try {
            runnable.run();
            Assert.fail("Expected " + exceptionClass.getName());
        } catch (Exception e) {
            Assert.assertThat(getRootCause(e), instanceOf(exceptionClass));
        }
    }
}