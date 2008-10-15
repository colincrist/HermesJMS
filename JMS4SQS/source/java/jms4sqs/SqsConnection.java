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

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import com.xerox.amazonws.sqs2.QueueService;

public class SqsConnection implements QueueConnection
{
   private QueueService queueService;
   private SqsConnectionFactory connectionFactory;
   private ExceptionListener exceptionListener;
   private boolean started = false;
   private boolean closed = false;
   private String clientID;
   private Collection<SqsQueueSession> sessions = new ConcurrentLinkedQueue<SqsQueueSession>();

   public SqsConnection(SqsConnectionFactory connectionFactory, QueueService queueService)
   {
      this.queueService = queueService;
      this.connectionFactory = connectionFactory;
   }

   private SqsQueueSession register(SqsQueueSession session)
   {
      sessions.add(session);
      return session;
   }

   public ConnectionConsumer createConnectionConsumer(Queue arg0, String arg1, ServerSessionPool arg2, int arg3) throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException
   {
      checkClosed();
      return register(new SqsQueueSession(queueService, connectionFactory, transacted, acknowledgeMode));
   }

   private void checkClosed() throws JMSException
   {
      if (closed)
      {
         throw new JMSException("Connection is closed");
      }
   }

   public void close() throws JMSException
   {
      for (SqsQueueSession session : sessions)
      {
         session.close();
      }

      sessions.clear();
      closed = true;
   }

   public ConnectionConsumer createConnectionConsumer(Destination arg0, String arg1, ServerSessionPool arg2, int arg3) throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public ConnectionConsumer createDurableConnectionConsumer(Topic arg0, String arg1, String arg2, ServerSessionPool arg3, int arg4) throws JMSException
   {
      throw new SqsNotImplementedException();
   }

   public Session createSession(boolean arg0, int arg1) throws JMSException
   {
      return createQueueSession(arg0, arg1);
   }

   public String getClientID() throws JMSException
   {
      return clientID;
   }

   public ExceptionListener getExceptionListener() throws JMSException
   {
      return exceptionListener;
   }

   public ConnectionMetaData getMetaData() throws JMSException
   {
      // TODO Auto-generated method stub
      return null;
   }

   public void setClientID(String clientID) throws JMSException
   {
      this.clientID = clientID;

   }

   public void setExceptionListener(ExceptionListener exceptionListener) throws JMSException
   {
      this.exceptionListener = exceptionListener;
   }

   public void start() throws JMSException
   {
      started = true;

      for (SqsQueueSession session : sessions)
      {
         session.start();
      }
   }

   public void stop() throws JMSException
   {
      started = false;

      for (SqsQueueSession session : sessions)
      {
         session.stop();
      }
   }

}
