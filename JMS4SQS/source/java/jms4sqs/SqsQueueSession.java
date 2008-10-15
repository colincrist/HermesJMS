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

import java.io.Serializable;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import jms4sqs.messages.TextMessageImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xerox.amazonws.sqs2.QueueService;
import com.xerox.amazonws.sqs2.SQSException;

public class SqsQueueSession implements QueueSession
{
   private static Log log = LogFactory.getLog(SqsQueueSession.class);
   private QueueService queueService;
   private SqsConnectionFactory connectionFactory;
   private boolean transacted;
   private int acknowledgeMode;
   private List<SqsQueueReceiver> asynchronousReceivers = Collections.synchronizedList(new ArrayList<SqsQueueReceiver>());
   private List<SqsQueueReceiver> allReceivers = Collections.synchronizedList(new ArrayList<SqsQueueReceiver>());
   private List<SqsQueueSender> allSenders = Collections.synchronizedList(new ArrayList<SqsQueueSender>());
   private AtomicBoolean closed = new AtomicBoolean(false);
   private Thread asychronousDelivery;
   private MessageListener messageListener ;

   public SqsQueueSession(QueueService queueService, SqsConnectionFactory connectionFactory, boolean transacted, int acknowledgeMode)
   {
      this.queueService = queueService;
      this.connectionFactory = connectionFactory;
      this.transacted = transacted;
      this.acknowledgeMode = acknowledgeMode;
   }

   public void start()
   {
      for (SqsQueueReceiver receiver : allReceivers)
      {
         receiver.start() ;
      }
      
      for (SqsQueueSender sender : allSenders)
      {
         sender.start() ;
      }
   }
   
   public void stop()
   {
      for (SqsQueueReceiver receiver : allReceivers)
      {
         receiver.stop() ;
      }
      
      for (SqsQueueSender sender : allSenders)
      {
         sender.stop() ;
      }
   }
   
   public QueueService getQueueService()
   {
      return queueService;
   }

   public QueueBrowser createBrowser(Queue queue) throws JMSException
   {
      return createBrowser(queue, null) ;
   }
   
   private final SqsQueueSender register(SqsQueueSender sender)
   {
      allSenders.add(sender);
      return sender;
   }

   private final SqsQueueReceiver register(SqsQueueReceiver receiver)
   {
      allReceivers.add(receiver);
      return receiver;
   }

   public QueueBrowser createBrowser(Queue queue, String selector) throws JMSException
   {
      try
      {
         return new SqsQueueBrowser(queue, queueService.getOrCreateMessageQueue(queue.getQueueName()), connectionFactory, selector);
      }
      catch (SQSException ex)
      {
         throw new SqsException(ex);
      }
   }

   public Queue createQueue(String name) throws JMSException
   {
      try
      {
         return new SqsQueue(queueService.getOrCreateMessageQueue(name), name);
      }
      catch (SQSException ex)
      {
         throw new SqsException(ex);
      }
   }

   public QueueReceiver createReceiver(Queue queue) throws JMSException
   {
     return createReceiver(queue, null) ;
   }

   public QueueReceiver createReceiver(Queue queue, String selector) throws JMSException
   {
      try
      {
         if (queue instanceof SqsQueue)
         {
            return register(new SqsQueueReceiver(this, (SqsQueue) queue,  connectionFactory, selector));
         }
         else
         {
            throw new SqsException("Destination is from another provider") ;
         }
      }
      catch (Exception ex)
      {
         throw new SqsException(ex);
      }
   }

   public QueueSender createSender(Queue queue) throws JMSException
   {
     return register(new SqsQueueSender(((SqsQueue) queue), this)) ;
   }

   public TemporaryQueue createTemporaryQueue() throws JMSException
   {
      VMID vmid = new VMID();

      return null;
   }

   public void close() throws JMSException
   {
      closed.set(true);

   }

   public void commit() throws JMSException
   {
      for (SqsQueueReceiver receiver : allReceivers)
      {
         receiver.commit();
      }
      
      for (SqsQueueSender sender : allSenders)
      {
         sender.commit();
      }

   }

