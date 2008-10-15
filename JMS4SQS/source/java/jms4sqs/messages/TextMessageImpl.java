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

package jms4sqs.messages;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import com.xerox.amazonws.sqs2.Message;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: TextMessageImpl.java,v 1.3 2004/09/16 20:30:49 colincrist Exp $
 */
public class TextMessageImpl extends MessageImpl implements TextMessage
{
    private String text = "";
    private Message message ;
    
    public TextMessageImpl()
    {
        super();
    }
    /**
     *  
     */
    public TextMessageImpl(Message message)
    {
        super();
        this.message = message ;
        this.text = message.getMessageBody() ;
        this.messageId = message.getMessageId() ;   
        try
        {
        	setStringProperty("JMSX_SQS_RECEIPT_HANDLE", message.getReceiptHandle()) ;
        }
        catch (JMSException ex)
        {
        	throw new RuntimeException(ex) ;
        }
    }

    /**
     *  
     */
    public TextMessageImpl(String text)
    {
        super();

        this.text = text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.jms.Message#clearBody()
     */
    public void clearBody() throws JMSException
    {
        text = "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.jms.TextMessage#getText()
     */
    public String getText() throws JMSException
    {
        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.jms.TextMessage#setText(java.lang.String)
     */
    public void setText(String arg0) throws JMSException
    {
        text = arg0;
    }

}