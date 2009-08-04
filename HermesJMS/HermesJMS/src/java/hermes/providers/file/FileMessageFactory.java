/* 
 * Copyright 2003,2004 Colin Crist
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

package hermes.providers.file;

import hermes.Domain;
import hermes.HermesException;
import hermes.MessageFactory;
import hermes.providers.messages.MapMessageImpl;
import hermes.providers.messages.MessageImpl;
import hermes.providers.messages.ObjectMessageImpl;
import hermes.providers.messages.TextMessageImpl;

import java.io.File;
import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.NamingException;

/**
 * An XML file provider.
 * 
 * @author colincrist@hermesjms.com
 * @version $Id: FileMessageFactory.java,v 1.1 2004/05/01 15:52:35 colincrist
 *          Exp $
 */

public class FileMessageFactory implements MessageFactory
{

    /**
     *  
     */
    public FileMessageFactory()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createBytesMessage()
     */
    public BytesMessage createBytesMessage() throws JMSException
    {
       throw new HermesException("BytesMessage not supported") ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createMapMessage()
     */
    public MapMessage createMapMessage() throws JMSException
    {
        return new MapMessageImpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createObjectMessage()
     */
    public ObjectMessage createObjectMessage() throws JMSException
    {
        return new ObjectMessageImpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createStreamMessage()
     */
    public StreamMessage createStreamMessage() throws JMSException
    {
       throw new HermesException("StreamMessage not supported") ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createTextMessage()
     */
    public TextMessage createTextMessage() throws JMSException
    {
        return new TextMessageImpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createTextMessage(java.lang.String)
     */
    public TextMessage createTextMessage(String text) throws JMSException
    {
        return new TextMessageImpl(text);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createMessage()
     */
    public Message createMessage() throws JMSException
    {
        return new MessageImpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#getDestination(java.lang.String)
     */
    public Destination getDestination(String name, Domain domain) throws JMSException, NamingException
    {
        File file = new File(name);

        return new FileQueue(file);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#getDestinationName(javax.jms.Destination)
     */
    public String getDestinationName(Destination to) throws JMSException
    {
        if (to instanceof Queue)
        {
            return ((Queue) to).getQueueName();
        }
        else
        {
            return ((Topic) to).getTopicName();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see hermes.MessageFactory#createObjectMessage(java.io.Serializable)
     */
    public ObjectMessage createObjectMessage(Serializable object) throws JMSException
    {
        return new ObjectMessageImpl(object);
    }

}