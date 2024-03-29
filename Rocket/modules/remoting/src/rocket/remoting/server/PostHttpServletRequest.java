/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.remoting.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import rocket.remoting.client.Headers;
import rocket.remoting.client.RequestParameters;
import rocket.util.client.Checker;

/**
 * This request supports simulating posting of data from the client using rpc
 * eventually making a regular POSt to a local web app resource.
 * 
 * @author Miroslav Pokorny (mP)
 */
class PostHttpServletRequest extends GetOrPostHttpServletRequest implements HttpServletRequest {

	public PostHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers,
			final RequestParameters parameters) {
		super(request, url, headers);

		this.setRequestParameters(parameters);
	}

	public PostHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers, final byte[] data) {
		super(request, url, headers);

		this.setData(data);
	}

	/**
	 * The bytes that are the post data.
	 */
	private byte[] data;

	public byte[] getData() {
		if (this.hasRequestParameters()) {
			throw new IllegalStateException("Cannot retrieve Post data as request parameters have already been read.");
		}

		if (false == this.hasData()) {
			throw new IllegalStateException("This request was created without any post data");
		}
		return this.data;
	}

	protected boolean hasData() {
		return this.data != null;
	}

	protected void setData(final byte[] data) {
		Checker.notNull("parameter:data", data);
		this.data = data;
	}

	@Override
	public String getMethod() {
		return "POST";
	}

	@Override
	public int getContentLength() {
		return this.getData().length;
	}

	/**
	 * An inputStream being used to read the post data from this request. There
	 * can only ever be either an inputStream or reader active never both.
	 */
	private ServletInputStream inputStream;

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (false == this.hasInputStream()) {
			this.createInputStream();
		}

		Checker.notNull("inputStream", this.inputStream);
		return this.inputStream;
	}

	protected boolean hasInputStream() {
		return null != this.inputStream;
	}

	protected void setInputStream(final ServletInputStream inputStream) {
		Checker.notNull("parameter:inputStream", inputStream);
		Checker.trueValue("existing inputStream", this.hasInputStream());
		Checker.falseValue("existing reader", this.hasReader());

		this.inputStream = inputStream;
	}

	protected void createInputStream() {
		Checker.notNull("parameter:inputStream", inputStream);
		this.setInputStream(new ByteArrayServletInputStream(this.getData()));
	}

	/**
	 * An BufferedReader being used to read the post data from this request.
	 * There can only ever be either an inputStream or reader active never both.
	 */
	private BufferedReader reader;

	public BufferedReader getReader() throws IOException {
		if (false == this.hasReader()) {
			this.createReader();
		}

		Checker.notNull("field:reader", this.reader);
		return this.reader;
	}

	protected boolean hasReader() {
		return null != this.reader;
	}

	protected void setReader(final BufferedReader reader) {
		Checker.notNull("parameter:reader", reader);
		Checker.falseValue("reader", this.hasReader());
		Checker.falseValue("inputStream", this.hasInputStream());

		this.reader = reader;
	}

	protected void createReader() {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.getData())));
		this.setReader(reader);
	}

	protected RequestParameters getRequestParameters() {
		if (false == this.hasRequestParameters()) {
			this.createRequestParameters();
		}

		return super.getRequestParameters();
	}

	protected void createRequestParameters() {
		final String postData = new String(this.getData());

		final RequestParameters parameters = new RequestParameters();
		parameters.buildFromQueryString(postData);
		this.setRequestParameters(parameters);
	}

	@Override
	public String toString() {
		return super.toString() + ", data: " + data + ", inputStream: " + inputStream + ", reader: " + reader;
	}
}
