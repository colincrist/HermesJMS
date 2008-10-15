package test;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import jms4sqs.SqsConnectionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestBrowser {
	private static Log log = LogFactory.getLog(TestBrowser.class);

	public static void main(String[] args) {
		try {
			SqsConnectionFactory cf = new SqsConnectionFactory();

			Connection connection = cf.createConnection("", "") ;
			Session session = connection.createSession(true, Session.SESSION_TRANSACTED) ;
			Queue queue = session.createQueue("NEW") ;
			MessageProducer sender = session.createProducer(queue) ;
			TextMessage message = session.createTextMessage("This is a test") ;
			
			sender.send(message) ;
			session.commit() ;
			
			QueueBrowser browser = session.createBrowser(queue) ;
			Enumeration<Message> iter = browser.getEnumeration() ;
			
			while (iter.hasMoreElements())
			{
			   TextMessage in = (TextMessage) iter.nextElement() ;
			   log.debug(in.getText()) ;
			}
			
			browser.close() ;
			sender.close() ;
			session.close() ;
			connection.close() ;		
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
