/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.converter;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

/**
 * Implementation of {@link HttpMessageConverter} that can read and write {@link Resource Resources}.
 * 
 * <p>
 * By default, this converter can read all media types. {@code application/octet-stream} is used to determine the
 * {@code Content-Type} of written resources.
 * 
 * @author Arjen Poutsma
 * @author Roy Clarkson
 * @since 1.0
 */
public class ResourceHttpMessageConverter extends AbstractHttpMessageConverter<Resource> {

	public ResourceHttpMessageConverter() {
		super(MediaType.ALL);
	}


	@Override
	protected boolean supports(Class<?> clazz) {
		return Resource.class.isAssignableFrom(clazz);
	}

	@Override
	protected Resource readInternal(Class<? extends Resource> clazz, HttpInputMessage inputMessage) 
			throws IOException, HttpMessageNotReadableException {

		byte[] body = FileCopyUtils.copyToByteArray(inputMessage.getBody());
		return new ByteArrayResource(body);
	}

	@Override
	protected MediaType getDefaultContentType(Resource resource) {
		return MediaType.APPLICATION_OCTET_STREAM;
	}

	@Override
	protected Long getContentLength(Resource resource, MediaType contentType) throws IOException {
		return resource.contentLength();
	}

	@Override
	protected void writeInternal(Resource resource, HttpOutputMessage outputMessage) 
			throws IOException, HttpMessageNotWritableException {

		FileCopyUtils.copy(resource.getInputStream(), outputMessage.getBody());
		outputMessage.getBody().flush();
	}

}
