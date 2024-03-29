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
package rocket.widget.rebind.imagefactory;

import rocket.widget.client.Image;
import rocket.widget.client.ImageFactory;
import rocket.widget.client.ImageFactoryImpl;

public class ImageFactoryConstants {
	final static String SUFFIX = "__{0}Impl";
	final static String USER_AGENT = "user.agent";
	final static String IMAGE_TYPE = Image.class.getName();
	final static String GWT_IMAGE_TYPE = com.google.gwt.user.client.ui.Image.class.getName();
	final static String IMAGE_FACTORY_IMPL_TYPE = ImageFactoryImpl.class.getName();

	private final static String IMAGE_FACTORY = ImageFactory.class.getName();
	public final static String IMAGE_FILE = "file";

	public final static String LOCATION = "location";
	public final static String LOCATION_LOCAL = "local";
	public final static String LOCATION_SERVER = "server";

	public final static String SERVER_REQUEST = "serverRequest";
	public final static String SERVER_REQUEST_LAZY = "lazy";
	public final static String SERVER_REQUEST_EAGER = "eager";

	private final static String MAX_DATA_URL_LENGTH = "maxDataUrlLength";

	final static String FIREFOX_MAXIMUM_DATA_URL_SIZE = IMAGE_FACTORY + ".FireFox." + MAX_DATA_URL_LENGTH;
	final static int FIREFOX_MAXIMUM_DATA_URL_SIZE_DEFAULT_VALUE = 32768;

	final static String SAFARI_MAXIMUM_DATA_URL_SIZE = IMAGE_FACTORY + ".FireSafari." + MAX_DATA_URL_LENGTH;
	final static int SAFARI_MAXIMUM_DATA_URL_SIZE_DEFAULT_VALUE = 32768;

	final static String INTERNET_EXPLORER_MAXIMUM_DATA_URL_SIZE = IMAGE_FACTORY + ".InternetExplorer." + MAX_DATA_URL_LENGTH;
	final static int INTERNET_EXPLORER_MAXIMUM_DATA_URL_SIZE_DEFAULT_VALUE = 0;

	final static String OPERA_MAXIMUM_DATA_URL_SIZE = IMAGE_FACTORY + ".Opera." + MAX_DATA_URL_LENGTH;
	final static int OPERA_MAXIMUM_DATA_URL_SIZE_DEFAULT_VALUE = 4100;

	final static String IMAGE_RESOURCE_SUFFIX = ".cache.";

	final static String TEMPLATE = "create-image.txt";
	final static String URL_PARAMETER = "url";
	final static String URL_TEMPLATE_PLACEHOLDER = "url";
	final static String IMAGE_TYPE_TEMPLATE_PLACEHOLDER = "imageType";

	final static String GET_PRELOAD_URLS = "getPreloadUrls";
}