   public BytesMessage createBytesMessage() throws JMSException
   {
      throw new SqsNotImplementedException() ;
   }

   public MessageConsumer createConsumer(Destination destination) throws JMSException
   {
      return createConsumer(destination, null) ;
   }

   public MessageConsumer createConsumer(Destination destination, String selector) throws JMSException
   {
      if (destination instanceof SqsQueue)
      {
         return new SqsQueueReceiver(this, (SqsQueue) destination, connectionFactory, selector) ;
      }
      else
      {
         throw new SqsException("Destination is from another provider") ;
      }
   }

   public MessageConsumer createConsumer(Destination destination, String selector, boolean noLocal) throws JMSException
   {
      return createConsumer(destination, selector) ;
   }

   public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1) throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1, String arg2, boolean arg3) throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public MapMessage createMapMessage() throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public Message createMessage() throws JMSException
   {
      throw new SqsNotImplementedException() ;
   }

   public ObjectMessage createObjectMessage() throws JMSException
   {
      throw new SqsNotImplementedException() ;
   }

   public ObjectMessage createObjectMessage(Serializable arg0) throws JMSException
   {
      throw new SqsNotImplementedException() ;
   }

   public MessageProducer createProducer(Destination destination) throws JMSException
   {
      if (destination == null)
      {
         return register(new SqsQueueSender(null, this)) ;
      }
      else if (destination instanceof SqsQueue)
      {
         return register(new SqsQueueSender((SqsQueue) destination, this)) ;
      }
      else
      {
         throw new SqsException("Destination is from another provider") ;
      }
   }

   public StreamMessage createStreamMessage() throws JMSException
   {
      throw new SqsNotImplementedException() ;
   }

   public TemporaryTopic createTemporaryTopic() throws JMSException
   {
      throw new SqsNotImplementedException() ;
   }

   public TextMessage createTextMessage() throws JMSException
   {
      return new TextMessageImpl();
   }

   public TextMessage createTextMessage(String arg0) throws JMSException
   {
      return new TextMessageImpl(arg0);
   }

   public Topic createTopic(String arg0) throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public int getAcknowledgeMode() throws JMSException
   {
      return acknowledgeMode ;
   }

   public MessageListener getMessageListener() throws JMSException
   {
     return messageListener ;
   }

   public boolean getTransacted() throws JMSException
   {
      return transacted;
   }

   public void recover() throws JMSException
   {
      // TODO Auto-generated method stub

   }

   public void rollback() throws JMSException
   {
      for (SqsQueueReceiver receiver : allReceivers)
      {
         receiver.rollback();
      }
      
      for (SqsQueueSender sender : allSenders)
      {
         sender.rollback();
      }

   }

   private void pollAndDeliver()
   {
      while (!closed.get())
      {
         if (!run0())
         {
            try
            {
               Thread.sleep(connectionFactory.getQueuePollTimeout());
            }
            catch (InterruptedException e)
            {
               log.error(e.getMessage(), e);
            }
         }
      }

   }

   private boolean run0()
   {
      boolean delivered = false;

      for (final SqsQueueReceiver receiver : asynchronousReceivers)
      {
         try
         {
            if (receiver.poll())
            {
               receiver.deliver();
               delivered = true;
            }
         }
         catch (JMSException ex)
         {
            log.error(ex.getMessage(), ex);
         }
      }

      return delivered;
   }

   public void run()
   {
      run0();
   }

   public void setMessageListener(MessageListener messageListener) throws JMSException
   {
    this.messageListener = messageListener ;

   }

   public void unsubscribe(String arg0) throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public void registerAsynchronous(SqsQueueReceiver receiver)
   {
      asynchronousReceivers.add(receiver);

      if (asychronousDelivery == null)
      {
         asychronousDelivery = new Thread(new Runnable()
         {

            public void run()
            {
               pollAndDeliver();
               asychronousDelivery = null;
            }
         });
      }
   }

   public void unregisterAsynchronous(SqsQueueReceiver receiver)
   {
      asynchronousReceivers.remove(receiver);
   }
}
