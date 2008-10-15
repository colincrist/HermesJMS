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

import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;

public class SqsUtils {

	public static boolean readMessages(MessageQueue messageQueue,
			ConcurrentLinkedQueue<javax.jms.Message> jmsMessages, int prefetch)
			throws SQSException {

		final Message[] messages = messageQueue.receiveMessages(prefetch);

		for (Message message : messages) {
			jmsMessages.add(MessageFactory.create(message));
		}

		return messages.length > 0;
	}

}
