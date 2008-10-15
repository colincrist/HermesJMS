/* 
 * Copyright 2008 Colin Crist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package jms4sqs;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codestreet.selector.ISelector;
import com.codestreet.selector.Selector;
import com.codestreet.selector.parser.InvalidSelectorException;
import com.xerox.amazonws.sqs2.MessageQueue;

public class SqsQueueBrowser implements QueueBrowser {

	private static Log log = LogFactory.getLog(SqsQueueBrowser.class);
	private Queue queue;
	private MessageQueue messageQueue;
	private SqsConnectionFactory connectionFactory;
	private String selector;
	private ISelector selectorImpl ;

	private class MyEnumeration implements Enumeration<javax.jms.Message> {
		private ConcurrentLinkedQueue<javax.jms.Message> messages = new ConcurrentLinkedQueue<javax.jms.Message>();

		public boolean hasMoreElements() {
			if (messages.size() == 0) {
				try {
					return SqsUtils.readMessages(messageQueue, messages,
							connectionFactory.getPreFetch(), selectorImpl);
				} catch (Exception ex) {
					throw new SqsRuntimeException(ex);
				}
			} else {
				return true;
			}
		}

		public javax.jms.Message nextElement() {
			return messages.remove();
		}
	}

	public SqsQueueBrowser(Queue queue, MessageQueue messageQueue,
			SqsConnectionFactory connectionFactory, String selector) throws JMSException {
		this.queue = queue;
		this.messageQueue = messageQueue;
		this.connectionFactory = connectionFactory;
		this.selector = selector ;
		
		if (selector != null)
		{
		   try
		   {
		      this.selectorImpl = Selector.getInstance(selector) ;
		   }
		   catch (InvalidSelectorException ex)
		   {
		      throw new SqsException(ex) ;
		   }
		}
		
	}

	public void close() throws JMSException {

	}

	public Enumeration getEnumeration() throws JMSException {
		return new MyEnumeration();
	}

	public String getMessageSelector() throws JMSException {
		return selector;
	}

	public Queue getQueue() throws JMSException {
		return queue;
	}

}
