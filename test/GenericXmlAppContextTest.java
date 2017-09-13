
import static org.junit.Assert.*;

import com.ukma.kutsyk.practice_1.domain.transport.Bus;
import com.ukma.kutsyk.framework.core.GenericXmlApplicationContext;
import com.ukma.kutsyk.framework.core.ParserTypes;
import org.junit.Test;
import org.mockito.*;

public class GenericXmlAppContextTest {

    public <T> T getMockObject(Class<T> classToBeMocked) throws Exception {
        return Mockito.mock(classToBeMocked);
    }

    @Test
    public void testCreationDefaultConstructor() {
        GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
        assertNotNull(tester);
    }

    @Test
    public void testCreationStringConstructor() {
        GenericXmlApplicationContext tester = new GenericXmlApplicationContext("filename");
        assertNotNull(tester);
    }

    @Test
    public void testGetBeanFactory() {
        GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
        assertNotNull(tester.getBeanFactory());
    }

    @Test
    public void testGetBean() {
        GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
        assertNotNull(tester.getBeanFactory().bean("bus"));
    }

    @Test
    public void testGetBeanGeneric() {
        GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
        assertNotNull((Bus)tester.getBeanFactory().bean("bus", Bus.class));
    }

   /* @Test
    public void testSetParserTypeSupported() {
        GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
        tester.setParserType(ParserTypes.SAX);
        tester.getReader().loadBeanDefinitions("");
        assertNotNull(tester);
    }*/

    @Test(expected = IllegalArgumentException.class)
    public void testSetParserTypeNotSupported() {
        GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
        tester.setParserType(ParserTypes.StAX);
        tester.getReader().loadBeanDefinitions("");
    }
}