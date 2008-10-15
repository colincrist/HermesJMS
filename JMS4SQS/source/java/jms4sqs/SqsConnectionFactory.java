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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import com.xerox.amazonws.sqs2.QueueService;
import com.xerox.amazonws.sqs2.SQSUtils;

public class SqsConnectionFactory implements QueueConnectionFactory {

	private String hostname;
	private boolean secure = false;
	private int maxConnections = 1;
	private String proxyHost;
	private int proxyPort;
	private String proxyUsername;
	private String proxyPassword;
	private String proxyDomain;
	private int visibilityTimeout = 0;
	private int preFetch = 10;
	private long queuePollTimeout = 500;

	public long getQueuePollTimeout() {
		return queuePollTimeout;
	}

	public void setQueuePollTimeout(long queuePollTimeout) {
		this.queuePollTimeout = queuePollTimeout;
	}

	public int getPreFetch() {
		return preFetch;
	}

	public void setPreFetch(int preFetch) {
		this.preFetch = preFetch;
	}

	public int getVisibilityTimeout() {
		return visibilityTimeout;
	}

	public void setVisibilityTimeout(int visibilityTimeout) {
		this.visibilityTimeout = visibilityTimeout;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public String getProxyDomain() {
		return proxyDomain;
	}

	public void setProxyDomain(String proxyDomain) {
		this.proxyDomain = proxyDomain;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public Connection createConnection() throws JMSException {
		return createQueueConnection();
	}

	public Connection createConnection(String arg0, String arg1)
			throws JMSException {
		return createQueueConnection(arg0, arg1);
	}

	public QueueConnection createQueueConnection() throws JMSException {
		throw new SqsException(
				"Username (AWS Access Id) and password (AWS Secret Key) required");
	}

	public QueueConnection createQueueConnection(String username,
			String password) throws JMSException {

		QueueService queueService = (hostname == null) ? new QueueService(
				username, password, isSecure()) : new QueueService(username,
				password, isSecure(), hostname);

		queueService.setMaxConnections(getMaxConnections());
		

		if (proxyUsername != null) {
			if (proxyDomain != null) {
				queueService.setProxyValues(proxyHost, proxyPort,
						proxyUsername, proxyPassword, proxyDomain);
			} else {
				queueService.setProxyValues(proxyHost, proxyPort,
						proxyUsername, proxyPassword);
			}
		}
		return new SqsConnection(this, queueService);
	}

}
