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

import java.util.ArrayList;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

import com.xerox.amazonws.sqs2.SQSException;

/**
 * @author colincrist@hermesjms.com
 * @version $Id$
 */

public class SqsQueueSender implements QueueSender
{

   private SqsQueue queue;
   private SqsQueueSession session;
   private int deliveryMode;
   private int priority;
   private long timeToLive;
   private ArrayList<Message> outboundMessages = new ArrayList<Message>();

   public SqsQueueSender(SqsQueue queue, SqsQueueSession session, int deliveryMode, int priority, long timeToLive)
   {
      this.queue = queue;
      this.session = session;
      this.deliveryMode = deliveryMode;
      this.priority = priority;
      this.timeToLive = timeToLive;
   }

   public SqsQueueSender(SqsQueue queue, SqsQueueSession session)
   {
      this(queue, session, DeliveryMode.PERSISTENT, 1, 0);
   }

   public void stop()
   {
      // @@TODO
   }

   public void start()
   {
      // @@TODO
   }

   public void close() throws JMSException
   {

   }

   public int getDeliveryMode() throws JMSException
   {
      return deliveryMode;
   }

   public Destination getDestination() throws JMSException
   {
      return queue;
   }

   public boolean getDisableMessageID() throws JMSException
   {
      return false;
   }

   public boolean getDisableMessageTimestamp() throws JMSException
   {
      return false;
   }

   public int getPriority() throws JMSException
   {
      return priority;
   }

   public long getTimeToLive() throws JMSException
   {
      return timeToLive;
   }

   private void send0(SqsQueue queue, String message) throws JMSException
   {
      try
      {
         queue.getMessageQueue().sendMessage(message);
      }
      catch (SQSException ex)
      {
         throw new SqsException(ex);
      }
   }

   public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
   {
      send0(destination, message);
   }

   public void commit() throws JMSException
   {
      for (Message message : outboundMessages)
      {
         send0((SqsQueue) message.getJMSDestination(), MessageFactory.create(message));
      }

      outboundMessages.clear();
   }

   public void rollback()
   {
      outboundMessages.clear();
   }

   private void send0(Destination destination, Message message) throws JMSException
   {
      if (destination instanceof SqsQueue)
      {
         if (session.getTransacted())
         {
            message.setJMSDestination(destination);
            outboundMessages.add(message);
         }
         else
         {
            send0((SqsQueue) destination, MessageFactory.create(message));
         }
      }
      else
      {
         throw new SqsException("Destination is not from this JMS provider");
      }
   }

   public void send(Destination destination, Message message) throws JMSException
   {
      send0(destination, message);
   }

   public void setDeliveryMode(int deliveryMode) throws JMSException
   {
      this.deliveryMode = deliveryMode;
   }

   public void setDisableMessageID(boolean arg0) throws JMSException
   {

   }

   public void setDisableMessageTimestamp(boolean arg0) throws JMSException
   {

   }

   public void setPriority(int priority) throws JMSException
   {
      this.priority = priority;

   }

   public void setTimeToLive(long timeToLive) throws JMSException
   {
      this.timeToLive = timeToLive;

   }

   public Queue getQueue() throws JMSException
   {
      return queue;
   }

   public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
   {
      send(message.getJMSDestination(), message, deliveryMode, priority, timeToLive);
   }

   public void send(Message message) throws JMSException
   {
      send(queue, message);

   }

   public void send(Queue destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
   {
      send0(destination, message);

   }

   public void send(Queue destination, Message message) throws JMSException
   {
      send0(destination, message);

   }

}
