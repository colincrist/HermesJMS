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


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

import com.xerox.amazonws.sqs2.SQSException;

public class SqsQueueReceiver implements QueueReceiver {

	private SqsQueue queue;
	
	private SqsConnectionFactory connectionFactory;
	private MessageListener messageListener;	
	private String selector;
	private ConcurrentLinkedQueue<javax.jms.Message> jmsMessages = new ConcurrentLinkedQueue<javax.jms.Message>();
	private ConcurrentLinkedQueue<javax.jms.Message> deliveredMessages = new ConcurrentLinkedQueue<javax.jms.Message>();	
	private AtomicBoolean closed = new AtomicBoolean(false);
	private SqsQueueSession session;

	public SqsQueueReceiver(SqsQueueSession session, SqsQueue queue,
			SqsConnectionFactory connectionFactory) {
		this.queue = queue;
	
		this.connectionFactory = connectionFactory;
		this.session = session ;
	}

	public void stop()
   {
      // @@TODO
   }

   public void start()
   {
      // @@TODO
   }
	public Queue getQueue() throws JMSException {
		return queue;
	}

	public void close() throws JMSException {
		closed.set(true);
	}

	public MessageListener getMessageListener() throws JMSException {
		return messageListener;
	}

	public String getMessageSelector() throws JMSException {
		return selector;
	}

	public void rollback() throws JMSException
	{
		deliveredMessages.clear();
	}
	
	public void commit() throws JMSException
	{
		for (javax.jms.Message message : deliveredMessages)
		{
			try {
				queue.getMessageQueue().deleteMessage(message.getJMSMessageID()) ;
			} catch (SQSException e) {
				throw new SqsException(e) ;
			}
		}
	}
	
	public void deliver() throws JMSException{
		final MessageListener cachedListener = messageListener;

		if (cachedListener != null) {
			while (jmsMessages.size() > 0) {
				javax.jms.Message message = jmsMessages.remove() ;				
				cachedListener.onMessage(message);
				
				if (session.getTransacted())
				{
					deliveredMessages.add(message) ;
				}
			}
		}
	}

	public boolean poll() throws JMSException {
		try {
			return SqsUtils.readMessages(queue.getMessageQueue(), jmsMessages, connectionFactory
					.getPreFetch());
		} catch (SQSException ex) {
			throw new SqsException(ex);
		}
	}

	private javax.jms.Message receive0(long timeout) throws JMSException {
		if (messageListener != null) {
			throw new SqsException("Receiver is asynchronous");
		}
		
		javax.jms.Message rval = null ;

		if (jmsMessages.size() > 0) {
			rval = jmsMessages.remove();
		} else {
			long entered = System.currentTimeMillis();
			while (!closed.get()) {
				poll();
				if (jmsMessages.size() > 0) {
					rval = jmsMessages.remove();
					break ;
				}

				if (timeout == -1) {
					return null;
				} else if (timeout == 0) {
					try {
						Thread.sleep(connectionFactory.getQueuePollTimeout());
					} catch (InterruptedException e) {
						throw new SqsException(e);
					}
				} else {
					if (System.currentTimeMillis() - entered > timeout) {
						return null;
					}
				}
			}			
		}
		
		if (rval != null)
		{
			if (session.getTransacted())
			{
				deliveredMessages.add(rval) ;
			}
		}
		
		return rval ;
	}

	public javax.jms.Message receive() throws JMSException {
		return receive0(0);
	}

	public javax.jms.Message receive(long timeout) throws JMSException {
		return receive0(timeout);
	}

	public javax.jms.Message receiveNoWait() throws JMSException {
		return receive0(-1);
	}

	public void setMessageListener(MessageListener messageListener)
			throws JMSException {
		this.messageListener = messageListener;
		
		if (messageListener == null)
		{
			session.unregisterAsynchronous(this) ;
		}
		else
		{
			session.registerAsynchronous(this) ;
		}
	}

}
