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

import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;

/**
 * @author colincrist@hermesjms.com
 * @version $Id$
 */

public class SqsTemporaryQueue extends SqsQueue implements TemporaryQueue
{

  

   public SqsTemporaryQueue(MessageQueue queue, String name)
   {
      super(queue, name);
      // TODO Auto-generated constructor stub
   }

   public void delete() throws JMSException
   {
      try
      {
         queue.deleteQueue() ;
      }
      catch (SQSException ex)
      {
         throw new SqsException(ex) ;
      }
   }

}
